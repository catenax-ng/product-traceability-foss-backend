package net.catenax.traceability.clients.email

import net.catenax.traceability.IntegrationSpec
import net.catenax.traceability.MailhogSupport
import org.springframework.beans.factory.annotation.Autowired

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
            assertMailbox()
                .hasTotalSize(1)
                .hasRecipient(recipient)
                .hasMessage(message)
                .hasSubject(subject)
    }

}
