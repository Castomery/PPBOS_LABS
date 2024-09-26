package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;


public class Main {

    private static int[] array;
    private static int numberOfThreads;
    private static final Object lock = new Object();
    private static int currentLength;
    private static int nextIndex = 0;
    private static boolean finished = false;

    public static void main(String[] args) {
        array = new int[]{1, 2, 3, 4, 5, 6}; // Початковий масив
        currentLength = array.length; // Визначення початкової довжини масиву
        numberOfThreads = Runtime.getRuntime().availableProcessors(); // Кількість доступних ядер процесора

        // Створення робочих потоків
        Thread[] workers = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            workers[i] = new Thread(new Worker());
            workers[i].start();
        }

        // Керуючий потік
        Thread managerThread = new Thread(new Manager());
        managerThread.start();

        // Чекаємо завершення всіх робочих потоків
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Завершуємо керуючий потік
        finished = true;
        synchronized (lock) {
            lock.notifyAll();
        }

        // Виводимо фінальний результат
        System.out.println("Сума елементів масиву: " + array[0]);
    }

    static class Manager implements Runnable {
        @Override
        public void run() {
            while (currentLength > 1) {
                synchronized (lock) {
                    nextIndex = 0; // Скидаємо індекс на початок
                    while (nextIndex < currentLength / 2) {
                        lock.notifyAll(); // Підказуємо робочим потокам, що є нові завдання
                        try {
                            lock.wait(); // Чекаємо завершення обробки
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    // Зменшуємо довжину масиву для наступної хвилі
                    currentLength = (currentLength + 1) / 2;
                }
            }
        }
    }

    static class Worker implements Runnable {
        @Override
        public void run() {
            while (!finished || nextIndex < currentLength / 2) {
                int index;
                synchronized (lock) {
                    // Перевіряємо, чи є ще індекси для обробки
                    if (nextIndex < currentLength / 2) {
                        index = nextIndex++;
                    } else {
                        break; // Немає більше завдань, виходимо
                    }
                }

                // Обчислення суми
                int secondIndex = currentLength - 1 - index; // Обчислення симетричного індексу
                int pairSum = array[index] + array[secondIndex];
                array[index] = pairSum; // Зберігаємо результат у нижчий індекс

                synchronized (lock) {
                    lock.notify(); // Підказуємо керуючому потоку, що це завдання завершено
                }
            }
        }
    }
}