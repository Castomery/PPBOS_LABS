using System;
using System.Collections.Concurrent;
using System.Threading;
using System.Threading.Tasks;


namespace LAB2_PPBOS
{
    internal class Program
    {
        static long[] array = new long[1000000];
        static int workerCount = Environment.ProcessorCount;
        static Thread[] workers;
        static ConcurrentQueue<(int, int)> taskQueue;
        static int completedWorkers;
        static object locker = new object();
        static bool workInProgress = true;
        static int currentLength;

        static void Main()
        {

            for (int i = 0; i < array.Length; i++)
            {
                array[i] = i;
            }

            taskQueue = new ConcurrentQueue<(int, int)>();

            workers = new Thread[workerCount];
            for (int i = 0; i < workerCount; i++)
            {
                workers[i] = new Thread(Worker);
                workers[i].Start();
            }


            Thread controllerThread = new Thread(ControllerThread);
            controllerThread.Start();

            controllerThread.Join();

            workInProgress = false;

            lock (locker)
            {
                Monitor.PulseAll(locker);
            }

            foreach (var worker in workers)
            {
                worker.Join();
            }

            Console.WriteLine("Final result: " + array[0]);
        }

        static void ControllerThread()
        {
            currentLength = array.Length;

            while (currentLength > 1)
            {
                int halfLength = currentLength / 2;
                completedWorkers = 0;

                for (int i = 0; i < halfLength; i++)
                {
                    int index = i;
                    int pairIndex = currentLength - 1 - i;
                    taskQueue.Enqueue((index, pairIndex));
                }

                lock (locker)
                {
                    Monitor.PulseAll(locker);
                }

                lock (locker)
                {
                    while (completedWorkers < halfLength)
                    {
                        Monitor.Wait(locker);
                    }
                }

                currentLength = halfLength + (currentLength % 2);
            }
        }

        static void Worker()
        {
            while (workInProgress)
            {
                if (taskQueue.TryDequeue(out var task))
                {
                    array[task.Item1] += array[task.Item2];
 
                    lock (locker)
                    {
                        completedWorkers++;
                        Monitor.PulseAll(locker);
                    }
                }
                else
                {
                    lock (locker)
                    {
                        if (workInProgress && taskQueue.IsEmpty)
                        {
                            Monitor.Wait(locker);
                        }
                    }
                }
            }
        }
    }
}