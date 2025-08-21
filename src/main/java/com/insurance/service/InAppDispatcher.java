package com.insurance.service;

import org.springframework.stereotype.Service;

import com.insurance.model.Notification;
import com.insurance.repo.NotificationRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InAppDispatcher {
  private final NotificationRepo repo;
  public void persist(Notification n) { repo.save(n); }
}