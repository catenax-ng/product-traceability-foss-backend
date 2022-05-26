package net.catenax.traceability.clients.email


import net.catenax.traceability.IntegrationSpec
import net.catenax.traceability.MailhogSupport
import org.springframework.beans.factory.annotation.Autowired

import static org.hamcrest.Matchers.equalTo

class EmailServiceIT extends IntegrationSpec implements MailhogSupport {

    @Autowired
    EmailService emailService

    def "should send email"() {
        given:
            String subject = "Test Subject"
            String message = "Test Message"
            String recipient = "test@test.com"

        when:
            emailService.sendMail(recipient, subject, message)

        then:
            mailbox()
                .body("total", equalTo(1))
                .body("items[0].Content.Headers.To[0]", equalTo(recipient))
                .body("items[0].Content.Body", equalTo(message))
                .body("items[0].Content.Headers.Subject[0]", equalTo(subject))
    }

}
