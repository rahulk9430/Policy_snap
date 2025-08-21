package com.insurance.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.insurance.model.Notification;
import com.insurance.model.NotificationTemplate;
import com.insurance.repo.NotificationTemplateRepo;

import freemarker.template.Template;
import lombok.RequiredArgsConstructor;

// TemplateService.java
@Service
@RequiredArgsConstructor
public class TemplateService {
    
  private final FreeMarkerConfigurer freeMarkerConfigurer;
  private final NotificationTemplateRepo templateRepo;

  public String render(String templateCode, Map<String, Object> model) throws Exception {
    NotificationTemplate t = templateRepo.findByCode(templateCode)
        .orElseThrow(() -> new IllegalArgumentException("Template not found: "+templateCode));
    Template tpl = new Template(templateCode, new StringReader(t.getBody()), freeMarkerConfigurer.getConfiguration());
    StringWriter out = new StringWriter();
    tpl.process(model, out);
    return out.toString();
  }

  public String subject(String templateCode) {
    return templateRepo.findByCode(templateCode).map(NotificationTemplate::getSubject).orElse("");
  }

  public Notification.Channel channel(String templateCode) {
    return templateRepo.findByCode(templateCode).map(NotificationTemplate::getChannel)
      .orElse(Notification.Channel.IN_APP);
  }
}
