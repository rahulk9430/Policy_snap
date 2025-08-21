package com.insurance.controller;

import com.insurance.dto.UserDTO;
import com.insurance.model.ApiResponse;
import com.insurance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Create new user
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserDTO userDto) {
        UserDTO created = userService.createUser(userDto);
        return ResponseEntity.ok(
                new ApiResponse<>("User created successfully", created, true)
        );
    }

    // ✅ Get all users
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(
                new ApiResponse<>("Fetched all users", users, true)
        );
    }

    // ✅ Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable UUID id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(
                new ApiResponse<>("User found", user, true)
        );
    }

    // ✅ Get user by username
    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByUsername(@PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(
                new ApiResponse<>("User found", user, true)
        );
    }

    // ✅ Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(
                new ApiResponse<>("User found", user, true)
        );
    }

    // ✅ Update user
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable UUID id,
                                                           @Valid @RequestBody UserDTO dto) {
        UserDTO updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>("User updated successfully", updated, true)
        );
    }

    // ✅ Soft delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(
                new ApiResponse<>("User deleted (soft) successfully", null, true)
        );
    }
}
