package com.wing.multiThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public abstract class WorkerFactory implements Runnable
{
    protected int seqNo;
    protected String threadName;
    protected MqInstance mqInst = null;

    protected static Logger logger = LoggerFactory.getLogger(WorkerFactory.class);

    public void process(){}


    protected static long startTime;
    protected static int msgCount;

    public void setStartTime()
    {
        startTime = System.currentTimeMillis();
        msgCount = 0;
    }

    public void printCostTime()
    {
        long endTime = System.currentTimeMillis();

        System.out.println( String.format("%s | %d | %d ",
                this.threadName,  (endTime-startTime), msgCount) );

        setStartTime();
    }

}
