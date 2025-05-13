package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.models.Event;
import com.bezkoder.springjwt.models.Notification;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.response.NotificationResponse;
import com.bezkoder.springjwt.repository.EventRepository;
import com.bezkoder.springjwt.repository.NotificationRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a");

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EmailService emailService;

    @Value("${app.reminder.days-before:1}")
    private int reminderDaysBefore;

    @Value("${app.reminder.enabled:true}")
    private boolean reminderEnabled;

    @Override
    @Transactional
    public Notification createNotification(User user, String message, String details) {
        Notification notification = new Notification(message, details, user);
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public Notification createEventNotification(User user, String message, String details, Event event) {
        Notification notification = new Notification(message, details, user, event);
        return notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponse> getNotificationsForUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return mapToNotificationResponses(notifications);
    }

    @Override
    public List<NotificationResponse> getUnreadNotificationsForUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndReadFalse(userId);
        return mapToNotificationResponses(notifications);
    }

    @Override
    public long countUnreadNotificationsForUser(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    @Override
    @Transactional
    public boolean markNotificationAsRead(Long notificationId, Long userId) {
        try {
            Notification notification = notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

            // Security check: ensure the notification belongs to the user
            if (!notification.getUser().getId().equals(userId)) {
                logger.warn("User {} attempted to mark notification {} as read but does not own it", userId, notificationId);
                return false;
            }

            notification.setRead(true);
            notificationRepository.save(notification);
            return true;
        } catch (Exception e) {
            logger.error("Error marking notification as read: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public int markAllNotificationsAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadFalse(userId);

        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }

        return unreadNotifications.size();
    }

    @Override
    @Transactional
    public boolean deleteNotification(Long notificationId, Long userId) {
        try {
            Notification notification = notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

            // Security check: ensure the notification belongs to the user
            if (!notification.getUser().getId().equals(userId)) {
                logger.warn("User {} attempted to delete notification {} but does not own it", userId, notificationId);
                return false;
            }

            notificationRepository.delete(notification);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting notification: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public int createEventReminderNotifications() {
        if (!reminderEnabled) {
            logger.info("Event reminders are disabled");
            return 0;
        }

        int notificationsCreated = 0;

        // Calculate the date range for upcoming events
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderDate = now.plusDays(reminderDaysBefore);
        LocalDateTime endOfReminderDay = reminderDate.toLocalDate().atTime(23, 59, 59);

        // Find all events that start on the reminder date
        List<Event> upcomingEvents = eventRepository.findAll().stream()
                .filter(event -> {
                    LocalDateTime eventStart = event.getStartDateTime();
                    return eventStart != null && 
                           eventStart.isAfter(reminderDate.toLocalDate().atStartOfDay()) && 
                           eventStart.isBefore(endOfReminderDay);
                })
                .collect(Collectors.toList());

        for (Event event : upcomingEvents) {
            User user = event.getUser();

            // Format date and time for the notification
            String formattedDate = event.getStartDateTime().format(DATE_FORMATTER);
            String formattedTime = event.getStartDateTime().format(TIME_FORMATTER);

            // Create notification message
            String message = "Reminder: " + event.getTitle();
            String details = "You have an event scheduled for " + formattedDate + " at " + formattedTime;

            // Create notification
            createEventNotification(user, message, details, event);

            // Send email reminder if user has an email
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                try {
                    emailService.sendEventReminderEmail(user, event);
                } catch (Exception e) {
                    logger.error("Failed to send email reminder to {}: {}", user.getEmail(), e.getMessage());
                }
            }

            notificationsCreated++;
        }

        logger.info("Created {} event reminder notifications", notificationsCreated);
        return notificationsCreated;
    }

    // Helper method to map Notification entities to NotificationResponse DTOs
    private List<NotificationResponse> mapToNotificationResponses(List<Notification> notifications) {
        return notifications.stream()
                .map(notification -> {
                    if (notification.getEvent() != null) {
                        return new NotificationResponse(
                                notification.getId(),
                                notification.getMessage(),
                                notification.getDetails(),
                                notification.isRead(),
                                notification.getCreatedAt(),
                                notification.getEvent().getId(),
                                notification.getEvent().getTitle()
                        );
                    } else {
                        return new NotificationResponse(
                                notification.getId(),
                                notification.getMessage(),
                                notification.getDetails(),
                                notification.isRead(),
                                notification.getCreatedAt()
                        );
                    }
                })
                .collect(Collectors.toList());
    }
}
