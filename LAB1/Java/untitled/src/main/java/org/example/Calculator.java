package org.example;

public class Calculator {

    private int[] arr;

    public Calculator(int[] arr) {
        this.arr = arr;
    }

    public long GetSumByThreads(int countOfThreads) throws InterruptedException {
        long totalSum = 0;
        long[] partialSum = new long[countOfThreads];
        int arrSize = arr.length;
        int chunkSize = arrSize / countOfThreads;

        Thread[] threads = new Thread[countOfThreads];

        for (int i = 0; i < countOfThreads; i++) {
            int start = i * chunkSize;
            int end = (i==countOfThreads-1)?arrSize: start+chunkSize;
            int threadIndex = i;

            threads[i] = new Thread(() -> {partialSum[threadIndex] = SumArrayPart(start,end);});
            threads[i].start();
        }

        for (int i = 0; i < countOfThreads; i++) {
            threads[i].join();
            totalSum += partialSum[i];
        }

        return totalSum;
    }

    private long SumArrayPart(int start, int end) {
        long localSum = 0;

        for (int i = start; i < end; i++) {
            localSum += arr[i];
        }

        return localSum;
    }
}
