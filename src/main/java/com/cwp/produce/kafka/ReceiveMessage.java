package com.cwp.produce.kafka;

import com.cwp.produce.service.MessageDataService;
import com.cwp.produce.utils.LogUtils;
import com.cwp.produce.utils.ReadConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Properties;

/**
 * 接收云平台的响应消息
 * Created by chen_wp on 2019-09-19.
 */
@Component
public class ReceiveMessage implements CommandLineRunner {

    private final static Logger LOG = LoggerFactory.getLogger(ReceiveMessage.class);

    private boolean kafkaConnect = true; // kafka 消费者是否创建成功

    @Autowired
    private MessageDataService messageDataService;

    @Autowired
    private SendMessage sendMessage;

    /**
     * SpringBoot 启动时会执行该方法一次
     */
    @Override
    public void run(String... args) {
        this.receiveMessage();
    }

    /**
     * 接收消息
     */
    private void receiveMessage() {
        new Thread(() -> {
            long threadId = Thread.currentThread().getId();
            LogUtils.info("接收消息线程 threadId: " + threadId);
            KafkaConsumer<String, String> consumer = null;
            while (kafkaConnect) {
                try {
                    consumer = this.getKafkaConsumer();
                    kafkaConnect = false;
                } catch (Exception e) {
                    LogUtils.info("Kafka 消费者创建失败 " + e.toString());
                    e.printStackTrace();
                    this.sleep(3000);
                }
            }
            String patrolCarNumber;
            while (true) {
                patrolCarNumber = this.sendMessage.getPatrolCarNumber(); // 从发送消息的类中获取巡检车的车牌号，而发送消息的类可以从数据库中获取到巡检车车牌号
                if (patrolCarNumber != null && !"".equals(patrolCarNumber.trim())) {
                    break;
                }
                this.sleep(3000);
            }
            // TODO 约定好的 topic: response-B6666
            consumer.subscribe(Arrays.asList("response-" + patrolCarNumber));
            LogUtils.info("成功订阅主题 ===> " + "response-" + patrolCarNumber);
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    LogUtils.info("接收到云平台的响应 offset = " + record.offset() + "\t topic = " + record.topic() + "\t key = " + record.key() + "\t value = " + record.value());
                    // 收到响应之后修改数据状态
                    this.messageDataService.updateStatus(record.value()); // TODO 接收到的 value 就是发送出去的 messageId
                }
            }
        }).start();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建消费者
     */
    private KafkaConsumer<String, String> getKafkaConsumer() {
        Properties props = new Properties();
        // 定义kakfa 服务的地址，不需要将所有broker指定上
//        props.put("bootstrap.servers", "wx.parkmas.com:9092");
        props.put("bootstrap.servers", this.getKafkaIpAndPort());
        // 制定consumer groupp
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

//    private String getPatrolCarNumber() {
//        String patrolCarNumber = ReadConfig.readProperties("patrolCarNumber"); // 巡检车车牌号
//        if (patrolCarNumber == null || "".equals(patrolCarNumber.replace(" ", "")) || patrolCarNumber.replace(" ", "").length() < 2) {
//            throw new RuntimeException("严重警告: 巡检车车牌号没有配置, 请检查 config 配置文件 !!!");
//        }
//        return patrolCarNumber.replace(" ", "");
//    }

    private String getKafkaIpAndPort() {
        String kafkaIpPort = ReadConfig.readProperties("kafkaIpPort"); // Kafka 服务地址
        if (kafkaIpPort == null || "".equals(kafkaIpPort.replace(" ", ""))) {
            throw new RuntimeException("严重警告: Kafka 服务地址没有配置, 请检查 config 配置文件 !!!");
        }
        return kafkaIpPort.replace(" ", "");
    }

}
