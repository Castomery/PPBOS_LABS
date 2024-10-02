using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LAB2_PPBOS
{
    internal class Calculator
    {
        private Worker[] _workers;
        private Semaphore _semaphore;
        private bool _isWaiting;
        private bool _isWorkInProcess;
        private int[] _countOfCompletedWork;

        public int StopIndex { get; private set; }
        public int CurrentLength { get; private set; }

        public void SetWorkers(Worker[] workers)
        {
            _workers = workers;
            _countOfCompletedWork = new int[_workers.Length];
            _isWorkInProcess = true;
        }

        public void GetSum(long[] array)
        {
            StopIndex = array.Length;

            Thread controllThread = new Thread(() => CheckIfWaveCompleted());
            controllThread.Start();

            _semaphore = new Semaphore(0, 1);

            while (StopIndex > 1)
            {
                
                for (int i = 0; i < _countOfCompletedWork.Length; i++)
                {
                    _countOfCompletedWork[i] = 0;
                }

                CurrentLength = StopIndex;

                StopIndex = StopIndex / 2 + StopIndex % 2;

                for (int i = 0; i< StopIndex && i < _workers.Length; i++)
                {
                    _workers[i].SetTasks(i, _workers.Length,true);
                }

                if (_countOfCompletedWork.Sum() < StopIndex)
                {
                    _isWaiting = true;
                    _semaphore.WaitOne();
                }

                
            }

            _isWorkInProcess = false;

            for (int i = 0; i < _workers.Length; i++)
            {
                _workers[i].StopWorker();
            }
        }

        public void IncreaseCountOfCompletedWork(int index)
        {
            _countOfCompletedWork[index]++;
        }

        private void CheckIfWaveCompleted()
        {
            while (_isWorkInProcess)
            {
                if (_countOfCompletedWork.Sum() >= StopIndex)
                {
                    if (_isWaiting)
                    {
                        _isWaiting = false;
                        _semaphore.Release();
                    }
                }
            }
        }
    }
}
