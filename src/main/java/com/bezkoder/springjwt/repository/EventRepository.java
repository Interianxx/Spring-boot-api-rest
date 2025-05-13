package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUserId(Long userId);
    
    // Find events between start and end dates (for calendar view)
    List<Event> findByUserIdAndStartDateTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);
}