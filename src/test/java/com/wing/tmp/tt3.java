package com.wing.tmp;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

public class tt3
{
    public static String aaa;

    public void init()
    {
        try{
            Configuration config = new PropertiesConfiguration(
                    "/Users/chijy/works/everBridge/src/test/rmq/rabbitmq_poc/rmqtest.conf");

            aaa = config.getString("HOST");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
