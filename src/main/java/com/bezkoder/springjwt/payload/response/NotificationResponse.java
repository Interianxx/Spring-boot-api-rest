package com.bezkoder.springjwt.payload.response;

import java.time.LocalDateTime;

public class NotificationResponse {
    private Long id;
    private String message;
    private String details;
    private boolean read;
    private LocalDateTime createdAt;
    private Long eventId;
    private String eventTitle;

    public NotificationResponse(Long id, String message, String details, boolean read, LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.details = details;
        this.read = read;
        this.createdAt = createdAt;
    }

    public NotificationResponse(Long id, String message, String details, boolean read, LocalDateTime createdAt, Long eventId, String eventTitle) {
        this.id = id;
        this.message = message;
        this.details = details;
        this.read = read;
        this.createdAt = createdAt;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
}