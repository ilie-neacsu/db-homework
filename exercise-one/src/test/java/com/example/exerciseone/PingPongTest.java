package com.example.exerciseone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PingPongTest extends BaseOutputTest {

    @Test
    public void testStartWithPing() {
        try (PingPong pingPong = new PingPong(TestConfiguration.START_WITH_PING)) {
            pingPong.startup(TestConfiguration.EXECUTION_DURATION_MS_LONG);
            String result = testOut.toString().trim();
            Assertions.assertTrue(result.startsWith("ping"), "Should start with ping.");
        }
    }

    @Test
    public void testStartWithPong() {
        try (PingPong pingPong = new PingPong(TestConfiguration.START_WITH_PONG)) {
            pingPong.startup(TestConfiguration.EXECUTION_DURATION_MS_LONG);
            String result = testOut.toString().trim();
            Assertions.assertTrue(result.startsWith("pong"), "Should start with pong.");
        }
    }

    @Test
    public void testPingPongAlternation() {
        try (PingPong ping = new PingPong(true)) {
            ping.startup(TestConfiguration.EXECUTION_DURATION_MS_LONG);
            String result = testOut.toString().trim();

            Assertions.assertFalse(result.isEmpty(), "Output should not be empty.");

            String[] lines = result.split("\\r?\\n");
            for (int i = 0; i < lines.length; i++) {
                if (i % 2 == 0) {
                    Assertions.assertTrue(lines[i].startsWith("ping"), "Should start with ping.");
                } else {
                    Assertions.assertTrue(lines[i].startsWith("pong"), "Should start with pong.");
                }
            }
        }
    }

    @Test
    public void testStopping() {
        try (PingPong pingPong = new PingPong(true)) {
            pingPong.startup(TestConfiguration.EXECUTION_DURATION_MS_SHORT);

        String result = testOut.toString().trim();

        Assertions.assertFalse(
                result.isEmpty(),
                "Output should not be empty after a short run."
        );

        int initialCount = result.split("\\r?\\n").length;

            // Wait some time and ensure no more output is produced after stopping.
            try {
                Thread.sleep(TestConfiguration.WAIT_DURATION_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        String resultAfterWait = testOut.toString().trim();
        int countAfterWait = resultAfterWait.split("\\r?\\n").length;

            Assertions.assertEquals(initialCount, countAfterWait, "No new lines should be added after stopping.");
        }
    }

}
