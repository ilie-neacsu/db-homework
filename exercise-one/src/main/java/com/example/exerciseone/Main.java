package com.example.exerciseone;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        try (PingPong pingPong = new PingPong(Configuration.START_WITH_PING)) {
            pingPong.startup(Configuration.EXECUTION_DURATION_MS);
        } catch(Exception ex) {
            log.error("Exception occurred in main", ex);
        }
    }
}
