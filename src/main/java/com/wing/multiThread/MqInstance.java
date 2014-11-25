package com.wing.multiThread;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MqInstance
{
    private Connection connection;
    private Channel channel;
    private QueueingConsumer consumer;

    private static Logger logger = LoggerFactory.getLogger(MqInstance.class);


    public MqInstance() throws Exception
    {
        String[] hosts = Config.MQ_HOSTS.split("\\/");

        if(hosts.length == 0){
            logger.error( "MQ_HOSTS parameter is error." );
            return;
        }
        else if(hosts.length == 1){
            connectSingleHost( hosts[0] );
        }
        else{
            connectMultiHosts( hosts );
        }
        
        this.channel = connection.createChannel();
    }


    // connect.
    public void connectSingleHost(String hostName) throws Exception
    {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost( hostName );
        factory.setPort( Config.MQ_PORT );

        logger.info( String.format("host=%s|port=%d", hostName, Config.MQ_PORT ));

        this.connection = factory.newConnection();
    }

    public void connectMultiHosts(String[] hosts) throws Exception
    {
        ConnectionFactory factory = new ConnectionFactory();

        Address[] addrArr = new Address[hosts.length];

        for(int i=0; i<hosts.length; i++){
            addrArr[i] = new Address(hosts[i].trim(), Config.MQ_PORT);

            System.out.println( String.format("host_address[%d]=%s", i, hosts[i]) );
        }

        this.connection = factory.newConnection(addrArr);
    }


    // create queues & exchanges.
    public void createPublicResource() throws Exception
    {
        createAllQueues(Config.MQ_QUEUE_COUNT, Config.MQ_QUEUE_NAME_PREFIX);

        createAllExchanges(Config.MQ_EXCHANGE_COUNT, Config.MQ_EXCHANGE_NAME_PREFIX);
    }

    public void createAllQueues(int count, String prefix) throws Exception
    {
        MqGlobal.getInstance().createAllQueues(this.channel, count, prefix);
    }

    public void createAllExchanges(int count, String prefix) throws Exception
    {
        MqGlobal.getInstance().createAllExchanges(this.channel, count, prefix);
    }


    // producer.
    public void publishMessage(int exchangeNo, String routingKey, byte[] message) throws Exception
    {
        this.channel.basicPublish(
                MqGlobal.getInstance().getExchangeByNo(exchangeNo).getExchangeName(),
                routingKey,
                MessageProperties.BASIC,
                message);
    }

    // consumer.
    public void setBlockingConsumer(String queueName) throws Exception
    {
        this.consumer = new QueueingConsumer( this.channel );

        this.channel.basicConsume(
                queueName, Config.MQ_CONSUME_ACK, consumer);

        this.channel.basicQos(1);
    }

    public QueueingConsumer.Delivery blockingConsumeMessage() throws Exception
    {
        QueueingConsumer.Delivery delivery = consumer.nextDelivery();

        this.channel.basicAck(
                delivery.getEnvelope().getDeliveryTag(),
                Config.MQ_CONSUME_ACK);

        return delivery;
    }

    public GetResponse pollingConsumeMessage(String queueName) throws Exception
    {
        //noAck = true，不需要回复，接收到消息后，queue上的消息就会清除
        //noAck = false，需要回复，接收到消息后，queue上的消息不会被清除，
        //  直到调用channel.basicAck(deliveryTag, false); queue上的消息才会被清除
        //  而且，在当前连接断开以前，其它客户端将不能收到此queue上的消息.

        GetResponse response = this.channel.basicGet(
                queueName, Config.MQ_CONSUME_ACK);

        if(response == null)
            return null;

        this.channel.basicAck(
                response.getEnvelope().getDeliveryTag(),
                Config.MQ_CONSUME_ACK);

        return response;
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


