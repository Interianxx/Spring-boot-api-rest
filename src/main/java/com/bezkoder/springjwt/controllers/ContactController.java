package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.Category;
import com.bezkoder.springjwt.models.Contact;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.ContactRequest;
import com.bezkoder.springjwt.payload.response.CategoryResponse;
import com.bezkoder.springjwt.payload.response.ContactResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.CategoryRepository;
import com.bezkoder.springjwt.repository.ContactRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/contacts")
@PreAuthorize("isAuthenticated()")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Get all contacts for the current user
    @GetMapping
    public ResponseEntity<List<ContactResponse>> getAllContacts() {
        UserDetailsImpl userDetails = getCurrentUser();
        List<Contact> contacts = contactRepository.findByUserId(userDetails.getId());

        List<ContactResponse> contactResponses = contacts.stream()
                .map(contact -> {
                    // Map categories to CategoryResponse objects
                    Set<CategoryResponse> categoryResponses = contact.getCategories().stream()
                            .map(category -> new CategoryResponse(
                                    category.getId(),
                                    category.getName(),
                                    category.getDescription()))
                            .collect(Collectors.toSet());

                    return new ContactResponse(
                            contact.getId(),
                            contact.getName(),
                            contact.getEmail(),
                            contact.getPhone(),
                            contact.getNotes(),
                            categoryResponses);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(contactResponses);
    }

    // Get a specific contact by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getContactById(@PathVariable Long id) {
        UserDetailsImpl userDetails = getCurrentUser();

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));

        // Check if the contact belongs to the current user
        if (!contact.getUser().getId().equals(userDetails.getId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("Error: You don't have permission to access this contact"));
        }

        // Map categories to CategoryResponse objects
        Set<CategoryResponse> categoryResponses = contact.getCategories().stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getDescription()))
                .collect(Collectors.toSet());

        ContactResponse contactResponse = new ContactResponse(
                contact.getId(),
                contact.getName(),
                contact.getEmail(),
                contact.getPhone(),
                contact.getNotes(),
                categoryResponses);

        return ResponseEntity.ok(contactResponse);
    }

    // Create a new contact
    @PostMapping
    public ResponseEntity<?> createContact(@Valid @RequestBody ContactRequest contactRequest) {
        UserDetailsImpl userDetails = getCurrentUser();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Contact contact = new Contact(
                contactRequest.getName(),
                contactRequest.getEmail(),
                contactRequest.getPhone(),
                contactRequest.getNotes(),
                user);

        // Associate categories with the contact if provided
        Set<CategoryResponse> categoryResponses = new HashSet<>();
        if (contactRequest.getCategoryIds() != null && !contactRequest.getCategoryIds().isEmpty()) {
            contactRequest.getCategoryIds().forEach(categoryId -> {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

                // Check if the category belongs to the current user
                if (!category.getUser().getId().equals(userDetails.getId())) {
                    throw new RuntimeException("You don't have permission to use this category");
                }

                category.addContact(contact);
                categoryResponses.add(new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getDescription()));
            });
        }

        Contact savedContact = contactRepository.save(contact);

        ContactResponse contactResponse = new ContactResponse(
                savedContact.getId(),
                savedContact.getName(),
                savedContact.getEmail(),
                savedContact.getPhone(),
                savedContact.getNotes(),
                categoryResponses);

        return ResponseEntity.status(HttpStatus.CREATED).body(contactResponse);
    }

    // Update an existing contact
    @PutMapping("/{id}")
    public ResponseEntity<?> updateContact(@PathVariable Long id, @Valid @RequestBody ContactRequest contactRequest) {
        UserDetailsImpl userDetails = getCurrentUser();

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));

        // Check if the contact belongs to the current user
        if (!contact.getUser().getId().equals(userDetails.getId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("Error: You don't have permission to update this contact"));
        }

        contact.setName(contactRequest.getName());
        contact.setEmail(contactRequest.getEmail());
        contact.setPhone(contactRequest.getPhone());
        contact.setNotes(contactRequest.getNotes());

        // Update category associations
        Set<CategoryResponse> categoryResponses = new HashSet<>();

        // Clear existing categories
        contact.getCategories().clear();

        // Add new categories if provided
        if (contactRequest.getCategoryIds() != null && !contactRequest.getCategoryIds().isEmpty()) {
            contactRequest.getCategoryIds().forEach(categoryId -> {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

                // Check if the category belongs to the current user
                if (!category.getUser().getId().equals(userDetails.getId())) {
                    throw new RuntimeException("You don't have permission to use this category");
                }

                category.addContact(contact);
                categoryResponses.add(new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getDescription()));
            });
        }

        Contact updatedContact = contactRepository.save(contact);

        ContactResponse contactResponse = new ContactResponse(
                updatedContact.getId(),
                updatedContact.getName(),
                updatedContact.getEmail(),
                updatedContact.getPhone(),
                updatedContact.getNotes(),
                categoryResponses);

        return ResponseEntity.ok(contactResponse);
    }

    // Delete a contact
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        UserDetailsImpl userDetails = getCurrentUser();

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));

        // Check if the contact belongs to the current user
        if (!contact.getUser().getId().equals(userDetails.getId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("Error: You don't have permission to delete this contact"));
        }

        contactRepository.delete(contact);

        return ResponseEntity.ok(new MessageResponse("Contact deleted successfully"));
    }

    // Helper method to get the current authenticated user
    private UserDetailsImpl getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }
}
