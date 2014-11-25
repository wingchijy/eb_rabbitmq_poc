package com.wing.multiThread;


import com.rabbitmq.client.AMQP;

public class MqQueue
{
    private String queueName;
    private int queueNo;
    private AMQP.Queue.DeclareOk queue;


    public int messageCount(){
        return queue.getMessageCount();
    }

    public void setQueue(AMQP.Queue.DeclareOk queue) {
        this.queue = queue;
    }

    public String getQueueName() {
        return queueName;
    }
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public int getQueueNo() {
        return queueNo;
    }
    public void setQueueNo(int queueNo) {
        this.queueNo = queueNo;
    }

}


