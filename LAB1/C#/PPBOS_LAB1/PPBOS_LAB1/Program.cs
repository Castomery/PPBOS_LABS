using System.Diagnostics;

namespace PPBOS_LAB1
{
    internal class Program
    {
        static int[] arr;
        static int arrSize = 5000000;
        static int countOfThreads = 5;
        static void Main(string[] args)
        {
            arr = new int[arrSize];

            for (int i = 0; i < arrSize; i++)
            {
                arr[i] = i;
            }

            Calculator calculator = new Calculator(arr);
            Stopwatch timer = new Stopwatch();


            
            Console.WriteLine("One Thread");
            timer.Start();
            Console.WriteLine("Sum: " + calculator.GetSumByThreads(1));
            timer.Stop();
            Console.WriteLine("Time: " + timer.Elapsed.TotalMilliseconds);

            timer.Reset();
            Console.WriteLine("Multiple Threads");
            timer.Start();
            Console.WriteLine("Sum: " + calculator.GetSumByThreads(countOfThreads));
            timer.Stop();
            Console.WriteLine("Time: " + timer.Elapsed.TotalMilliseconds);

            Console.ReadKey();
        }
    }
}