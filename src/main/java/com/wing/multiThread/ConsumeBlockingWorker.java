package com.wing.multiThread;

import com.rabbitmq.client.QueueingConsumer;


public class ConsumeBlockingWorker extends WorkerFactory
{
    private int queueNo;
    private String queueName;

    public ConsumeBlockingWorker(int seqNo)
    {
        threadName = String.format("t-consumer-%d", seqNo);
        queueNo = seqNo % Config.MQ_QUEUE_COUNT;
    }


    @Override
    public void run()
    {
        try
        {
            mqInst = new MqInstance();

            queueName = MqGlobal.getInstance().getQueueByNo(queueNo).getQueueName();

            mqInst.setBlockingConsumer( queueName );

            this.process();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            printCostTime();
        }
    }


    public void process()
    {
        setStartTime();

        while (true)
        {
            try{
                QueueingConsumer.Delivery delivery = mqInst.blockingConsumeMessage();

                logger.debug( String.format("consumer: %s | qName=%s | qNo=%d | %d",
                        threadName, queueName, queueNo, delivery.getBody().length ));
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }

            msgCount++;
            if (msgCount == Config.CONSUME_TIMECOST_ONCE_COUNT)
                printCostTime();
        }
    }

}
