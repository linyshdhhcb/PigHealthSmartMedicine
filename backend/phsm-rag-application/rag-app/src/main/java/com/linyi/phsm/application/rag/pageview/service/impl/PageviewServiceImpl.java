package com.linyi.phsm.application.rag.pageview.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linyi.phsm.application.rag.pageview.service.PageviewService;
import com.linyi.phsm.domain.rag.model.entity.IllnessDO;
import com.linyi.phsm.domain.rag.model.entity.PageviewDO;
import com.linyi.phsm.domain.rag.model.request.PageviewPageRequest;
import com.linyi.phsm.domain.rag.model.vo.HotIllnessVO;
import com.linyi.phsm.domain.rag.model.vo.PageviewStatisticsVO;
import com.linyi.phsm.domain.rag.model.vo.PageviewVO;
import com.linyi.phsm.framework.context.UserContext;
import com.linyi.phsm.framework.exception.ClientException;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.IllnessMapper;
import com.linyi.phsm.infrastructure.persistence.rag.mapper.PageviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PageviewServiceImpl implements PageviewService {

    private final PageviewMapper pageviewMapper;
    private final IllnessMapper illnessMapper;

    @Override
    public IPage<PageviewVO> pageQuery(PageviewPageRequest requestParam) {
        Page<PageviewDO> page = new Page<>(requestParam.getCurrent(), requestParam.getSize());
        IPage<PageviewDO> result = pageviewMapper.selectPage(
                page,
                Wrappers.lambdaQuery(PageviewDO.class)
                        .eq(PageviewDO::getDeleted, 0)
                        .orderByDesc(PageviewDO::getUpdateTime)
        );
        Set<String> illnessIds = result.getRecords().stream()
                .map(PageviewDO::getIllnessId)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());
        Map<String, String> names = resolveIllnessNames(illnessIds);
        return result.convert(p -> toVo(p, names.get(p.getIllnessId())));
    }

    @Override
    public PageviewVO queryById(String id) {
        PageviewDO row = load(id);
        Map<String, String> names = resolveIllnessNames(Set.of(row.getIllnessId()));
        return toVo(row, names.get(row.getIllnessId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increment(String illnessId) {
        Assert.notBlank(illnessId, () -> new ClientException("疾病不能为空"));
        IllnessDO ill = illnessMapper.selectOne(
                Wrappers.lambdaQuery(IllnessDO.class)
                        .eq(IllnessDO::getId, illnessId)
                        .eq(IllnessDO::getDeleted, 0)
        );
        Assert.notNull(ill, () -> new ClientException("疾病不存在"));
        String actor = UserContext.getUsername() != null ? UserContext.getUsername() : "anonymous";
        PageviewDO latest = pageviewMapper.selectOne(
                Wrappers.lambdaQuery(PageviewDO.class)
                        .eq(PageviewDO::getIllnessId, illnessId)
                        .eq(PageviewDO::getDeleted, 0)
                        .orderByDesc(PageviewDO::getUpdateTime)
                        .last("LIMIT 1")
        );
        if (latest != null) {
            int pv = latest.getPageviews() != null ? latest.getPageviews() : 0;
            latest.setPageviews(pv + 1);
            latest.setUpdatedBy(actor);
            pageviewMapper.updateById(latest);
            return;
        }
        PageviewDO row = PageviewDO.builder()
                .illnessId(illnessId)
                .pageviews(1)
                .createdBy(actor)
                .updatedBy(actor)
                .build();
        pageviewMapper.insert(row);
    }

    @Override
    public List<HotIllnessVO> hotIllnesses(int limit) {
        int safe = Math.min(Math.max(limit, 1), 100);
        List<Map<String, Object>> rows = pageviewMapper.selectHotIllnessAggregated(safe);
        return rows.stream().map(this::mapHotRow).collect(Collectors.toList());
    }

    @Override
    public PageviewStatisticsVO statistics() {
        List<PageviewDO> all = pageviewMapper.selectList(
                Wrappers.lambdaQuery(PageviewDO.class).eq(PageviewDO::getDeleted, 0)
        );
        long total = all.stream()
                .mapToInt(p -> p.getPageviews() != null ? p.getPageviews() : 0)
                .asLongStream()
                .sum();
        Date start = startOfToday();
        long today = pageviewMapper.selectList(
                        Wrappers.lambdaQuery(PageviewDO.class)
                                .eq(PageviewDO::getDeleted, 0)
                                .ge(PageviewDO::getUpdateTime, start)
                )
                .stream()
                .mapToInt(p -> p.getPageviews() != null ? p.getPageviews() : 0)
                .asLongStream()
                .sum();
        List<HotIllnessVO> top = hotIllnesses(10);
        return PageviewStatisticsVO.builder()
                .totalPageviews(total)
                .todayPageviews(today)
                .topIllnesses(top)
                .build();
    }

    private Date startOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private HotIllnessVO mapHotRow(Map<String, Object> row) {
        Object id = row.get("illness_id");
        Object name = row.get("illness_name");
        Object pv = row.get("pageviews");
        return HotIllnessVO.builder()
                .illnessId(id != null ? String.valueOf(id) : null)
                .illnessName(name != null ? String.valueOf(name) : "")
                .pageviews(pv instanceof Number ? ((Number) pv).longValue() : 0L)
                .build();
    }

    private PageviewDO load(String id) {
        PageviewDO row = pageviewMapper.selectOne(
                Wrappers.lambdaQuery(PageviewDO.class)
                        .eq(PageviewDO::getId, id)
                        .eq(PageviewDO::getDeleted, 0)
        );
        Assert.notNull(row, () -> new ClientException("记录不存在"));
        return row;
    }

    private Map<String, String> resolveIllnessNames(Set<String> ids) {
        if (ids.isEmpty()) {
            return Map.of();
        }
        List<IllnessDO> list = illnessMapper.selectList(
                Wrappers.lambdaQuery(IllnessDO.class)
                        .in(IllnessDO::getId, ids)
                        .eq(IllnessDO::getDeleted, 0)
        );
        Map<String, String> map = new HashMap<>();
        for (IllnessDO i : list) {
            map.put(i.getId(), i.getIllnessName());
        }
        return map;
    }

    private PageviewVO toVo(PageviewDO row, String illnessName) {
        return PageviewVO.builder()
                .id(row.getId())
                .pageviews(row.getPageviews())
                .illnessId(row.getIllnessId())
                .illnessName(illnessName)
                .updateTime(row.getUpdateTime())
                .build();
    }
}
