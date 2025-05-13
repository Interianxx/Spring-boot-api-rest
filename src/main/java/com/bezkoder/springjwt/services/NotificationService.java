package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.models.Event;
import com.bezkoder.springjwt.models.Notification;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    /**
     * Create a new notification for a user
     * 
     * @param user the user to create the notification for
     * @param message the notification message
     * @param details additional details about the notification
     * @return the created notification
     */
    Notification createNotification(User user, String message, String details);
    
    /**
     * Create a new notification for a user related to an event
     * 
     * @param user the user to create the notification for
     * @param message the notification message
     * @param details additional details about the notification
     * @param event the related event
     * @return the created notification
     */
    Notification createEventNotification(User user, String message, String details, Event event);
    
    /**
     * Get all notifications for a user
     * 
     * @param userId the user ID
     * @return list of notification responses
     */
    List<NotificationResponse> getNotificationsForUser(Long userId);
    
    /**
     * Get unread notifications for a user
     * 
     * @param userId the user ID
     * @return list of unread notification responses
     */
    List<NotificationResponse> getUnreadNotificationsForUser(Long userId);
    
    /**
     * Count unread notifications for a user
     * 
     * @param userId the user ID
     * @return count of unread notifications
     */
    long countUnreadNotificationsForUser(Long userId);
    
    /**
     * Mark a notification as read
     * 
     * @param notificationId the notification ID
     * @param userId the user ID (for security check)
     * @return true if successful, false otherwise
     */
    boolean markNotificationAsRead(Long notificationId, Long userId);
    
    /**
     * Mark all notifications as read for a user
     * 
     * @param userId the user ID
     * @return number of notifications marked as read
     */
    int markAllNotificationsAsRead(Long userId);
    
    /**
     * Delete a notification
     * 
     * @param notificationId the notification ID
     * @param userId the user ID (for security check)
     * @return true if successful, false otherwise
     */
    boolean deleteNotification(Long notificationId, Long userId);
    
    /**
     * Create event reminder notifications for upcoming events
     * This method will be called by the scheduler
     * 
     * @return number of notifications created
     */
    int createEventReminderNotifications();
}