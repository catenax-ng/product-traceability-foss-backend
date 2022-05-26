package net.catenax.traceability


import io.restassured.builder.RequestSpecBuilder
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

import static io.restassured.RestAssured.given

trait MailhogSupport {

    private static final int HTTP_PORT = 8025
    private static final int SMTP_PORT = 1025

    private static GenericContainer<?> mailhog = new GenericContainer<>("mailhog/mailhog")
            .withExposedPorts(HTTP_PORT, SMTP_PORT)
            .waitingFor(Wait.forHttp("/").forPort(HTTP_PORT))

    private static RequestSpecification mailHogRequest;

    static {
        mailhog.start()
        mailHogRequest = new RequestSpecBuilder()
            .setBasePath("http://${mailhog.getHost()}")
            .setPort(mailhog.getMappedPort(HTTP_PORT))
            .setBasePath("/api/v2")
            .build()
    }

    @DynamicPropertySource
    static void initialize(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.port", () -> mailhog.getMappedPort(SMTP_PORT));
    }

    ValidatableResponse mailbox() {
        given(mailHogRequest).when().get("/messages").then()
    }
}