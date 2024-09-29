package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;


public class Main {
    static int arrayLength = 5000000;
    static int workerCount = 8;

    public static void main(String[] args) {

        ArraySumCalculator calculator = new ArraySumCalculator(arrayLength, workerCount);
        calculator.calculateSum();
    }
}