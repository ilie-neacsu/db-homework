package com.example.exerciseone;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PongWorkerTest extends BaseOutputTest {

    @Test
    public void testPongWorkerPrintsPong() throws InterruptedException {
        // Given a state where pingTurn = false so PongWorker can start immediately
        PingPong.PingPongState testState = new PingPong.PingPongState(TestConfiguration.START_WITH_PONG);
        testState.running.set(true);

        // Create the pongWorkerThread
        PongWorker worker = new PongWorker(testState);
        Thread pongWorkerThread = new Thread(worker);

        // Start the worker thread
        pongWorkerThread.start();

        // Give it a short time to run and print
        Thread.sleep(TestConfiguration.EXECUTION_DURATION_MS_SHORT);

        // Stop the worker by setting running to false
        testState.running.set(false);

        // Signal conditions to wake up the worker if it's waiting
        testState.lock.lock();
        try {
            testState.pongCondition.signalAll();
        } finally {
            testState.lock.unlock();
        }

        // Wait for the worker to finish
        pongWorkerThread.join();

        String result = testOut.toString().trim();
        Assertions.assertEquals(
                TestConfiguration.MESSAGE_PONG,
                result,
                "PongWorker should print 'pong' once.");
    }
}
