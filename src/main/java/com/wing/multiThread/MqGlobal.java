package com.wing.multiThread;

import com.rabbitmq.client.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MqGlobal
{
    private static MqGlobal mqGlobal = null;
    private static Map<Integer, MqQueue> queues= new HashMap<Integer,MqQueue>();
    private static Map<Integer, MqExchange> exchanges = new HashMap<Integer, MqExchange>();


    public static MqGlobal getInstance()
    {
        if (mqGlobal == null) {
            mqGlobal = new MqGlobal();
        }
        return mqGlobal;
    }


    // queues.
    public void createAllQueues(Channel channel, int count, String prefix) throws Exception
    {
        for(int i=0; i<count; i++)
        {
            String name = String.format("%s%d", prefix, i);
            createQueue(channel, name, i);
        }
    }

    public void createQueue(Channel channel, String name, int no) throws Exception
    {
        if( queues.get(no) != null ){
            return ;
        }

        MqQueue q = new MqQueue();
        q.setQueueName(name);
        q.setQueueNo(no);
        q.setQueue( this.setQueue(channel, name) );

        queues.put(no, q);
    }


    public AMQP.Queue.DeclareOk setQueue(Channel channel, String name) throws Exception
    {
        Map<String, Object> args = null;
//        args = new HashMap<String, Object>();
//        args.put("x-ha-policy", "all");

        return channel.queueDeclare(name, Config.MQ_DURABLE, false, false, args) ;
    }


    public MqQueue getQueueByNo(int no)
    {
        return queues.get(no);
    }


    // exchanges
    public void createAllExchanges(Channel channel, int count, String prefix) throws Exception
    {
        for(int i=0; i<count; i++)
        {
            String name = String.format("%s%d", prefix, i);
            createExchange(channel, name, i);
        }
    }

    public MqExchange createExchange(Channel channel, String name, int no) throws Exception
    {
        if( exchanges.get(no) != null ){
            return exchanges.get(no);
        }
        else
        {
            MqExchange ex = new MqExchange();
            ex.setExchangeName(name);
            ex.setExchangeNo(no);
            ex.setExchange( this.setExchange(channel,name) );

            exchanges.put(no, ex);

            // exchange bind queues.
            exchangeBindAllQueues(channel, no, Config.MQ_ROUTE_KEY_PREFIX);

            return ex;
        }
    }

    public AMQP.Exchange.DeclareOk setExchange(Channel channel, String name) throws Exception
    {
        return channel.exchangeDeclare(name, Config.MQ_EXCHANGE_TYPE, true);
    }


    public void exchangeBindAllQueues(Channel channel, int exchangeNo, String routePrefix) throws Exception
    {
        Iterator iter = queues.keySet().iterator();

        while (iter.hasNext())
        {
            Integer no = (Integer)iter.next();
            MqQueue q =  queues.get(no);

            channel.queueBind(
                    q.getQueueName(),
                    this.getExchangeByNo(exchangeNo).getExchangeName(),
                    String.format("%s%d.*",routePrefix, q.getQueueNo()) );
        }
    }


    public MqExchange getExchangeByNo(int no)
    {
        return exchanges.get(no);
    }



    public static Map<Integer, MqQueue> getQueues() {
        return queues;
    }

    public static void setQueues(Map<Integer, MqQueue> queues) {
        MqGlobal.queues = queues;
    }

    public static Map<Integer, MqExchange> getExchanges() {
        return exchanges;
    }

    public static void setExchanges(Map<Integer, MqExchange> exchanges) {
        MqGlobal.exchanges = exchanges;
    }

}


