package com.insurance.service;

import java.util.List;
import java.util.UUID;

import com.insurance.dto.UserDTO;

public interface UserService {


     // ✅ Create new user
    UserDTO createUser(UserDTO userDto);

    // ✅ Get all users
    List<UserDTO> getAllUsers();

    // ✅ Get single user by ID
    UserDTO getUserById(UUID userId);

    // ✅ Update user details
    UserDTO updateUser(UUID userId, UserDTO userDto);

    // ✅ Delete (soft delete) user
    void deleteUser(UUID userId);

    // ✅ Get user by username
    UserDTO getUserByUsername(String username);

    // ✅ Get user by email
    UserDTO getUserByEmail(String email);
    
}
