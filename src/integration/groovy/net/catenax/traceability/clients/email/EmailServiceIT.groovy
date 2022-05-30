package net.catenax.traceability.clients.email

import com.icegreen.greenmail.spring.GreenMailBean
import net.catenax.traceability.IntegrationSpec
import net.catenax.traceability.MailboxSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

class EmailServiceIT extends IntegrationSpec implements MailboxSupport {

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

    @TestConfiguration
    static class EmailServiceConfig {

        @Bean
        GreenMailBean greenMailBean() {
            GreenMailBean mailBean = new GreenMailBean()
            mailBean.setUsers(["notifications:password@catena-x.net"])
            mailBean
        }
    }
}
