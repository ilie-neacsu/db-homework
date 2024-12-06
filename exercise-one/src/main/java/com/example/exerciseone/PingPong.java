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

    private Thread pingThread;
    private Thread pongThread;

    public PingPong(boolean startWithPing) {
        this.state = new PingPongState(startWithPing);
    }

    public void start(long executionMillis) {
        state.running.set(true);

        pingThread = new Thread(new PingWorker(state), Configuration.THREAD_NAME_PING);
        pongThread = new Thread(new PongWorker(state), Configuration.THREAD_NAME_PONG);

        pingThread.start();
        pongThread.start();

        try {
            Thread.sleep(executionMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
            if (pingThread != null) {
               pingThread.join();
            }

            if (pongThread != null) {
                pongThread.join();
            }

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
