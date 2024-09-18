package org.example;

import org.springframework.util.StopWatch;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static int[] arr;
    static int arrSize= 50000000;
    static int countOfThreads = 5;

    public static void main(String[] args) throws InterruptedException {

        arr = new int[arrSize];

        for (int i = 0; i < arrSize; i++) {
            arr[i] = i;
        }

        Calculator calc = new Calculator(arr);

        System.out.println("One Thread");
        long startTime = System.currentTimeMillis();
        System.out.println("Sum: " + calc.GetSumByThreads(1));
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Time: " + estimatedTime + "ms");

        System.out.println("Multiple Thread");
        startTime = System.currentTimeMillis();
        System.out.println("Sum: " + calc.GetSumByThreads(countOfThreads));
        estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Time: " + estimatedTime + "ms");
    }
}