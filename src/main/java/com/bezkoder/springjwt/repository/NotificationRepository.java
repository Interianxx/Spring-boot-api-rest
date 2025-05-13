package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Find all notifications for a specific user
    List<Notification> findByUserId(Long userId);
    
    // Find all unread notifications for a specific user
    List<Notification> findByUserIdAndReadFalse(Long userId);
    
    // Count unread notifications for a specific user
    long countByUserIdAndReadFalse(Long userId);
    
    // Find notifications related to a specific event
    List<Notification> findByEventId(Long eventId);
}