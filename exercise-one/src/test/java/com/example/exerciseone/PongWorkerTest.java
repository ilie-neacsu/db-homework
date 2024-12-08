package com.example.exerciseone;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class PongWorkerTest extends BaseOutputTest {

    @Test
    public void pongWorkerShouldPrintPong() throws InterruptedException {

        // Arrange
        PingPong.PingPongState testState = new PingPong.PingPongState(TestConfiguration.START_WITH_PONG);
        testState.running.set(true);
        PongWorker worker = new PongWorker(testState);
        Thread pongWorkerThread = new Thread(worker);

        // Act
        pongWorkerThread.start();
        Thread.sleep(TestConfiguration.EXECUTION_DURATION_MS_SHORT);
        testState.running.set(false);
        testState.lock.lock();
        try {
            testState.pongCondition.signalAll();
        } finally {
            testState.lock.unlock();
        }
        pongWorkerThread.join();

        // Assert
        String result = testOut.toString().trim();
        Assertions.assertEquals(
                TestConfiguration.MESSAGE_PONG,
                result,
                "PongWorker should print 'pong' once.");
    }

    @Test
    public void pongWorkerOnInterruptedExceptionShouldResetStateAndSignalTheOtherThread()
            throws InterruptedException {

        // Arrange
        ReentrantLock mockLock = Mockito.spy(new ReentrantLock());
        Condition mockPongCondition = Mockito.mock(Condition.class);
        AtomicBoolean mockRunning = Mockito.mock(AtomicBoolean.class);

        doThrow(new InterruptedException()).when(mockPongCondition).await();

        PingPong.PingPongState testState = Mockito.spy(new PingPong.PingPongState(true));

        testState.pongCondition = mockPongCondition;
        testState.lock = mockLock;
        testState.running = mockRunning;
        Mockito.doReturn(true).doReturn(true).doReturn(false)
                .when(testState.running).get();

        PongWorker worker = Mockito.spy(new PongWorker(testState));
        Thread pongWorkerThread = new Thread(worker);

        // Act
        pongWorkerThread.start();
        pongWorkerThread.join();

        // Assert
        Assertions.assertFalse(pongWorkerThread.isAlive());
        Assertions.assertTrue(testState.pingTurn);
        verify(mockPongCondition).signalAll();
        verify(mockLock).unlock();

    }

    @Test
    public void pongWorkerOnGenericExceptionShouldStopRunningAndPerformCleanup()
            throws InterruptedException {

        // Arrange
        ReentrantLock mockLock = Mockito.spy(new ReentrantLock());
        Condition mockPongCondition = Mockito.mock(Condition.class);
        Condition mockPingCondition = Mockito.mock(Condition.class);
        AtomicBoolean mockRunning = Mockito.mock(AtomicBoolean.class);

        Mockito.doAnswer(invocation -> {
            throw new Exception("Mocked Exception");
        }).when(mockPongCondition).await();

        PingPong.PingPongState testState = Mockito.spy(new PingPong.PingPongState(true));

        testState.pongCondition = mockPongCondition;
        testState.pingCondition = mockPingCondition;
        testState.lock = mockLock;
        testState.running = mockRunning;
        Mockito.doReturn(true).doReturn(true).doReturn(false)
                .when(testState.running).get();

        PongWorker worker = Mockito.spy(new PongWorker(testState));
        Thread pongWorkerThread = new Thread(worker);

        // Act
        pongWorkerThread.start();
        pongWorkerThread.join();

        // Assert
        verify(mockRunning).set(false);
        verify(mockPingCondition).signalAll();
        verify(mockPongCondition).signalAll();
        verify(mockLock).unlock();

    }
}
