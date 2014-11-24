package com.wing.multiThread;


public class ProduceWorker implements Runnable
{
    private String threadName;
    private int exchangeNo;
    protected MqInstance mqInst;


    public ProduceWorker(int seqNo)
    {
        this.threadName = String.format("t-producer-%d", seqNo);
        this.exchangeNo = seqNo % Config.MQ_EXCHANGE_COUNT;
    }


    @Override
    public void run()
    {
        try
        {
            mqInst = new MqInstance();

            byte[] msg = new Message(Config.MESSAGE_PER_SIZE).getValue();

            for(int i=0; i< Config.SEND_TIMES; i++)
            {
                doWork(msg);
                Thread.sleep( Config.SEND_ONETIME_INTERVAL_SECONDS * 1000 );
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void doWork(byte[] message)
    {
        long startTime = System.currentTimeMillis();
        int count=0;

        for(int i=0; i< Config.SEND_ONETIME_MESSAGE_COUNT; i++)
        {
            String key = String.format("%s%d.%d-%d",
                    Config.MQ_ROUTE_KEY_PREFIX,
                    (i % Config.MQ_QUEUE_COUNT),
                    this.exchangeNo, i);
            try
            {
                mqInst.publishOneMessage(this.exchangeNo, key, message);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println(
//                    String.format("producer: %s | exNo=%d | msgNo=%d | key=%s | %d",
//                    this.threadName, this.exchangeNo, i, key, message.length ));

            // compute time-cost.
            count++;
            if (count == Config.TIMECOST_ONETIME_DEAL_COUNT)
            {
                long endTime = System.currentTimeMillis();
                System.out.println( String.format("Producer: %s | %d | %d ",
                        this.threadName, (endTime-startTime), Config.TIMECOST_ONETIME_DEAL_COUNT) );

                startTime = System.currentTimeMillis();
                count=0;
            }
        }

        // compute time-cost.
        if(count > 0)
        {
            long endTime = System.currentTimeMillis();
            System.out.println( String.format("Producer: %s | %d | %d ",
                    this.threadName, (endTime-startTime), count) );
        }
    }

}
