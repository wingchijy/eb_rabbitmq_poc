package com.wing.multiThread;

import com.rabbitmq.client.QueueingConsumer;


public class ConsumeWorker implements Runnable
{
    private String threadName;
    private int queueNo;
    protected MqInstance mqInst = null;


    public ConsumeWorker(int seqNo)
    {
        this.threadName = String.format("t-consumer-%d", seqNo);
        this.queueNo = seqNo % Config.MQ_QUEUE_COUNT;
    }


    @Override
    public void run()
    {
        try
        {
            mqInst = new MqInstance();

            String queueName = MqGlobal.getInstance().getQueueByNo(this.queueNo).getQueueName();

            QueueingConsumer consumer = new QueueingConsumer( mqInst.getChannel() );

            mqInst.getChannel().basicConsume(queueName, false, consumer);

            mqInst.getChannel().basicQos(1);

            this.doWork(consumer);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void doWork(QueueingConsumer consumer)
    {
        long startTime = System.currentTimeMillis();
        int count = 0;

        while (true)
        {
            try{
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                mqInst.getChannel().basicAck(
                        delivery.getEnvelope().getDeliveryTag(), false );
            }
            catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println(
//                    String.format("consume: %s | qName=%s | qNo=%d | %d",
//                    this.threadName, queueName, this.queueNo, delivery.getBody().length ));

            // compute time-cost.
            count++;
            if( count == Config.TIMECOST_ONETIME_DEAL_COUNT ){
                long endTime = System.currentTimeMillis();
                System.out.println( String.format("Consumer: %s | %d | %d ",
                        this.threadName, (endTime-startTime), count) );

                startTime = System.currentTimeMillis();
                count = 0;
            }
        }
    }

}
