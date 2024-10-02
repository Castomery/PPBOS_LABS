package org.example;
import java.util.Arrays;
import java.util.concurrent.*;

public class ArraySumCalculator {

    private Worker[] _workers;
    private boolean _isWorkInProcess;
    private int[] _countOfCompletedWork;
    private ControllerThread _controllerThread;

    private int stopIndex;
    private int currentLength;

    public void setWorkers(Worker[] workers)
    {
        _workers = workers;
        _countOfCompletedWork = new int[_workers.length];
        _isWorkInProcess = true;
    }

    public void getSum(long[] array)
    {
        stopIndex = array.length;

        _controllerThread = new ControllerThread(this);
        _controllerThread.start();


        while (stopIndex > 1)
        {

            for (int i = 0; i < _countOfCompletedWork.length; i++)
            {
                _countOfCompletedWork[i] = 0;
            }

            currentLength = stopIndex;

            stopIndex = stopIndex / 2 + stopIndex % 2;

            for (int i = 0; i< stopIndex && i < _workers.length; i++)
            {
                _workers[i].SetTasks(i, _workers.length,true);
            }

            if (getCountOfCompletedWork() < stopIndex)
            {
                _controllerThread.IsSemaphoreWaiting = true;
                _controllerThread.acquireCalculationSemaphore();
            }


        }

        _isWorkInProcess = false;

        for (int i = 0; i < _workers.length; i++)
        {
            _workers[i].StopWorker();
        }
    }

    public boolean getIsWorkInProcess(){
        return _isWorkInProcess;
    }

    public void increaseCountOfCompletedWork(int index)
    {
        _countOfCompletedWork[index]++;
    }

    public int getCountOfCompletedWork(){
        int countOfCompletedWork = 0;
        for (int i = 0; i < _countOfCompletedWork.length; i++)
        {
            countOfCompletedWork += _countOfCompletedWork[i];
        }
        return countOfCompletedWork;
    }

    public int getStopIndex(){
        return stopIndex;
    }

    public int getCurrentLength(){
        return currentLength;
    }
}