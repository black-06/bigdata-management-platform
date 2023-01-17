package com.bmp.cron.broker;

import com.bmp.cron.Task;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

@RequiredArgsConstructor
public class RocketMQBroker implements Broker {
    private final String namesrvAddr;
    private final String topic;
    private final String consumerGroup;

    private final DefaultMQProducer producer;

    public RocketMQBroker(String namesrvAddr, String topic, String consumerGroup, String producerGroup) {
        this.namesrvAddr = namesrvAddr;
        this.topic = topic;
        this.consumerGroup = consumerGroup;

        this.producer = new DefaultMQProducer();
        this.producer.setNamesrvAddr(namesrvAddr);
        this.producer.setProducerGroup(producerGroup);
    }

    @Override
    public void send(Task task) throws Exception {
        byte[] msg = task.serialize();
        producer.send(new Message(topic, msg));
    }

    @Override
    public void startConsume() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumerGroup(consumerGroup);
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                Task task;
                try {
                    task = Task.deserialize(msg.getBody());
                    Task.validate(task);
                    // run
                } catch (Exception e) {
                    // TODO:
                    // Note here we do not do re-tries using the message queue.
                    // The consumeTask callback function should have error handling mechanisms for retries.
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }
            // TODO:
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
    }
}
