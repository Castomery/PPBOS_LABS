package org.example;

import java.util.concurrent.Semaphore;

public class ControllerThread extends Thread{
    public Boolean IsSemaphoreWaiting;
    private ArraySumCalculator _calculator;
    private Semaphore _calculationSemaphore;

    public ControllerThread(ArraySumCalculator calculator)
    {
        _calculator = calculator;
        _calculationSemaphore = new Semaphore(0);
    }

    public void acquireCalculationSemaphore()
    {
        try {
            _calculationSemaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run()
    {
        while(_calculator.getIsWorkInProcess())
        {
            if (_calculator.getCountOfCompletedWork() >= _calculator.getStopIndex())
            {
                if(IsSemaphoreWaiting)
                {
                    IsSemaphoreWaiting = false;
                    _calculationSemaphore.release();
                }
            }
        }
    }
}
