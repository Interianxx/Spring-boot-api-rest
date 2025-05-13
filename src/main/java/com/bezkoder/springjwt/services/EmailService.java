package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.models.Event;
import com.bezkoder.springjwt.models.User;

public interface EmailService {
    /**
     * Send a simple text email
     * 
     * @param to recipient email address
     * @param subject email subject
     * @param text email body text
     */
    void sendSimpleEmail(String to, String subject, String text);
    
    /**
     * Send an HTML email
     * 
     * @param to recipient email address
     * @param subject email subject
     * @param htmlContent email body as HTML
     */
    void sendHtmlEmail(String to, String subject, String htmlContent);
    
    /**
     * Send an event reminder email
     * 
     * @param user the user to send the reminder to
     * @param event the event to remind about
     */
    void sendEventReminderEmail(User user, Event event);
}