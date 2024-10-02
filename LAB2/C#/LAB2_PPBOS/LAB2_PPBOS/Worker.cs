using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LAB2_PPBOS
{
    internal class Worker
    {
        private long[] _array;
        private Calculator _calculator;
        private bool _isWorking = false;
        private Semaphore _isWorkingSemaphore = new Semaphore(0, 1);
        private int _threadIndex;
        private int _startIndex;
        private int _step;

        public Worker( long[] array, Calculator calculator, int threadIndex)
        {
            _array = array;
            _calculator = calculator;
            _threadIndex = threadIndex;
        }

        public void SetTasks(int startIndex, int step,bool isWorking = false)
        {
            _startIndex = startIndex;
            _step = step;
            _isWorking = isWorking;

            if (_isWorking)
            {
                _isWorkingSemaphore.Release();
            }
        }

        public void DoWork()
        {
            while (true)
            {
                _isWorkingSemaphore.WaitOne();

                if (!_isWorking)
                {
                    break;
                }

                for (int i = _startIndex; i < _calculator.StopIndex; i+=_step)
                {
                    int pairIndex = _calculator.CurrentLength - i - 1;

                    if (i == pairIndex)
                    {
                        _calculator.IncreaseCountOfCompletedWork(_threadIndex);
                        continue;
                    }

                    _array[i] += _array[pairIndex];
                   _calculator.IncreaseCountOfCompletedWork(_threadIndex);
                }
            }
        }

        public void StopWorker()
        {
            _isWorking = false;
            _isWorkingSemaphore.Release();
        }
    }
}
