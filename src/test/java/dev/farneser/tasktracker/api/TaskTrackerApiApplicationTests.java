package dev.farneser.tasktracker.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@Profile("test")
@SpringBootTest
class TaskTrackerApiApplicationTests {
    @Test
    public void testContextLoads() {
        // hapi hapi hapi
        assert true;
    }
}
