package com.cwp.produce.test;

import com.cwp.produce.bean.Message;
import com.cwp.produce.bean.MessageData;
import com.cwp.produce.bean.MessageType;
import com.cwp.produce.utils.JsonUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Kafka 生产者测试类
 * Created by chen_wp on 2019-09-27.
 */
@SuppressWarnings("Duplicates")
public class TestKafkaProducer {

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            new Thread(() -> {
                long threadId = Thread.currentThread().getId();
                System.out.println("发送消息线程 threadId: " + threadId);
                send();
            }).start();
        }
        receiveMessage();
    }

    private static void send() {
        KafkaProducer<String, String> kafkaProducer = getKafkaProducer();

        System.out.println("Kafka生产者 = " + kafkaProducer);

        List<Message> messageList = createMessagesData();
        System.out.println("发送的消息数据 messageList ===> " + JsonUtils.objectToJson(messageList));
        // 发送消息
        for (Message m : messageList) {
            kafkaProducer.send(new ProducerRecord<>("request-BDX8985", m.getMessageId(), JsonUtils.objectToJson(m)), (metadata, exception) -> {
                if (metadata != null) {
                    System.out.println("分区 ==> " + metadata.partition() + "\t offset ==> " + metadata.offset());
                }
                if (exception != null) {
                    System.out.println("发送失败...");
                    exception.printStackTrace();
                }
            });
        }
        kafkaProducer.close();

        System.out.println("文字消息发送完毕... ");

        sendImages("1111111111111", "222222222222222"); // 发送图片
    }

    /**
     * 创建生产者
     */
    private static KafkaProducer<String, String> getKafkaProducer() {
        Properties props = new Properties();
        // Kafka服务端的主机名和端口号
        props.put("bootstrap.servers", "192.168.130.129:9092");
        // 等待所有副本节点的应答
        props.put("acks", "all");
        // 消息发送最大尝试次数
        props.put("retries", 0);
        // 一批消息处理大小
        props.put("batch.size", 20971520);
        props.put("max.request.size", 2097152);
        // 增加服务端请求延时
        props.put("linger.ms", 1);
        // 发送缓存区内存大小
        props.put("buffer.memory", 33554432);
        // key序列化
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // value序列化
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(props);
    }

    private static List<Message> createMessagesData() {
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Message message = new Message();
            message.setMessageId(UUID.randomUUID().toString());
            message.setClientId("BDX8985");
            message.setMessageType(MessageType.REQUEST);
            message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()));
            MessageData m = new MessageData();
            message.setMessageData(m);
            messageList.add(message);
        }
        return messageList;
    }

    /**
     * 发送照片
     *
     * @param batchNumber    批次号
     * @param minBatchNumber 小批号
     */
    private static void sendImages(String batchNumber, String minBatchNumber) {
        System.out.println("开始发送图片消息... batchNumber = " + batchNumber + " minBatchNumber = " + minBatchNumber);

        KafkaProducer<String, byte[]> kafkaProducer = getImageKafkaProducer(); // 发送照片的生产者
        Map<String, byte[]> imageName2imageData = new HashMap<>(); // 将要发送的照片
        List<String> images = getImage("C:\\Users\\Administrator\\Desktop\\发");
        for (String imagePath : images) {
            BufferedImage image;
            OutputStream bOut = null;
            try {
                System.out.println("图片全路径 ===> " + imagePath);
                image = ImageIO.read(new FileInputStream(imagePath));
                bOut = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", bOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] data = ((ByteArrayOutputStream) bOut).toByteArray();
            imageName2imageData.put(imagePath.substring(imagePath.lastIndexOf("\\") + 1), data);
        }
        for (String imageName : imageName2imageData.keySet()) {
            byte[] imageData = imageName2imageData.get(imageName);
            kafkaProducer.send(new ProducerRecord<>("request-image-BDX8985", "粵BDX8985" + "@" + batchNumber + "@" + imageName, imageData), (metadata, exception) -> {
                if (metadata != null) {
                    System.out.println("发送图片的 主题 = " + metadata.topic() + "\t 分区 = " + metadata.partition() + "\t offset ==> " + metadata.offset() + "\t 图片大小 = " + imageData.length + "\t 图片名称 = " + imageName);
                }
                if (exception != null) {
                    exception.printStackTrace();
                }
            });
        }
        kafkaProducer.close();
    }

    private static List<String> getImage(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        List<String> list = new ArrayList<>();
        String system = System.getProperties().getProperty("os.name");
        for (File f : files) {
            if (f.isFile()) {
                if (system.contains("Windows") || system.contains("windows")) {
                    list.add(f.toString());
                }
            }
        }
        System.out.println(JsonUtils.objectToJson(list));
        return list;
    }

    /**
     * 创建发送图片的生产者
     */
    private static KafkaProducer<String, byte[]> getImageKafkaProducer() {
        Properties props = new Properties();
        // Kafka服务端的主机名和端口号
        props.put("bootstrap.servers", "192.168.130.129:9092");
        // 等待所有副本节点的应答
        props.put("acks", "all");
        // 消息发送最大尝试次数
        props.put("retries", 0);
        // 一批消息处理大小
        props.put("batch.size", 20971520);
        props.put("max.request.size", 2097152);
        // 增加服务端请求延时
        props.put("linger.ms", 1);
        // 发送缓存区内存大小
        props.put("buffer.memory", 33554432);
        // key序列化
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // value序列化
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        return new KafkaProducer<>(props);
    }

    /**
     * 接收消息
     */
    private static void receiveMessage() {
        new Thread(() -> {
            long threadId = Thread.currentThread().getId();
            System.out.println("接收消息线程 threadId: " + threadId);
            KafkaConsumer<String, String> consumer = getKafkaConsumer();
            // 消费者订阅的topic, 可同时订阅多个
            consumer.subscribe(Arrays.asList("response-BDX8985"));
            while (true) {
                // 读取数据，读取超时时间为100ms
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("接收到云平台的响应 offset = " + record.offset() + "\t topic = " + record.topic() + "\t key = " + record.key() + "\t value = " + record.value());
                }
            }
        }).start();
    }

    /**
     * 创建消费者
     */
    private static KafkaConsumer<String, String> getKafkaConsumer() {
        Properties props = new Properties();
        // 定义kakfa 服务的地址，不需要将所有broker指定上
        props.put("bootstrap.servers", "192.168.130.129:9092");
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
}
