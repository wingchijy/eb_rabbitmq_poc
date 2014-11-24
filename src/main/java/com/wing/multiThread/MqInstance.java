package com.wing.multiThread;

import com.rabbitmq.client.*;


public class MqInstance
{
    private Connection connection;
    private Channel channel;
    private MqGlobal mqGlobal = MqGlobal.getInstance();


    public MqInstance() throws Exception
    {
        ConnectionFactory factory = new ConnectionFactory();

        String[] hosts = Config.MQ_HOSTS.split("\\/");
        if(hosts.length == 1){
            factory.setHost( hosts[0] );
            factory.setPort( 5673 );
            System.out.println( String.format("host=%s", hosts[0] ));
            this.connection = factory.newConnection();
        }
        else{
            Address[] addrArr = new Address[hosts.length];
            for(int i=0; i<hosts.length; i++)
            {
                addrArr[i] = new Address(hosts[i].trim(), 5673);
                System.out.println( String.format("host_address[%d]=%s", i, hosts[i]) );
            }
            this.connection = factory.newConnection(addrArr);
        }
        
        this.channel = connection.createChannel();
    }


    // queues.
    public void createAllQueues(int count, String prefix) throws Exception
    {
        for(int i=0; i<count; i++)
        {
            String name = String.format("%s%d", prefix, i);
            mqGlobal.createQueue(this.channel, name, i);
        }
    }



    // exchanges.
    public void createAllExchanges(int count, String prefix) throws Exception
    {
        for(int i=0; i<count; i++)
        {
            String name = String.format("%s%d", prefix, i);
            mqGlobal.createExchange(this.channel, name, i);
        }
    }


    // Publish
    public void publishOneMessage(int exchangeNo, String routingKey, byte[] message) throws Exception
    {
        this.channel.basicPublish(
                mqGlobal.getExchangeByNo(exchangeNo).getExchangeName(),
                routingKey, MessageProperties.BASIC, message);
    }


    // close
    public void close() throws Exception{
        this.channel.close();
        this.connection.close();
    }

    // channel.
    public Channel getChannel(){
        return this.channel;
    }
}


