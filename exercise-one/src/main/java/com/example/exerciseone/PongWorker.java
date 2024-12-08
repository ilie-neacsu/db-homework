package com.example.exerciseone;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PongWorker implements Runnable {

    private final PingPong.PingPongState state;

    public PongWorker(PingPong.PingPongState state) {
        this.state = state;
    }

    @Override
    public void run() {

        while (state.running.get()) {
            state.lock.lock();

            try {

                while (state.pingTurn && state.running.get()) {
                    state.pongCondition.await();
                }

                if (!state.running.get()) {
                    return;
                }

                System.out.println(Configuration.MESSAGE_PONG);
                state.pingTurn = true;
                state.pingCondition.signalAll();

            } catch (InterruptedException interruptedException) {

                Thread currentThread = Thread.currentThread();
                currentThread.interrupt();
                log.warn("{} interrupted", Configuration.THREAD_NAME_PONG, interruptedException);
                
                // Reset the state and signal the other thread to prevent deadlock
                state.pingTurn = true;
                state.pongCondition.signalAll();

            } catch(Exception exception) {
                log.error("Unexpected exception in {}", Configuration.THREAD_NAME_PONG, exception);
                
                // Stop running and perform cleanup
                state.running.set(false);
                state.pingCondition.signalAll();
                state.pongCondition.signalAll();
                
            } finally {
                state.lock.unlock();
            }
        }

        log.debug("{} stopped", Configuration.THREAD_NAME_PONG);
    }
}
