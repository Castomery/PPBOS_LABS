using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PPBOS_LAB1
{
    internal class Calculator
    {
        private int[] arr;

        public Calculator(int[] array)
        {
            this.arr = array;
        }

        public long GetSumByThreads(int countOfThreads)
        {
            
            long totalSum = 0;
            long[] partialSum = new long[countOfThreads];
            int arraySize = arr.Length;
            int chunkSize = arraySize / countOfThreads;

            Thread[] threads = new Thread[countOfThreads];

            for (int i = 0; i < countOfThreads; i++)
            {
                int start = i * chunkSize;
                int end = (i == countOfThreads - 1) ? arraySize : start + chunkSize;
                int threadIndex = i;

                threads[i] = new Thread(() => partialSum[threadIndex] = SumArrayPart(start, end));
                threads[i].Start();
            }

            for (int i = 0; i < countOfThreads; i++)
            {
                threads[i].Join();
                totalSum += partialSum[i];
            }

            return totalSum;
        }

        private long SumArrayPart(int startIndex, int endIndex)
        {
            long localSum = 0;

            for (int i = startIndex; i < endIndex; i++)
            {
                localSum += arr[i];
            }

            return localSum;
        }
    }
}
