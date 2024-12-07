package com.example.exerciseone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PingWorkerTest extends BaseOutputTest {

    @Test
    public void testPingWorkerPrintsPing() throws InterruptedException {

        // Given a state where pingTurn = true so PingWorker can start immediately
        PingPong.PingPongState testState = new PingPong.PingPongState(TestConfiguration.START_WITH_PING);
        testState.running.set(true);

        // Create the pingWorkerThread
        PingWorker worker = new PingWorker(testState);
        Thread pingWorkerThread = new Thread(worker);

        // Start the worker thread
        pingWorkerThread.start();

        // Give it a short time to run and print
        Thread.sleep(TestConfiguration.EXECUTION_DURATION_MS_SHORT);

        // Stop the worker by setting running to false
        testState.running.set(false);

        // Signal conditions to wake up the worker if it's waiting
        testState.lock.lock();
        try {
            testState.pingCondition.signalAll();
        } finally {
            testState.lock.unlock();
        }

        // Wait for the worker to finish
        pingWorkerThread.join();

        String result = testOut.toString().trim();
        Assertions.assertEquals(
                TestConfiguration.MESSAGE_PING,
                result,
                "PingWorker should print 'ping' once.");
    }
}
