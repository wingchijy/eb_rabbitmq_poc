package com.wing.multiThread;


public class ProduceWorker extends WorkerFactory
{
    private int exchangeNo;


    public ProduceWorker(int no)
    {
        seqNo = no;
        threadName = String.format("t-producer-%d", no);
        exchangeNo = no % Config.MQ_EXCHANGE_COUNT;
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
                process(msg);

                Thread.sleep( Config.SEND_INTERVAL_SECONDS * 1000 );
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void process(byte[] message)
    {
        setStartTime();

        for(int i=0; i< Config.SEND_MESSAGE_COUNT; i++)
        {
            String key = String.format("%s%d.%d-%d",
                    Config.MQ_ROUTE_KEY_PREFIX,
                    (i % Config.MQ_QUEUE_COUNT),
                    exchangeNo, i);
            try{
                mqInst.publishMessage(exchangeNo, key, message);

                logger.debug( String.format("producer: %s | exNo=%d | msgNo=%d | key=%s | %d",
                        threadName, exchangeNo, i, key, message.length ));
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }

            msgCount++;
            if (msgCount == Config.PRODUCE_TIMECOST_ONCE_COUNT)
                printCostTime();
        }

        if(msgCount > 0 )
            printCostTime();
    }

}
