package com.example.exerciseone;

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

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                state.lock.unlock();
            }
        }
    }
}
