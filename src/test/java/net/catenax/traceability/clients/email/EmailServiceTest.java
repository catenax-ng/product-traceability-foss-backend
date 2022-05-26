package net.catenax.traceability.clients.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void shouldSendEmail() {
        emailService.sendMail("blazej.kepa@gmail.com", "Test subject", "Test content");
    }

}
