package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.models.Event;
import com.bezkoder.springjwt.models.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a");

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
            logger.info("Simple email sent to {}", to);
        } catch (Exception e) {
            logger.error("Failed to send simple email to {}: {}", to, e.getMessage());
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            emailSender.send(message);
            logger.info("HTML email sent to {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send HTML email to {}: {}", to, e.getMessage());
        }
    }

    @Override
    public void sendEventReminderEmail(User user, Event event) {
        String to = user.getEmail();
        String subject = "Reminder: " + event.getTitle();
        
        String formattedDate = event.getStartDateTime().format(DATE_FORMATTER);
        String formattedTime = event.getStartDateTime().format(TIME_FORMATTER);
        
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><body>");
        htmlBuilder.append("<h2>Event Reminder</h2>");
        htmlBuilder.append("<p>Hello ").append(user.getUsername()).append(",</p>");
        htmlBuilder.append("<p>This is a reminder for your upcoming event:</p>");
        htmlBuilder.append("<div style='margin: 20px; padding: 15px; border: 1px solid #ddd; border-radius: 5px;'>");
        htmlBuilder.append("<h3>").append(event.getTitle()).append("</h3>");
        
        if (event.getDescription() != null && !event.getDescription().isEmpty()) {
            htmlBuilder.append("<p><strong>Description:</strong> ").append(event.getDescription()).append("</p>");
        }
        
        htmlBuilder.append("<p><strong>Date:</strong> ").append(formattedDate).append("</p>");
        htmlBuilder.append("<p><strong>Time:</strong> ").append(formattedTime).append("</p>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<p>You can view more details and manage your events by logging into your account.</p>");
        htmlBuilder.append("<p>Regards,<br/>Your Agenda App Team</p>");
        htmlBuilder.append("</body></html>");
        
        sendHtmlEmail(to, subject, htmlBuilder.toString());
        logger.info("Event reminder email sent to {} for event: {}", to, event.getTitle());
    }
}