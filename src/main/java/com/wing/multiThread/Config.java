package com.wing.multiThread;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config
{
    protected static Configuration config;

    public static String  MQ_HOSTS;
    public static int     MQ_QUEUE_COUNT;
    public static int     MQ_EXCHANGE_COUNT;
    public static String  MQ_QUEUE_NAME_PREFIX;
    public static String  MQ_EXCHANGE_NAME_PREFIX;

    public static String  MQ_ROUTE_KEY_PREFIX;
    public static boolean MQ_DURABLE;
    public static String  MQ_EXCHANGE_TYPE;


    public static int WORKER_PRODUCE_COUNT;
    public static int WORKER_CONSUME_COUNT;
    public static int MESSAGE_PER_SIZE;

    public static int SEND_TIMES;
    public static int SEND_ONETIME_MESSAGE_COUNT;
    public static int SEND_ONETIME_INTERVAL_SECONDS;
    public static int TIMECOST_ONETIME_DEAL_COUNT;


    public void load(String fileName) throws Exception
    {
        config = new PropertiesConfiguration(fileName);

        MQ_HOSTS = config.getString("MQ_HOSTS");
        MQ_QUEUE_COUNT = config.getInt("MQ_QUEUE_COUNT");
        MQ_QUEUE_NAME_PREFIX = config.getString("MQ_QUEUE_NAME_PREFIX");
        MQ_EXCHANGE_COUNT = config.getInt("MQ_EXCHANGE_COUNT");
        MQ_EXCHANGE_NAME_PREFIX = config.getString("MQ_EXCHANGE_NAME_PREFIX");

        MQ_ROUTE_KEY_PREFIX = config.getString("MQ_ROUTE_KEY_PREFIX");
        MQ_DURABLE = config.getBoolean("MQ_DURABLE");
        MQ_EXCHANGE_TYPE = config.getString("MQ_EXCHANGE_TYPE");

        WORKER_PRODUCE_COUNT = config.getInt("WORKER_PRODUCE_COUNT");
        WORKER_CONSUME_COUNT = config.getInt("WORKER_CONSUME_COUNT");

        MESSAGE_PER_SIZE = config.getInt("MESSAGE_PER_SIZE");

        SEND_TIMES = config.getInt("SEND_TIMES");
        SEND_ONETIME_MESSAGE_COUNT    = config.getInt("SEND_ONETIME_MESSAGE_COUNT");
        SEND_ONETIME_INTERVAL_SECONDS = config.getInt("SEND_ONETIME_INTERVAL_SECONDS");
        TIMECOST_ONETIME_DEAL_COUNT   = config.getInt("TIMECOST_ONETIME_DEAL_COUNT");
    }

}
