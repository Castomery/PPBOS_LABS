using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LAB2_PPBOS
{
    internal class Calculator
    {
        private int[] arr;

        public Calculator(int[] array)
        {
            this.arr = array;
        }

        public int GetSumByThreads(int countOfThreads)
        {

          return arr[countOfThreads];
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
