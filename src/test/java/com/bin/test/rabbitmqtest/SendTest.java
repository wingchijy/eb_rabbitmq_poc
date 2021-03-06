package rabbitmqtest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SendTest {
	
	private static Logger logger = LoggerFactory.getLogger(SendTest.class); 
	
	int queueCount;
	int sendThreadCount;
	int messageTotalCount;
	byte[] message;
	static long [] times;
	AtomicInteger totalCountIndex = new AtomicInteger(0);
	
	private void pushTask() throws IOException {
		final int perThreadCount = messageTotalCount / sendThreadCount;
		final int lastThreadCount = perThreadCount + messageTotalCount % sendThreadCount;
		final int perQueueCount = messageTotalCount / queueCount;
		
		ConnectionFactory factory = new ConnectionFactory();
//		factory.setHost("10.68.52.201");
        factory.setHost("127.0.0.1");
		final Connection connection = factory.newConnection();
		
		final Channel channel = connection.createChannel();
		channel.exchangeDeclare("testexchange", "topic", true);
		channel.confirmSelect();
		
		for (int i = 0; i < queueCount; i++) {
			try {
				channel.queueDeclare(String.valueOf(i), true, false, false, null);
				channel.queueBind(String.valueOf(i), "testexchange", String.valueOf(i) + ".*"); 
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		message = new Message(111, "123").toString().getBytes();
		times = new long[sendThreadCount];
		
		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < sendThreadCount; i++) {
			Thread sendThread = new Thread(new Runnable() {
				public void run() {
					
					long startTime = System.currentTimeMillis();
					String threadName = Thread.currentThread().getName();
					logger.debug("thread name {}", threadName);
					
					int length = perThreadCount;
					if (Integer.valueOf(threadName) == sendThreadCount - 1) {
						length = lastThreadCount;
					}
					
					for (int j = 0; j < length; j++) {
						try {
							int index = totalCountIndex.getAndAdd(1);
							String routingKey = (index % queueCount) + "." + index;
							channel.basicPublish("testexchange", routingKey, MessageProperties.PERSISTENT_BASIC, message);
						}
						catch (IOException e) {
							e.printStackTrace();
						}
					}

					long time = 0;
					try {
						while (channel.waitForConfirms())
						{
							logger.debug("{} confirms.", threadName);
							time = System.currentTimeMillis() - startTime;
							break;
						}
					}
					catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					if (time == 0) {
						logger.debug("something error.");
						return;
					}
					
					times[Integer.parseInt(threadName)] = time;
					if (Integer.valueOf(threadName) == sendThreadCount - 1) {
						try {
							Thread.currentThread().sleep(1000 * 3);
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
						Arrays.sort(times);
						System.out.println(times[sendThreadCount - 1]);
					}
				}
			});
			sendThread.setName(String.valueOf(i));
			threads.add(sendThread);
		}
		
		for (Thread sendThread : threads) {
			sendThread.start();
		}
	}
	
	public static void main(String[] args) {
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		SendTest sender = (SendTest)factory.getBean("sendtest");
		try {
			sender.pushTask();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getQueueCount() {
		return queueCount;
	}

	public void setQueueCount(int queueCount) {
		this.queueCount = queueCount;
	}

	public int getSendThreadCount() {
		return sendThreadCount;
	}

	public void setSendThreadCount(int sendThreadCount) {
		this.sendThreadCount = sendThreadCount;
	}

	public int getMessageTotalCount() {
		return messageTotalCount;
	}

	public void setMessageTotalCount(int messageTotalCount) {
		this.messageTotalCount = messageTotalCount;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}

}
