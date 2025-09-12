package com.banklink;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BankLinkBackendApplication.class)
@ActiveProfiles("test")
class BankLinkBackendApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
        // All repository beans, service beans, and configuration should be properly wired
    }

    @Test
    void applicationStartsSuccessfully() {
        // Additional test to ensure the application can start without issues
        // Database connections, JPA repositories, and services should be functional
    }
}
