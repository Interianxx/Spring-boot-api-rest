package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.Event;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.EventRequest;
import com.bezkoder.springjwt.payload.response.EventResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.EventRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/events")
@PreAuthorize("isAuthenticated()")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all events for the current user
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        UserDetailsImpl userDetails = getCurrentUser();
        List<Event> events = eventRepository.findByUserId(userDetails.getId());

        List<EventResponse> eventResponses = events.stream()
                .map(event -> new EventResponse(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getStartDateTime(),
                        event.getEndDateTime()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(eventResponses);
    }

    // Get events by date range (for calendar view)
    @GetMapping("/range")
    public ResponseEntity<List<EventResponse>> getEventsByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        UserDetailsImpl userDetails = getCurrentUser();
        List<Event> events = eventRepository.findByUserIdAndStartDateTimeBetween(userDetails.getId(), start, end);

        List<EventResponse> eventResponses = events.stream()
                .map(event -> new EventResponse(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getStartDateTime(),
                        event.getEndDateTime()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(eventResponses);
    }

    // Get events in FullCalendar format
    @GetMapping("/calendar")
    public ResponseEntity<List<Map<String, Object>>> getEventsForCalendar(
            @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        UserDetailsImpl userDetails = getCurrentUser();
        List<Event> events;

        if (start != null && end != null) {
            events = eventRepository.findByUserIdAndStartDateTimeBetween(userDetails.getId(), start, end);
        } else {
            events = eventRepository.findByUserId(userDetails.getId());
        }

        List<Map<String, Object>> calendarEvents = events.stream()
                .map(event -> {
                    Map<String, Object> calendarEvent = new HashMap<>();
                    calendarEvent.put("id", event.getId());
                    calendarEvent.put("title", event.getTitle());
                    calendarEvent.put("start", event.getStartDateTime());
                    if (event.getEndDateTime() != null) {
                        calendarEvent.put("end", event.getEndDateTime());
                    }
                    calendarEvent.put("description", event.getDescription());
                    return calendarEvent;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(calendarEvents);
    }

    // Get a specific event by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        UserDetailsImpl userDetails = getCurrentUser();

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        // Check if the event belongs to the current user
        if (!event.getUser().getId().equals(userDetails.getId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("Error: You don't have permission to access this event"));
        }

        EventResponse eventResponse = new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getStartDateTime(),
                event.getEndDateTime());

        return ResponseEntity.ok(eventResponse);
    }

    // Create a new event
    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventRequest eventRequest) {
        try {
            UserDetailsImpl userDetails = getCurrentUser();

            User user = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Event event = new Event(
                    eventRequest.getTitle(),
                    eventRequest.getDescription(),
                    eventRequest.getStartDateTime(),
                    eventRequest.getEndDateTime(),
                    user);

            Event savedEvent = eventRepository.save(event);

            EventResponse eventResponse = new EventResponse(
                    savedEvent.getId(),
                    savedEvent.getTitle(),
                    savedEvent.getDescription(),
                    savedEvent.getStartDateTime(),
                    savedEvent.getEndDateTime());

            return ResponseEntity.status(HttpStatus.CREATED).body(eventResponse);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error creating event: " + e.getMessage()));
        }
    }

    // Update an existing event
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequest eventRequest) {
        try {
            UserDetailsImpl userDetails = getCurrentUser();

            Event event = eventRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

            // Check if the event belongs to the current user
            if (!event.getUser().getId().equals(userDetails.getId())) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Error: You don't have permission to update this event"));
            }

            event.setTitle(eventRequest.getTitle());
            event.setDescription(eventRequest.getDescription());
            event.setStartDateTime(eventRequest.getStartDateTime());
            event.setEndDateTime(eventRequest.getEndDateTime());

            Event updatedEvent = eventRepository.save(event);

            EventResponse eventResponse = new EventResponse(
                    updatedEvent.getId(),
                    updatedEvent.getTitle(),
                    updatedEvent.getDescription(),
                    updatedEvent.getStartDateTime(),
                    updatedEvent.getEndDateTime());

            return ResponseEntity.ok(eventResponse);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error updating event: " + e.getMessage()));
        }
    }

    // Delete an event
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        try {
            UserDetailsImpl userDetails = getCurrentUser();

            Event event = eventRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

            // Check if the event belongs to the current user
            if (!event.getUser().getId().equals(userDetails.getId())) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Error: You don't have permission to delete this event"));
            }

            eventRepository.delete(event);

            return ResponseEntity.ok(new MessageResponse("Event deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error deleting event: " + e.getMessage()));
        }
    }

    // Helper method to get the current authenticated user
    private UserDetailsImpl getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }
}
