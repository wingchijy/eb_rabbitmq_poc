package com.wing.tmp;

import java.util.ResourceBundle;

public class tt2
{
    public static final String HOST = getString("rabbit", "HOST");

    public static final int WORKER_PRODUCE_COUNT = getInt("rabbit", "WORKER_PRODUCE_COUNT");
    public static final int WORKER_CONSUME_COUNT = getInt("rabbit", "WORKER_CONSUME_COUNT");

    public static final int MESSAGE_PER_SIZE = getInt("rabbit", "MESSAGE_PER_SIZE");

    public static final int SEND_TIMES = getInt("rabbit", "SEND_TIMES");
    public static final int SEND_ONETIME_MESSAGE_COUNT    = getInt("rabbit", "SEND_ONETIME_MESSAGE_COUNT");
    public static final int SEND_ONETIME_INTERVAL_SECONDS = getInt("rabbit", "SEND_ONETIME_INTERVAL_SECONDS");
    public static final int TIMECOST_ONETIME_DEAL_COUNT   = getInt("rabbit", "TIMECOST_ONETIME_DEAL_COUNT");


    public static final int     MQ_QUEUE_COUNT = getInt("rabbit", "MQ_QUEUE_COUNT");
    public static final int     MQ_EXCHANGE_COUNT = getInt( "rabbit", "MQ_EXCHANGE_COUNT" );
    public static final String  MQ_QUEUE_NAME_PREFIX = getString("rabbit", "MQ_QUEUE_NAME_PREFIX");
    public static final String  MQ_EXCHANGE_NAME_PREFIX = getString("rabbit", "MQ_EXCHANGE_NAME_PREFIX");

    public static final String  MQ_ROUTE_KEY_PREFIX = getString("rabbit", "MQ_ROUTE_KEY_PREFIX");
    public static final boolean MQ_DURABLE = getBoolean("rabbit", "MQ_DURABLE");
    public static final String  MQ_EXCHANGE_TYPE = getString("rabbit", "MQ_EXCHANGE_TYPE");


    public static int getInt(String fname, String key)
    {
        try {
            return Integer.parseInt(
                    ResourceBundle.getBundle(fname).getString(key).trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getString(String fname, String key)
    {
        try {
            return ResourceBundle.getBundle(fname).getString(key).trim();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean getBoolean(String fname, String key)
    {
        try {
            return Boolean.valueOf(
                    ResourceBundle.getBundle(fname).getString(key).trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

}
