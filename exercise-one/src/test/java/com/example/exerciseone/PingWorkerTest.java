package com.example.exerciseone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class PingWorkerTest extends BaseOutputTest {

    @Test
    public void pingWorkerShouldPrintsPing() throws InterruptedException {

        // Arrange
        PingPong.PingPongState testState = new PingPong.PingPongState(TestConfiguration.START_WITH_PING);
        testState.running.set(true);
        PingWorker worker = new PingWorker(testState);
        Thread pingWorkerThread = new Thread(worker);

        // Act
        pingWorkerThread.start();
        Thread.sleep(TestConfiguration.EXECUTION_DURATION_MS_SHORT);
        testState.running.set(false);
        testState.lock.lock();
        try {
            testState.pingCondition.signalAll();
        } finally {
            testState.lock.unlock();
        }
        pingWorkerThread.join();

        // Assert
        String result = testOut.toString().trim();
        Assertions.assertEquals(
                TestConfiguration.MESSAGE_PING,
                result,
                "PingWorker should print 'ping' once.");
    }

    @Test
    public void pingWorkerOnInterruptedExceptionShouldResetStateAndSignalTheOtherThread()
            throws InterruptedException {

        // Arrange
        ReentrantLock mockLock = Mockito.spy(new ReentrantLock());
        Condition mockPingCondition = Mockito.mock(Condition.class);
        AtomicBoolean mockRunning = Mockito.mock(AtomicBoolean.class);

        doThrow(new InterruptedException()).when(mockPingCondition).await();

        PingPong.PingPongState testState = Mockito.spy(new PingPong.PingPongState(false));

        testState.pingCondition = mockPingCondition;
        testState.lock = mockLock;
        testState.running = mockRunning;
        Mockito.doReturn(true).doReturn(true).doReturn(false)
                .when(testState.running).get();

        PongWorker worker = Mockito.spy(new PongWorker(testState));
        Thread pingWorkerThread = new Thread(worker);

        // Act
        pingWorkerThread.start();
        pingWorkerThread.join();

        // Assert
        Assertions.assertFalse(pingWorkerThread.isAlive());
        Assertions.assertTrue(testState.pingTurn);
        verify(mockPingCondition).signalAll();
        verify(mockLock).unlock();

    }

    @Test
    public void pingWorkerOnGenericExceptionShouldStopRunningAndPerformCleanup()
            throws InterruptedException {

        // Arrange
        ReentrantLock mockLock = Mockito.spy(new ReentrantLock());
        Condition mockPingCondition = Mockito.mock(Condition.class);
        Condition mockPongCondition = Mockito.mock(Condition.class);
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
