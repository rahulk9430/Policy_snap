package com.insurance.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.insurance.dto.UserDTO;
import com.insurance.events.EventPublisher;
import com.insurance.exception.InvalidRoleException;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.model.EventMessage;
import com.insurance.model.User;
import com.insurance.model.UserRole;
import com.insurance.repo.UserRepo;
import com.insurance.service.UserService;
import com.insurance.util.GenericMapper;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepo userRepository;
    private final GenericMapper genericMapper;
    private final EventPublisher eventPublisher;

    public UserServiceImpl(UserRepo userRepository, GenericMapper genericMapper, EventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.genericMapper = genericMapper;
        this.eventPublisher = eventPublisher;
    }
    

    @Override
    public UserDTO createUser(UserDTO userDto) {
       User user = genericMapper.mapToEntity(userDto, User.class);
        user.setPassword(userDto.getPassword()); 
        user.setRole(UserRole.CUSTOMER);
        user.setIsActive(true);
        user.setEmailVerified(false);
        User savedUser = userRepository.save(user);

        // UserService.java
eventPublisher.publishAfterCommit("insurance.user.events",
  EventMessage.builder()
    .eventId(UUID.randomUUID())
    .eventType("USER_REGISTERED")
    .aggregateType("USER")
    .aggregateId(user.getUserId())
    .userId(user.getUserId())
    .occurredAt(LocalDateTime.now())
    .source("user-service")
    .data(Map.of("username", user.getUsername(), "email", user.getEmail()))
    .build()
);

        return genericMapper.mapToDto(savedUser, UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return genericMapper.mapToDtoList(users, UserDTO.class);
    }

    @Override
    public UserDTO getUserById(UUID userId) {
       User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return genericMapper.mapToDto(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(UUID userId, UserDTO userDto) {
   User existing = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Update allowed fields
        existing.setUsername(userDto.getUsername());
        existing.setEmail(userDto.getEmail());
        existing.setPhone(userDto.getPhone());
        existing.setPassword(userDto.getPassword()); // Ensure password is hashed in the repository
        existing.setAddress(userDto.getAddress());
        existing.setGender(userDto.getGender());
        existing.setDateOfBirth(userDto.getDateOfBirth());
        existing.setProfilePicture(userDto.getProfilePicture());
         // ✅ Safely convert string to enum
    try {
        existing.setRole(UserRole.valueOf(userDto.getRole().toUpperCase()));
    } catch (IllegalArgumentException ex) {
        throw new InvalidRoleException("Invalid role provided: " + userDto.getRole());
    }

        User updated = userRepository.save(existing);
        return genericMapper.mapToDto(updated, UserDTO.class);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        user.setIsActive(false);
        userRepository.delete(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
       User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return genericMapper.mapToDto(user, UserDTO.class);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
         User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return genericMapper.mapToDto(user, UserDTO.class);
    }
    
}
