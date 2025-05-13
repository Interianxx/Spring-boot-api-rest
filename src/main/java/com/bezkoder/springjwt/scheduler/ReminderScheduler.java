package com.bezkoder.springjwt.scheduler;

import com.bezkoder.springjwt.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReminderScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ReminderScheduler.class);

    @Autowired
    private NotificationService notificationService;

    /**
     * Scheduled task to create event reminder notifications
     * Runs daily at 8:00 AM
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void createEventReminders() {
        logger.info("Running scheduled event reminder task");
        int notificationsCreated = notificationService.createEventReminderNotifications();
        logger.info("Scheduled task completed: {} reminder notifications created", notificationsCreated);
    }
}