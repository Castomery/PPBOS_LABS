package org.example;

import java.util.concurrent.Semaphore;

public class Worker extends Thread{

    private long[] _array;
    private ArraySumCalculator _calculator;
    private boolean _isWorking = false;
    private Semaphore _isWorkingSemaphore = new Semaphore(0);
    private int _threadIndex;
    private int _startIndex;
    private int _step;

    public Worker( long[] array, ArraySumCalculator calculator, int threadIndex)
    {
        _array = array;
        _calculator = calculator;
        _threadIndex = threadIndex;
    }

    public void SetTasks(int startIndex, int step,boolean isWorking)
    {
        _startIndex = startIndex;
        _step = step;
        _isWorking = isWorking;

        if (_isWorking)
        {
            _isWorkingSemaphore.release();
        }
    }

    @Override
    public void run()
    {
        while (true)
        {
            try {
                _isWorkingSemaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!_isWorking)
            {
                break;
            }

            for (int i = _startIndex; i < _calculator.getStopIndex(); i+=_step)
            {
                int pairIndex = _calculator.getCurrentLength() - i - 1;

                if (i == pairIndex)
                {
                    _calculator.increaseCountOfCompletedWork(_threadIndex);
                    continue;
                }

                _array[i] += _array[pairIndex];
                _calculator.increaseCountOfCompletedWork(_threadIndex);
            }
        }
    }

    public void StopWorker()
    {
        _isWorking = false;
        _isWorkingSemaphore.release();
    }
}
