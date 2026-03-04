

package com.nageoffer.ai.ragent.knowledge.schedule;

import org.springframework.scheduling.support.CronExpression;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Cron 工具类
 */
public final class CronScheduleHelper {

    private CronScheduleHelper() {
    }

    public static Date nextRunTime(String cron, Date from) {
        if (!StringUtils.hasText(cron) || from == null) {
            return null;
        }
        CronExpression expression = CronExpression.parse(cron.trim());
        LocalDateTime fromTime = LocalDateTime.ofInstant(from.toInstant(), ZoneId.systemDefault());
        LocalDateTime next = expression.next(fromTime);
        if (next == null) {
            return null;
        }
        return Date.from(next.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static boolean isIntervalLessThan(String cron, Date from, long minSeconds) {
        if (!StringUtils.hasText(cron) || from == null) {
            return true;
        }
        CronExpression expression = CronExpression.parse(cron.trim());
        LocalDateTime fromTime = LocalDateTime.ofInstant(from.toInstant(), ZoneId.systemDefault());
        LocalDateTime first = expression.next(fromTime);
        if (first == null) {
            return true;
        }
        LocalDateTime second = expression.next(first);
        if (second == null) {
            return true;
        }
        long diffSeconds = Duration.between(first, second).getSeconds();
        return diffSeconds < minSeconds;
    }
}
