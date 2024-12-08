package com.example.exerciseone;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PingPong implements AutoCloseable {

    public static class PingPongState {
        
        ReentrantLock lock = new ReentrantLock();
        Condition pingCondition = lock.newCondition();
        Condition pongCondition = lock.newCondition();
        AtomicBoolean running = new AtomicBoolean(false);
        boolean pingTurn;

        public PingPongState(boolean startWithPing) {
            this.pingTurn = startWithPing;
        }

    }

    private final PingPongState state;

    private final Thread pingThread;
    private final Thread pongThread;

    public PingPong(boolean startWithPing) {
        this.state = new PingPongState(startWithPing);
        this.pingThread = new Thread(new PingWorker(state), Configuration.THREAD_NAME_PING);
        this.pongThread = new Thread(new PongWorker(state), Configuration.THREAD_NAME_PONG);
    }

    public void startup(long executionMillis) {
        state.running.set(true);
        log.debug("Starting ping-pong threads for {} ms", executionMillis);
        pingThread.start();
        pongThread.start();

        try {
            Thread.sleep(executionMillis);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            log.warn("PingPong start interrupted", interruptedException);
            // Ensure cleanup of ping-pong threads when interrupted
            shutdown();
            return;
        }

        shutdown();
    }

    public void shutdown() {
        log.debug("Initiating shutdown of ping-pong threads");

        state.running.set(false);
        state.lock.lock();

        try {
            state.pingCondition.signalAll();
            state.pongCondition.signalAll();
        } finally {
            state.lock.unlock();
        }

        try {
            pingThread.join();
            pongThread.join();
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while stopping PingPong threads", interruptedException);
            // Force interrupt of ping-pong threads to ensure they stop
            pingThread.interrupt();
            pongThread.interrupt();
        }
    }

    @Override
    public void close() {
        shutdown();
    }
}
