package com.yehor.notification.service.email.sender;

import com.yehor.notification.service.email.entity.Email;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class EmailSender {
    @Value("${email.api.key}")
    private String apiKey;

    public void send(Email email) {
        // Logic to send email using apiKey
    }
}
