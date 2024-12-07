package com.example.exerciseone;

public class Main {

    public static void main(String[] args) {
        try (PingPong pingPong = new PingPong(Configuration.START_WITH_PING)) {
            pingPong.start(Configuration.EXECUTION_DURATION_MS);
        }
    }
}
