

package com.nageoffer.ai.ragent.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nageoffer.ai.ragent.framework.exception.ClientException;
import com.nageoffer.ai.ragent.knowledge.dao.entity.KnowledgeDocumentDO;
import com.nageoffer.ai.ragent.knowledge.dao.entity.KnowledgeDocumentScheduleDO;
import com.nageoffer.ai.ragent.knowledge.dao.mapper.KnowledgeDocumentScheduleMapper;
import com.nageoffer.ai.ragent.knowledge.enums.SourceType;
import com.nageoffer.ai.ragent.knowledge.schedule.CronScheduleHelper;
import com.nageoffer.ai.ragent.knowledge.service.KnowledgeDocumentScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeDocumentScheduleServiceImpl implements KnowledgeDocumentScheduleService {

    private final KnowledgeDocumentScheduleMapper scheduleMapper;
    @Value("${rag.knowledge.schedule.min-interval-seconds:60}")
    private long scheduleMinIntervalSeconds;

    @Override
    public void upsertSchedule(KnowledgeDocumentDO documentDO) {
        syncSchedule(documentDO, true);
    }

    @Override
    public void syncScheduleIfExists(KnowledgeDocumentDO documentDO) {
        syncSchedule(documentDO, false);
    }

    private void syncSchedule(KnowledgeDocumentDO documentDO, boolean allowCreate) {
        if (documentDO == null) {
            return;
        }
        if (documentDO.getId() == null || documentDO.getKbId() == null) {
            return;
        }
        if (!SourceType.URL.getValue().equalsIgnoreCase(documentDO.getSourceType())) {
            return;
        }
        boolean docEnabled = documentDO.getEnabled() == null || documentDO.getEnabled() == 1;
        String cron = documentDO.getScheduleCron();
        boolean enabled = documentDO.getScheduleEnabled() != null && documentDO.getScheduleEnabled() == 1;
        if (!StringUtils.hasText(cron)) {
            enabled = false;
        }
        if (!docEnabled) {
            enabled = false;
        }

        Date nextRunTime = null;
        if (enabled) {
            try {
                if (CronScheduleHelper.isIntervalLessThan(cron, new Date(), scheduleMinIntervalSeconds)) {
                    throw new ClientException("定时周期不能小于 " + scheduleMinIntervalSeconds + " 秒");
                }
                nextRunTime = CronScheduleHelper.nextRunTime(cron, new Date());
            } catch (IllegalArgumentException e) {
                throw new ClientException("定时表达式不合法");
            }
        }

        KnowledgeDocumentScheduleDO existing = scheduleMapper.selectOne(
                new LambdaQueryWrapper<KnowledgeDocumentScheduleDO>()
                        .eq(KnowledgeDocumentScheduleDO::getDocId, documentDO.getId())
                        .last("LIMIT 1")
        );

        if (existing == null) {
            if (!allowCreate) {
                return;
            }
            KnowledgeDocumentScheduleDO schedule = KnowledgeDocumentScheduleDO.builder()
                    .docId(documentDO.getId())
                    .kbId(documentDO.getKbId())
                    .cronExpr(cron)
                    .enabled(enabled ? 1 : 0)
                    .nextRunTime(nextRunTime)
                    .build();
            scheduleMapper.insert(schedule);
        } else {
            existing.setCronExpr(cron);
            existing.setEnabled(enabled ? 1 : 0);
            existing.setNextRunTime(nextRunTime);
            scheduleMapper.updateById(existing);
        }
    }
}
