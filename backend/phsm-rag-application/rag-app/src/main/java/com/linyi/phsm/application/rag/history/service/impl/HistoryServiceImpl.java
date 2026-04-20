package com.linyi.phsm.application.rag.history.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linyi.phsm.application.rag.history.service.HistoryService;
import com.linyi.phsm.domain.rag.model.entity.HistoryDO;
import com.linyi.phsm.domain.rag.model.request.HistoryCreateRequest;
import com.linyi.phsm.domain.rag.model.request.HistoryPageRequest;
import com.linyi.phsm.domain.rag.model.vo.HistoryVO;
import com.linyi.phsm.framework.context.LoginUser;
import com.linyi.phsm.framework.context.UserContext;
import com.linyi.phsm.framework.exception.ClientException;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.HistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryMapper historyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void record(HistoryCreateRequest requestParam) {
        LoginUser user = UserContext.requireUser();
        HistoryDO row = HistoryDO.builder()
                .userId(user.getUserId())
                .keyword(StrUtil.trim(requestParam.getKeyword()))
                .operateType(requestParam.getOperateType())
                .createdBy(user.getUsername())
                .updatedBy(user.getUsername())
                .build();
        historyMapper.insert(row);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        HistoryDO row = loadAuthorized(id);
        historyMapper.deleteById(row.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearMine() {
        LoginUser user = UserContext.requireUser();
        if (StpUtil.hasRole("admin")) {
            historyMapper.delete(
                    Wrappers.lambdaQuery(HistoryDO.class).eq(HistoryDO::getDeleted, 0)
            );
            return;
        }
        historyMapper.delete(
                Wrappers.lambdaQuery(HistoryDO.class)
                        .eq(HistoryDO::getUserId, user.getUserId())
                        .eq(HistoryDO::getDeleted, 0)
        );
    }

    @Override
    public HistoryVO queryById(String id) {
        return toVo(loadAuthorized(id));
    }

    @Override
    public IPage<HistoryVO> pageQuery(HistoryPageRequest requestParam) {
        String kw = StrUtil.trim(requestParam.getKeyword());
        Integer op = requestParam.getOperateType();
        Page<HistoryDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        var wrapper = Wrappers.lambdaQuery(HistoryDO.class)
                .eq(HistoryDO::getDeleted, 0)
                .eq(op != null, HistoryDO::getOperateType, op)
                .like(StrUtil.isNotBlank(kw), HistoryDO::getKeyword, kw)
                .orderByDesc(HistoryDO::getCreateTime);
        if (!StpUtil.hasRole("admin")) {
            LoginUser user = UserContext.requireUser();
            wrapper.eq(HistoryDO::getUserId, user.getUserId());
        }
        IPage<HistoryDO> result = historyMapper.selectPage(page, wrapper);
        return result.convert(this::toVo);
    }

    private HistoryDO load(String id) {
        HistoryDO row = historyMapper.selectOne(
                Wrappers.lambdaQuery(HistoryDO.class)
                        .eq(HistoryDO::getId, id)
                        .eq(HistoryDO::getDeleted, 0)
        );
        Assert.notNull(row, () -> new ClientException("记录不存在"));
        return row;
    }

    private HistoryDO loadAuthorized(String id) {
        HistoryDO row = load(id);
        if (!StpUtil.hasRole("admin")) {
            LoginUser user = UserContext.requireUser();
            Assert.isTrue(StrUtil.equals(row.getUserId(), user.getUserId()),
                    () -> new ClientException("无权访问该记录"));
        }
        return row;
    }

    private HistoryVO toVo(HistoryDO row) {
        return HistoryVO.builder()
                .id(row.getId())
                .userId(row.getUserId())
                .keyword(row.getKeyword())
                .operateType(row.getOperateType())
                .createTime(row.getCreateTime())
                .build();
    }
}
