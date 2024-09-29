package org.example;

public class ControllerThread extends Thread{
    private final ArraySumCalculator calculator;

    public ControllerThread(ArraySumCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public void run() {
        calculator.setCurrentLength(calculator.getArray().length);

        while (calculator.getCurrentLength() > 1) {
            int halfLength = calculator.getCurrentLength() / 2;
            calculator.setCompletedWorkers(0);

            // Add tasks to the queue
            for (int i = 0; i < halfLength; i++) {
                int[] task = {i, calculator.getCurrentLength() - 1 - i};
                calculator.getTaskQueue().add(task);
            }

            // Notify workers to start working
            synchronized (calculator.getLocker()) {
                calculator.getLocker().notifyAll();
            }

            // Wait for workers to complete their tasks
            synchronized (calculator.getLocker()) {
                while (calculator.getCompletedWorkers() < halfLength) {
                    try {
                        calculator.getLocker().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            calculator.setCurrentLength(halfLength + (calculator.getCurrentLength() % 2));
        }
    }
}
