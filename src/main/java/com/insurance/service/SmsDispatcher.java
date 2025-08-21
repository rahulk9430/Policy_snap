package com.insurance.service;

import org.springframework.stereotype.Service;

// SmsDispatcher.java (mock or integrate Twilio/etc)
@Service
public class SmsDispatcher {
  public void send(String phone, String text) {
    // call provider API here (Twilio/msg91/any)
    System.out.println("SMS-> " + phone + " : " + text);
  }
}

