package com.wing.multiThread;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config
{
    protected static Configuration config;

    public static String  MQ_HOSTS;
    public static int     MQ_PORT;

    public static int     MQ_QUEUE_COUNT;
    public static int     MQ_EXCHANGE_COUNT;
    public static String  MQ_QUEUE_NAME_PREFIX;
    public static String  MQ_EXCHANGE_NAME_PREFIX;

    public static String  MQ_ROUTE_KEY_PREFIX;
    public static boolean MQ_DURABLE;
    public static String  MQ_EXCHANGE_TYPE;
    public static boolean MQ_CONSUME_ACK;

    public static int PRODUCE_WORKER_COUNT;
    public static int CONSUME_WORKER_COUNT;
    public static int PRODUCE_TIMECOST_ONCE_COUNT;
    public static int CONSUME_TIMECOST_ONCE_COUNT;
    public static int CONSUME_QUEUE_ASSIGN_CYCLE;
    public static int CONSUME_SLEEP_SECONDS;

    public static int MESSAGE_PER_SIZE;
    public static int SEND_TIMES;
    public static int SEND_MESSAGE_COUNT;
    public static int SEND_INTERVAL_SECONDS;


    public void load(String fileName) throws Exception
    {
        config = new PropertiesConfiguration(fileName);


        MQ_HOSTS = config.getString("MQ_HOSTS");
        MQ_PORT = config.getInt("MQ_PORT");

        MQ_QUEUE_COUNT = config.getInt("MQ_QUEUE_COUNT");
        MQ_QUEUE_NAME_PREFIX = config.getString("MQ_QUEUE_NAME_PREFIX");
        MQ_EXCHANGE_COUNT = config.getInt("MQ_EXCHANGE_COUNT");
        MQ_EXCHANGE_NAME_PREFIX = config.getString("MQ_EXCHANGE_NAME_PREFIX");

        MQ_ROUTE_KEY_PREFIX = config.getString("MQ_ROUTE_KEY_PREFIX");
        MQ_DURABLE = config.getBoolean("MQ_DURABLE");
        MQ_EXCHANGE_TYPE = config.getString("MQ_EXCHANGE_TYPE");
        MQ_CONSUME_ACK = config.getBoolean("MQ_CONSUME_ACK");

        PRODUCE_WORKER_COUNT = config.getInt("PRODUCE_WORKER_COUNT");
        CONSUME_WORKER_COUNT = config.getInt("CONSUME_WORKER_COUNT");
        PRODUCE_TIMECOST_ONCE_COUNT = config.getInt("PRODUCE_TIMECOST_ONCE_COUNT");
        CONSUME_TIMECOST_ONCE_COUNT = config.getInt("CONSUME_TIMECOST_ONCE_COUNT");
        CONSUME_QUEUE_ASSIGN_CYCLE = config.getInt("CONSUME_QUEUE_ASSIGN_CYCLE");
        CONSUME_SLEEP_SECONDS = config.getInt("CONSUME_SLEEP_SECONDS");

        MESSAGE_PER_SIZE = config.getInt("MESSAGE_PER_SIZE");
        SEND_TIMES  = config.getInt("SEND_TIMES");
        SEND_MESSAGE_COUNT  = config.getInt("SEND_MESSAGE_COUNT");
        SEND_INTERVAL_SECONDS = config.getInt("SEND_INTERVAL_SECONDS");
    }

}
