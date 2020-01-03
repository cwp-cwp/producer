package com.cwp.produce.test;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * Kafka 消费者测试类
 */
@SuppressWarnings("Duplicates")
public class TestKafkaConsumer {

    public static void main(String[] args) {
        receive();
    }

    private static void receive() {
        new Thread(() -> {
            long threadId = Thread.currentThread().getId();
            System.out.println("接收消息线程 threadId: " + threadId);
            KafkaConsumer<String, String> consumer = getKafkaConsumer();
            String patrolCarNumber = "B6666";
            while (true) {
//                patrolCarNumber = this.sendMessage.getPatrolCarNumber(); // 从发送消息的类中获取巡检车的车牌号，而发送消息的类可以从数据库中获取到巡检车车牌号
                if (patrolCarNumber != null && !"".equals(patrolCarNumber.trim())) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // TODO 约定好的 topic: response-B6666
            consumer.subscribe(Arrays.asList("response-" + patrolCarNumber));
            System.out.println("成功订阅主题 ===> " + "response-" + patrolCarNumber);
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("接收到云平台的响应 offset = " + record.offset() + "\t topic = " + record.topic() + "\t key = " + record.key() + "\t value = " + record.value());
                    // 收到响应之后修改数据状态
//                    this.messageDataService.updateStatus(record.value()); // TODO 接收到的 value 就是发送出去的 messageId
                }
            }
        }).start();
    }

    private static KafkaConsumer<String, String> getKafkaConsumer() {
        Properties props = new Properties();
        // 定义kakfa 服务的地址，不需要将所有broker指定上
        props.put("bootstrap.servers", "192.168.130.129:9092");
        // 制定consumer group
        props.put("group.id", "test");
        // 是否自动确认offset
        props.put("enable.auto.commit", "true");
        // 自动确认offset的时间间隔
        props.put("auto.commit.interval.ms", "1000");
        // key的序列化类
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // value的序列化类
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // 定义consumer
        return new KafkaConsumer<>(props);
    }

}
