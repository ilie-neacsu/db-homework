package com.example.exerciseone;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PingPong {

    public static class PingPongState {
        final ReentrantLock lock = new ReentrantLock();
        final Condition pingCondition = lock.newCondition();
        final Condition pongCondition = lock.newCondition();
        final AtomicBoolean running = new AtomicBoolean(false);
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

    public void start(long executionMillis) {
        
        state.running.set(true);
        pingThread.start();
        pongThread.start();

        try {
            Thread.sleep(executionMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Ensure cleanup of ping-pong threads when interrupted
            stop();
            return;
        }

        stop();
    }

    public void stop() {

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
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            // Force interrupt of ping-pong threads to ensure they stop
            pingThread.interrupt();
            pongThread.interrupt();
            return;
        }
    }
}
