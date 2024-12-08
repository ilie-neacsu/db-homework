package com.example.exerciseone;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PingWorker implements Runnable {

    private final PingPong.PingPongState state;

    public PingWorker(PingPong.PingPongState state) {
        this.state = state;
    }

    @Override
    public void run() {
        while(state.running.get()) {
            state.lock.lock();
            try {

                while (!state.pingTurn && state.running.get()) {
                    state.pingCondition.await();
                }

                if (!state.running.get()) {
                    return;
                }

                System.out.println(Configuration.MESSAGE_PING);
                state.pingTurn = false;
                state.pongCondition.signalAll();

            } catch (InterruptedException interruptedException) {

                Thread.currentThread().interrupt();
                log.warn("{} interrupted", Configuration.THREAD_NAME_PING, interruptedException);

                // Reset the state and signal the other thread to prevent deadlock
                state.pingTurn = false;
                state.pongCondition.signalAll();

            } catch(Exception exception) {

                log.error("Unexpected exception in {}", Configuration.THREAD_NAME_PING, exception);
                // Stop running and perform cleanup
                state.running.set(false);
                state.pingCondition.signalAll();
                state.pongCondition.signalAll();
                
            } finally {
                state.lock.unlock();
            }
        }

        log.debug("{} stopped", Configuration.THREAD_NAME_PING);
    }
}
