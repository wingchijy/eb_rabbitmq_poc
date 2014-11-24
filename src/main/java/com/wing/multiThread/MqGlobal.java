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


    public MqQueue createQueue(Channel channel, String name, int no) throws Exception
    {
        if( queues.get(no) != null ){
            return queues.get(no);
        }
        else
        {
            MqQueue q = new MqQueue();
            q.setQueueName(name);
            q.setQueueNo(no);

            Map<String, Object> args = new HashMap<String, Object>();
            args.put("x-ha-policy", "all");
            q.setQueue(
                    channel.queueDeclare(name, Config.MQ_DURABLE, false, false, args) );

            queues.put(no, q);
            return q;
        }
    }

    public MqQueue getQueueByNo(int no)
    {
        return queues.get(no);
    }



    public MqExchange createExchange(Channel channel, String name, int no) throws Exception
    {
        if( exchanges.get(no) != null ){
            return exchanges.get(no);
        }
        else
        {
            MqExchange ex = new MqExchange();

            ex.setExchange(
                    channel.exchangeDeclare(name, Config.MQ_EXCHANGE_TYPE, true));
            ex.setExchangeName(name);
            ex.setExchangeNo(no);

            exchanges.put(no, ex);

            // exchange bind to queues.
            this.exchangeBindAllQueues(channel, no, Config.MQ_ROUTE_KEY_PREFIX);

            return ex;
        }

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


