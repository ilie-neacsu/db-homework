package com.example.exercisetwo;

import java.util.HashMap;
import java.util.Map;

class Solution {

    private record DigitPair(int firstDigit, int lastDigit) {}

    public int solution(int[] array) {

        Map<DigitPair, Integer[]> digitMap = new HashMap<>();

        for (int number : array) {

            int lastDigit = getLastDigit(number);
            int firstDigit = getFirstDigit(number);

            DigitPair key = new DigitPair(firstDigit, lastDigit);

            Integer[] topNumbers = digitMap.getOrDefault(key, new Integer[] { Integer.MIN_VALUE, Integer.MIN_VALUE });

            if(number > topNumbers[0]) {
                topNumbers[1] = topNumbers[0];
                topNumbers[0] = number;
            } else if(number > topNumbers[1]) {
                topNumbers[1] = number;
            }

            digitMap.put(key, topNumbers);
        }

        int maxSum = Integer.MIN_VALUE;

        for (Integer[] topNumbers : digitMap.values()) {
            if (topNumbers[1] > Integer.MIN_VALUE) {
                int sum = topNumbers[0] + topNumbers[1];
                if (sum > maxSum) {
                    maxSum = sum;
                }
            }
        }

        return maxSum;
    }

    private int getFirstDigit(int number) {
        number = Math.abs(number);
        while (number >= 10) {
            number /= 10;
        }
        return number;
    }

    private int getLastDigit(int number) {
        return Math.abs(number) % 10;
    }
}

