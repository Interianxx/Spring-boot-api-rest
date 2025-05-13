package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.Event;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.payload.response.NotificationResponse;
import com.bezkoder.springjwt.repository.EventRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import com.bezkoder.springjwt.services.EmailService;
import com.bezkoder.springjwt.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/notifications")
@PreAuthorize("isAuthenticated()")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    /**
     * Get all notifications for the current user
     */
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        UserDetailsImpl userDetails = getCurrentUser();
        List<NotificationResponse> notifications = notificationService.getNotificationsForUser(userDetails.getId());
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get unread notifications for the current user
     */
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications() {
        UserDetailsImpl userDetails = getCurrentUser();
        List<NotificationResponse> notifications = notificationService.getUnreadNotificationsForUser(userDetails.getId());
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get count of unread notifications for the current user
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getUnreadNotificationCount() {
        UserDetailsImpl userDetails = getCurrentUser();
        long count = notificationService.countUnreadNotificationsForUser(userDetails.getId());
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * Mark a notification as read
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long id) {
        UserDetailsImpl userDetails = getCurrentUser();
        boolean success = notificationService.markNotificationAsRead(id, userDetails.getId());

        if (success) {
            return ResponseEntity.ok(new MessageResponse("Notification marked as read"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to mark notification as read"));
        }
    }

    /**
     * Mark all notifications as read for the current user
     */
    @PutMapping("/read-all")
    public ResponseEntity<?> markAllNotificationsAsRead() {
        UserDetailsImpl userDetails = getCurrentUser();
        int count = notificationService.markAllNotificationsAsRead(userDetails.getId());
        return ResponseEntity.ok(new MessageResponse(count + " notifications marked as read"));
    }

    /**
     * Delete a notification
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        UserDetailsImpl userDetails = getCurrentUser();
        boolean success = notificationService.deleteNotification(id, userDetails.getId());

        if (success) {
            return ResponseEntity.ok(new MessageResponse("Notification deleted successfully"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to delete notification"));
        }
    }

    /**
     * Helper method to get the current authenticated user
     */
    private UserDetailsImpl getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    /**
     * Send a test email to the current user
     */
    @PostMapping("/send-test-email")
    public ResponseEntity<?> sendTestEmail() {
        try {
            UserDetailsImpl userDetails = getCurrentUser();
            User user = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String to = user.getEmail();
            if (to == null || to.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("User does not have an email address"));
            }

            String subject = "Test Email from Your Agenda App";
            String text = "Hello " + user.getUsername() + ",\n\n" +
                    "This is a test email from your Agenda App to verify that email notifications are working correctly.\n\n" +
                    "If you received this email, it means that the email notification system is configured correctly.\n\n" +
                    "Regards,\nYour Agenda App Team";

            emailService.sendSimpleEmail(to, subject, text);
            logger.info("Test email sent to {}", to);

            return ResponseEntity.ok(new MessageResponse("Test email sent to " + to));
        } catch (Exception e) {
            logger.error("Failed to send test email: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to send test email: " + e.getMessage()));
        }
    }

    /**
     * Send an email reminder for a specific event
     */
    @PostMapping("/send-event-reminder/{eventId}")
    public ResponseEntity<?> sendEventReminder(@PathVariable Long eventId) {
        try {
            UserDetailsImpl userDetails = getCurrentUser();
            User user = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

            // Check if the event belongs to the current user
            if (!event.getUser().getId().equals(userDetails.getId())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: You don't have permission to send a reminder for this event"));
            }

            // Send email reminder
            emailService.sendEventReminderEmail(user, event);
            logger.info("Event reminder email sent to {} for event: {}", user.getEmail(), event.getTitle());

            // Create notification
            String message = "Reminder: " + event.getTitle();
            String details = "You requested a reminder for this event.";
            notificationService.createEventNotification(user, message, details, event);

            return ResponseEntity.ok(new MessageResponse("Event reminder sent for: " + event.getTitle()));
        } catch (Exception e) {
            logger.error("Failed to send event reminder: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to send event reminder: " + e.getMessage()));
        }
    }
}
