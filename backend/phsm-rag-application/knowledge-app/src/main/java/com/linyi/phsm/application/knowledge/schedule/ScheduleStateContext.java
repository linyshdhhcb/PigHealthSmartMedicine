package com.linyi.phsm.application.knowledge.schedule;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ScheduleStateContext {

    private final String scheduleId;
    private final String execId;
    private final String cronExpr;
    private final Date startTime;
    private final Date nextRunTime;
}
