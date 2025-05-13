package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.Category;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.CategoryRequest;
import com.bezkoder.springjwt.payload.response.CategoryResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.CategoryRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/categories")
@PreAuthorize("isAuthenticated()")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all categories for the current user
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        UserDetailsImpl userDetails = getCurrentUser();
        List<Category> categories = categoryRepository.findByUserId(userDetails.getId());
        
        List<CategoryResponse> categoryResponses = categories.stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getDescription()))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(categoryResponses);
    }

    // Get a specific category by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        UserDetailsImpl userDetails = getCurrentUser();
        
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        
        // Check if the category belongs to the current user
        if (!category.getUser().getId().equals(userDetails.getId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("Error: You don't have permission to access this category"));
        }
        
        CategoryResponse categoryResponse = new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription());
        
        return ResponseEntity.ok(categoryResponse);
    }

    // Create a new category
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        UserDetailsImpl userDetails = getCurrentUser();
        
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Category category = new Category(
                categoryRequest.getName(),
                categoryRequest.getDescription(),
                user);
        
        Category savedCategory = categoryRepository.save(category);
        
        CategoryResponse categoryResponse = new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getDescription());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }

    // Update an existing category
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest categoryRequest) {
        UserDetailsImpl userDetails = getCurrentUser();
        
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        
        // Check if the category belongs to the current user
        if (!category.getUser().getId().equals(userDetails.getId())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("Error: You don't have permission to update this category"));
        }
        
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        
        Category updatedCategory = categoryRepository.save(category);
        
        CategoryResponse categoryResponse = new CategoryResponse(
                updatedCategory.getId(),
                updatedCategory.getName(),
                updatedCategory.getDescription());
        
        return ResponseEntity.ok(categoryResponse);
    }

    // Delete a category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            UserDetailsImpl userDetails = getCurrentUser();
            
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
            
            // Check if the category belongs to the current user
            if (!category.getUser().getId().equals(userDetails.getId())) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Error: You don't have permission to delete this category"));
            }
            
            categoryRepository.delete(category);
            
            return ResponseEntity.ok(new MessageResponse("Category deleted successfully"));
        } catch (Exception e) {
            System.out.println("Error deleting category: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error deleting category: " + e.getMessage()));
        }
    }

    // Helper method to get the current authenticated user
    private UserDetailsImpl getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }
}