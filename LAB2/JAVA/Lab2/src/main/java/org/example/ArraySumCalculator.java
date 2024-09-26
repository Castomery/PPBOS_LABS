package org.example;
import java.util.concurrent.*;

public class ArraySumCalculator {
    private final long[] array;
    private final int numOfThreads;
    private final ExecutorService executor;

    public ArraySumCalculator(long[] array, int numOfThreads) {
        this.array = array;
        this.numOfThreads = numOfThreads;
        this.executor = Executors.newFixedThreadPool(numOfThreads);
    }

    public long calculateSum() throws InterruptedException, ExecutionException {
        int length = array.length;

        while (length > 1) {
            int halfLength = length / 2;
            Future<?>[] futures = new Future<?>[halfLength];

            for (int i = 0; i < halfLength; i++) {
                final int idx = i;
                final int symIdx = length - 1 - idx;

                futures[i] = executor.submit(() -> {
                    array[idx] += array[symIdx];
                });
            }

            for (Future<?> future : futures) {
                future.get();
            }

            length = halfLength + (length % 2);  // Скорочуємо довжину масиву
        }

        executor.shutdown();
        return array[0];
    }
}