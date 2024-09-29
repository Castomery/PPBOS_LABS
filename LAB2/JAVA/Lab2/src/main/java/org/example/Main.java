package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;


public class Main {
    static long[] array = new long[10000000];
    static int workerCount = 8;
    static Thread[] workers;
    static ConcurrentLinkedQueue<int[]> taskQueue;
    static int completedWorkers;
    static final Object locker = new Object();
    static volatile boolean workInProgress = true;
    static int currentLength;

    public static void main(String[] args) {

        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }

        taskQueue = new ConcurrentLinkedQueue<>();
        workers = new Thread[workerCount];

        for (int i = 0; i < workerCount; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }

        Thread controllerThread = new ControllerThread();
        controllerThread.start();

        try {
            controllerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        workInProgress = false;

        synchronized (locker) {
            locker.notifyAll();
        }

        // Join all worker threads
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Final result: " + array[0]);
    }

    static class ControllerThread extends Thread {
        @Override
        public void run() {
            currentLength = array.length;

            while (currentLength > 1) {
                int halfLength = currentLength / 2;
                completedWorkers = 0;

                // Enqueue tasks
                for (int i = 0; i < halfLength; i++) {
                    int pairIndex = currentLength - 1 - i;
                    taskQueue.add(new int[]{i, pairIndex});
                }

                synchronized (locker) {
                    locker.notifyAll();
                }

                synchronized (locker) {
                    while (completedWorkers < halfLength) {
                        try {
                            locker.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                currentLength = halfLength + (currentLength % 2);
            }
        }
    }

    // Worker class to process the tasks
    static class Worker extends Thread {
        @Override
        public void run() {
            while (workInProgress) {
                int[] task = taskQueue.poll();

                if (task != null) {
                    array[task[0]] += array[task[1]];

                    synchronized (locker) {
                        completedWorkers++;
                        locker.notifyAll();
                    }
                } else {
                    synchronized (locker) {
                        try {
                            if (workInProgress && taskQueue.isEmpty()) {
                                locker.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}