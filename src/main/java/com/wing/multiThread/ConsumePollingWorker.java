package com.wing.multiThread;

import com.rabbitmq.client.GetResponse;
import java.util.ArrayList;


public class ConsumePollingWorker extends WorkerFactory
{
    private ArrayList<Integer> queueList = new ArrayList<Integer>();


    public ConsumePollingWorker(int no)
    {
        seqNo = no;
        threadName = String.format("t-consumer-%d", seqNo);

        this.assignQueueByCycle();
    }


    public void assignQueueByCycle()
    {
        for (int i=0; i<Config.CONSUME_QUEUE_ASSIGN_CYCLE; i++)
        {
            for(int j=0; j<Config.MQ_QUEUE_COUNT; j++)
            {
                int k = i *Config.MQ_QUEUE_COUNT +j;

                if (k % Config.CONSUME_WORKER_COUNT == seqNo) {
                    queueList.add( k % Config.MQ_QUEUE_COUNT );

                    logger.debug( String.format("seqNo=%d | queueNo=%d",
                            seqNo, k % Config.MQ_QUEUE_COUNT) );
                }
            }
        }
    }


    @Override
    public void run()
    {
        try
        {
            mqInst = new MqInstance();

            while(true)
            {
                if( getMessageCountInQueues() == 0 )
                    Thread.sleep(Config.CONSUME_SLEEP_SECONDS * 1000);

                // consume messages.
                for(Integer queueNo: queueList)
                {
                    String queueName = MqGlobal.getInstance().
                            getQueueByNo(queueNo).getQueueName();

                    this.process(queueNo, queueName);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void process(Integer queueNo, String queueName)
    {
        setStartTime();

        while (true)
        {
            try{
                GetResponse response = mqInst.pollingConsumeMessage(queueName);

                if ( response == null )
                    return;

                logger.debug( String.format("consumer: %s | qName=%s | qNo=%d | %d",
                        this.threadName, queueName, queueNo, response.getBody().length ));
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }

            msgCount++;
            if( msgCount == Config.CONSUME_TIMECOST_ONCE_COUNT )
                printCostTime();
        }
    }

    public int getMessageCountInQueues()
    {
        int count = 0;

        // check msg_count in queues.
        // If msg_count==0, then sleep n seconds.

        for(Integer queueNo: queueList){
            count += MqGlobal.getInstance().getQueueByNo(queueNo).messageCount();
        }

        if ( count > 0 )
            System.out.println("----" +seqNo +"----" +count);

        return count;
    }

}
