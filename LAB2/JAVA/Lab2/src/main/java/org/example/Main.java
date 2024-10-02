package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;


public class Main {

    public static void main(String[] args) {

        int size = 500000000;
        long[] array = new long[size];

        for (int i = 0; i < size; i++)
        {
            array[i] = i;
        }

        int workerCount = 4;

        ArraySumCalculator calculator = new ArraySumCalculator();

        Worker[] workers = new Worker[workerCount];

        for (int i = 0; i < workerCount; i++)
        {
            workers[i] = new Worker(array, calculator, i);
            workers[i].start();
        }

        calculator.setWorkers(workers);
        long startTime = System.currentTimeMillis();
        calculator.getSum(array);
        System.out.println("Сума всіх елементів масиву = " +  array[0]);
        long time = System.currentTimeMillis() - startTime;
        System.out.println("Час: " + time);
    }
}