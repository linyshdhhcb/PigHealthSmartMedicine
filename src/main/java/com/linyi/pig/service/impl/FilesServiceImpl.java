package com.linyi.pig.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.config.MinioComponent;
import com.linyi.pig.config.MinioConfiguration;
import com.linyi.pig.entity.Files;
import com.linyi.pig.entity.vo.files.FilesAddVo;
import com.linyi.pig.entity.vo.files.FilesQueryVo;
import com.linyi.pig.entity.vo.files.FilesUpdateVo;
import com.linyi.pig.exception.LinyiException;
import com.linyi.pig.mapper.FilesMapper;
import com.linyi.pig.service.FilesService;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: FilesServiceImpl
* @Version: 1.0
* @Description: 文件信息 服务实现层
*/
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@SuppressWarnings({"unchecked", "rawtypes"})
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files> implements FilesService {

    @Autowired
    private FilesMapper filesMapper;

    @Autowired
    private MinioComponent minioComponent;

    @Autowired
    private MinioConfiguration minioConfiguration;

    @Override
    public PageResult<Files> filesPage(FilesQueryVo filesQueryVo) {
        LambdaQueryWrapper<Files> queryWrapper = new LambdaQueryWrapper<>();
        //TODO 需要补充条件查询

        //分页数据
        Page<Files> page = new Page<>(filesQueryVo.getPageNum(),filesQueryVo.getPageSize());
        //查询数据
        Page<Files> pageNew = filesMapper.selectPage(page, queryWrapper);
        //返回分页数据
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(), filesQueryVo.getPageNum(), filesQueryVo.getPageSize());
    }

    @Override
    public Boolean filesAdd(FilesAddVo filesAddVo){
        //创建实体对象
        Files files = new Files();
        //复制属性
        BeanUtils.copyProperties(filesAddVo, files);
        //插入数据
        return filesMapper.insert(files) > 0 ? true : false;
    }

    @Override
    public Boolean filesUpdate(FilesUpdateVo filesUpdateVo){
        //根据ID查询数据
        Files byId=this.getById(filesUpdateVo.getId());
        //判断数据是否存在
        if(Optional.ofNullable(byId).isEmpty()){
            log.error("数据不存在");
            return false;
        }
        //复制属性
        BeanUtils.copyProperties(filesUpdateVo, byId);
        //修改数据
        return filesMapper.updateById(byId) > 0 ? true : false;
    }

    @Override
    public FilesAddVo updoadFileAndName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        //获取文件拓展名
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());

        String fileName = IdUtil.simpleUUID()+substring;

        //将时间格式化yyyy/MM/dd
        String format = DateUtil.format(new Date(), "yyyy-MM-dd").toString().replace("-","/");
        String path = "/" + minioConfiguration.getBucketName() + "/" + format + "/" + fileName;
        //获取文件的文件的 MIME 类型
        String contentType = file.getContentType();

        // 调用MinIO组件的上传方法，将文件上传到MinIO
        minioComponent.upload(file,"/" + format + "/" + fileName);
        FilesAddVo filesAddVo = new FilesAddVo();
        filesAddVo.setFilePath(path);
        filesAddVo.setBucketName(minioConfiguration.getBucketName());
        filesAddVo.setUrl(minioConfiguration.getUrl()+path);
        filesAddVo.setFileSize(file.getSize());
        filesAddVo.setFileName(fileName);
        filesAddVo.setContentType(contentType);
        //将上传文件信息插入数据库中
        Boolean b = filesAdd(filesAddVo);
        if (!b){
            log.error("文件上传失败");
            throw new LinyiException("文件上传失败");
        }
      return filesAddVo;
    }

    @Override
    public Map policy(String fileName) {
        // 调用MinIO组件的getPolicy方法，获取预签名的策略信息
        // 参数包括文件名和过期时间（当前时间加上10分钟）
        Map policy = minioComponent.getPolicy(fileName, ZonedDateTime.now().plusMinutes(10));
        return policy;
    }

    @Override
    public String getPolicyUrl(String fileName) {
        // 参数包括文件名、HTTP方法、过期时间和时间单位
        String url = minioComponent.getPolicyUrl(fileName, Method.PUT, 2, TimeUnit.MINUTES);
        return url;
    }

    @Override
    public String getUrl(String fileName) {
        // 调用MinIO组件的方法获取文件的临时URL，并设置URL的过期时间为7天
        String url = minioComponent.getUrl(fileName, 7, TimeUnit.DAYS);
        return url;
    }

}
