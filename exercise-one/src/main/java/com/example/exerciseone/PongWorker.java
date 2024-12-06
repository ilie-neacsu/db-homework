package com.example.exerciseone;

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

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                state.lock.unlock();
            }
        }
    }
}
