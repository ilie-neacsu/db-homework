package com.example.exerciseone;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BaseOutputTest {
    protected PrintStream originalOut;
    protected ByteArrayOutputStream testOut;

    @BeforeEach
    public void setUp() {
        originalOut = System.out;
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }
}
