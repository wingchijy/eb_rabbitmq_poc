package com.wing.multiThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LaunchMain
{
    public static void main(String[] args)
    {
        String configFile;
        String workerRole = null;
        Config conf = new Config();

        // check parameters.
        if (args.length > 2 || args.length == 0) {
            System.out.println("Input parameter error!");
            return;
        } else {
            configFile = args[0];

            if (args.length == 2) {
                workerRole = args[1];
            }
        }

        try
        {
            // load config items.
            conf.load(configFile);

            // create mq instances.
            MqInstance mq = new MqInstance();
            mq.createPublicResource();

            // multi-thread work.
            if ( workerRole == null ) {
                startProduce();
                startConsume();
            }
            else if ("produce".equals(workerRole)) {
                startProduce();
            }
            else if ("consume".equals(workerRole)) {
                startConsume();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void startProduce() throws Exception
    {
        // create producers.
        ExecutorService produce = Executors.newCachedThreadPool();

        for(int i=0; i< Config.PRODUCE_WORKER_COUNT; i++) {
            produce.execute(new ProduceWorker(i));
        }
        produce.shutdown();
    }

    public static void startConsume() throws Exception
    {
        // create consumers.
        ExecutorService consumer = Executors.newCachedThreadPool();

        for (int i = 0; i < Config.CONSUME_WORKER_COUNT; i++) {
            if("Blocking".equalsIgnoreCase(Config.CONSUME_MODE) ) {
                consumer.execute(new ConsumeBlockingWorker(i));
            }
            else if("Polling".equalsIgnoreCase(Config.CONSUME_MODE) ) {
                consumer.execute(new ConsumePollingWorker(i));
            }
        }
        consumer.shutdown();
    }
}
