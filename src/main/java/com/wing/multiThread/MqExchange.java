package com.wing.multiThread;


import com.rabbitmq.client.AMQP;

public class MqExchange
{
    private String exchangeName;
    private int exchangeNo;
    private AMQP.Exchange.DeclareOk exchange;


    public void setExchange(AMQP.Exchange.DeclareOk exchange) {
        this.exchange = exchange;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public int getExchangeNo() {
        return exchangeNo;
    }

    public void setExchangeNo(int exchangeNo) {
        this.exchangeNo = exchangeNo;
    }
}


