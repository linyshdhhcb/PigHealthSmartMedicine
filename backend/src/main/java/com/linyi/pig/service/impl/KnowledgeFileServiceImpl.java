package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.KnowledgeFile;
import com.linyi.pig.entity.vo.knowledge.KnowledgeFileQueryVo;
import com.linyi.pig.entity.vo.knowledge.KnowledgeFileUpdateVo;
import com.linyi.pig.mapper.KnowledgeFileMapper;
import com.linyi.pig.service.KnowledgeFileService;
import com.linyi.pig.exception.LinyiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class KnowledgeFileServiceImpl extends ServiceImpl<KnowledgeFileMapper, KnowledgeFile>
        implements KnowledgeFileService {

    @Value("${knowledge.storage.root:classpath:knowledge}")
    private String storageRootConfig;

    private Path resolveKnowledgeRoot() throws IOException {
        // 默认使用应用工作目录下的 resources/knowledge（适配开发环境与部署环境）
        Path projectRoot = Paths.get("").toAbsolutePath();
        Path defaultRoot = projectRoot.resolve("backend").resolve("src").resolve("main").resolve("resources")
                .resolve("knowledge");
        Path root = defaultRoot;
        if (storageRootConfig != null && !storageRootConfig.isBlank()
                && !"classpath:knowledge".equals(storageRootConfig)) {
            root = Paths.get(storageRootConfig);
        }
        Files.createDirectories(root);
        return root;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KnowledgeFile saveToKnowledge(MultipartFile file, Integer createBy, String remark) {
        try (InputStream in = file.getInputStream()) {
            String md5 = DigestUtils.md5Hex(in);

            KnowledgeFile exist = lambdaQuery().eq(KnowledgeFile::getFileMd5, md5).one();
            if (exist != null) {
                return exist;
            }

            String originalName = file.getOriginalFilename();
            String contentType = file.getContentType();
            long size = file.getSize();

            String lowerName = originalName == null ? "" : originalName.toLowerCase();
            boolean allowedExt = lowerName.endsWith(".txt") || lowerName.endsWith(".md") || lowerName.endsWith(".pdf")
                    || lowerName.endsWith(".doc") || lowerName.endsWith(".docx");
            if (!allowedExt) {
                throw new LinyiException("仅支持上传 txt、md、pdf、word(doc/docx) 文件");
            }

            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            // 生成不依赖操作系统分隔符的相对路径
            String relativeDir = datePath;
            String saveName = System.currentTimeMillis() + "_" + (originalName == null ? "unknown" : originalName);

            Path root = resolveKnowledgeRoot();
            Path targetDir = root.resolve(relativeDir);
            Files.createDirectories(targetDir);
            Path target = targetDir.resolve(saveName);

            file.transferTo(target.toFile());

            KnowledgeFile kf = new KnowledgeFile();
            kf.setFileName(originalName);
            // 将相对路径标准化为使用正斜杠存储，便于跨平台
            String relativePath = Paths.get(relativeDir, saveName).toString().replace('\\', '/');
            kf.setFilePath(relativePath);
            kf.setFileSize(size);
            kf.setFileMd5(md5);
            kf.setFileType(contentType);
            kf.setCreateBy(createBy);
            kf.setCreateTime(LocalDateTime.now());
            kf.setUpdateTime(LocalDateTime.now());
            kf.setRemark(remark);

            save(kf);
            return kf;
        } catch (IOException e) {
            throw new RuntimeException("保存知识库文件失败", e);
        }
    }

    @Override
    public PageResult<KnowledgeFile> page(KnowledgeFileQueryVo vo) {
        LambdaQueryWrapper<KnowledgeFile> qw = new LambdaQueryWrapper<>();
        if (vo.getFileName() != null && !vo.getFileName().isBlank()) {
            qw.like(KnowledgeFile::getFileName, vo.getFileName());
        }
        if (vo.getFilePath() != null && !vo.getFilePath().isBlank()) {
            qw.like(KnowledgeFile::getFilePath, vo.getFilePath());
        }
        if (vo.getFileType() != null && !vo.getFileType().isBlank()) {
            qw.eq(KnowledgeFile::getFileType, vo.getFileType());
        }
        if (vo.getFileMd5() != null && !vo.getFileMd5().isBlank()) {
            qw.eq(KnowledgeFile::getFileMd5, vo.getFileMd5());
        }
        Page<KnowledgeFile> page = new Page<>(vo.getPageNum(), vo.getPageSize());
        Page<KnowledgeFile> res = this.baseMapper.selectPage(page, qw);
        return new PageResult<>(res.getRecords(), res.getTotal(), res.getPages(), vo.getPageNum(), vo.getPageSize());
    }

    @Override
    public boolean updateRemark(KnowledgeFileUpdateVo vo) {
        KnowledgeFile db = getById(vo.getId());
        if (db == null)
            return false;
        db.setRemark(vo.getRemark());
        db.setUpdateTime(LocalDateTime.now());
        return updateById(db);
    }

    private boolean deletePhysicalFile(String relativePath) {
        try {
            Path root = resolveKnowledgeRoot();
            Path target = root.resolve(relativePath.replace('/', java.io.File.separatorChar));
            java.nio.file.Files.deleteIfExists(target);
            return true;
        } catch (Exception e) {
            // 物理文件删除失败不应导致事务回滚，可记录日志
            log.warn("删除物理文件失败: {}", relativePath, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeWithFileById(Long id) {
        KnowledgeFile kf = getById(id);
        if (kf == null)
            return false;
        boolean dbOk = removeById(id);
        deletePhysicalFile(kf.getFilePath());
        return dbOk;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeWithFileByIds(java.util.List<Long> ids) {
        if (ids == null || ids.isEmpty())
            return true;
        java.util.List<KnowledgeFile> list = listByIds(ids);
        boolean dbOk = removeByIds(ids);
        for (KnowledgeFile kf : list) {
            if (kf != null)
                deletePhysicalFile(kf.getFilePath());
        }
        return dbOk;
    }
}
