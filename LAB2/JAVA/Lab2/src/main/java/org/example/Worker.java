package org.example;

public class Worker extends Thread{

    private final ArraySumCalculator calculator;

    public Worker(ArraySumCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public void run() {
        while (calculator.isWorkInProgress()) {
            // Retrieve a task from the queue
            int[] task = calculator.getTaskQueue().poll();
            if (task != null) {
                // Process the task by summing the values
                calculator.sumArrayElements(task[0],task[1]);

                // Increment completed worker count and notify controller
                synchronized (calculator.getLocker()) {
                    calculator.incrementCompletedWorkers();
                    calculator.getLocker().notifyAll();
                }
            } else {
                // If no task is available, wait for a new task
                synchronized (calculator.getLocker()) {
                    try {
                        if (calculator.isWorkInProgress() && calculator.getTaskQueue().isEmpty()) {
                            calculator.getLocker().wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
