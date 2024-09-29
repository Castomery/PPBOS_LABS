package org.example;
import java.util.concurrent.*;

public class ArraySumCalculator {
    private long[] array;
    int workerCount;
    private Thread[] workers;
    private ConcurrentLinkedQueue<int[]> taskQueue;
    private int completedWorkers;
    private final Object locker = new Object();
    private boolean workInProgress = true;
    private int currentLength;

    public ArraySumCalculator(int arraySize, int workerCount) {
        this.array = new long[arraySize];
        this.workerCount = workerCount;
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        taskQueue = new ConcurrentLinkedQueue<>();
        workers = new Worker[workerCount];
    }

    public void calculateSum() {

        for (int i = 0; i < workerCount; i++) {
            workers[i] = new Worker(this);
            workers[i].start();
        }

        ControllerThread controllerThread = new ControllerThread(this);
        controllerThread.start();

        try {
            controllerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        workInProgress = false;

        // Notify workers to stop
        synchronized (locker) {
            locker.notifyAll();
        }

        // Wait for all workers to finish
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Print the final result
        System.out.println("Final result: " + array[0]);
    }

    public void sumArrayElements(int index1, int index2) {
        array[index1] += array[index2];
    }

    public long[] getArray() {
        return array;
    }

    public void setArray(long[] array) {
        this.array = array;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }

    public ConcurrentLinkedQueue<int[]> getTaskQueue() {
        return taskQueue;
    }

    public void setTaskQueue(ConcurrentLinkedQueue<int[]> taskQueue) {
        this.taskQueue = taskQueue;
    }

    public int getCompletedWorkers() {
        return completedWorkers;
    }

    public void setCompletedWorkers(int count) {
        this.completedWorkers = count;
    }

    public void incrementCompletedWorkers() {
        this.completedWorkers++;
    }

    public Object getLocker() {
        return locker;
    }

    public boolean isWorkInProgress() {
        return workInProgress;
    }

    public void setWorkInProgress(boolean workInProgress) {
        this.workInProgress = workInProgress;
    }

    public int getCurrentLength() {
        return currentLength;
    }

    public void setCurrentLength(int currentLength) {
        this.currentLength = currentLength;
    }
}