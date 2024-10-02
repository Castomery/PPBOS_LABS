using System;
using System.Collections.Concurrent;
using System.ComponentModel.Design;
using System.Diagnostics;

namespace LAB2_PPBOS
{
    internal class Program
    {
        static void Main()
        {
            int size = 50000000;
            long[] array = new long[size];

            for (int i = 0; i < size; i++)
            {
                array[i] = i;
            }

            Stopwatch stopwatch = new Stopwatch();
            

            int workerCount = 4;

            Calculator calculator = new Calculator();

            Worker[] workers = new Worker[workerCount];
            Thread[] workersThreads = new Thread[workerCount];

            for (int i = 0; i < workerCount; i++)
            {
                workers[i] = new Worker(array, calculator, i);
                workersThreads[i] = new Thread(new ThreadStart(workers[i].DoWork));
                workersThreads[i].Start();
            }

             calculator.SetWorkers(workers);
            stopwatch.Start();
            calculator.GetSum(array);
            stopwatch.Stop();
            Console.WriteLine($"Сума всіх елементів масиву = {array[0]}");
            Console.WriteLine(stopwatch.ElapsedMilliseconds);
        }
    }
}