package com.cwp.produce.kafka;

import com.cwp.produce.bean.*;
import com.cwp.produce.service.MessageDataService;
import com.cwp.produce.utils.JsonUtils;
import com.cwp.produce.utils.LogUtils;
import com.cwp.produce.utils.ReadConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 发送消息给云平台
 * Created by chen_wp on 2019-09-18.
 */
@SuppressWarnings("Duplicates")
@Component
public class SendMessage implements CommandLineRunner {

    private final static Logger LOG = LoggerFactory.getLogger(SendMessage.class);

    private String patrolCarNumber = null; // 巡检车车牌号

    private boolean jsonConnect = true; // 发送文字的 Kafka 是否连接成功
    private boolean imageConnect = true; // 发送图片的 Kafka 是否连接成功

    @Autowired
    private MessageDataService messageDataService;

    @Override
    public void run(String... args) {
        new Thread(() -> {
            long threadId = Thread.currentThread().getId();
            LogUtils.info("发送文字消息线程 threadId: " + threadId);
            KafkaProducer<String, String> kafkaProducer = null;
            while (jsonConnect) {
                try {
                    kafkaProducer = this.getKafkaProducer();
                    jsonConnect = false;
                } catch (Exception e) {
                    LogUtils.info("Kafka 文字生产者创建失败 " + e.toString());
                    e.printStackTrace();
                    this.sleep(3000);
                }
            }
            while (true) {
                try {
                    LogUtils.info("send() 。。。########################################");
                    List<MessageData> messageDataList = this.messageDataService.getByTimeAndStatusIsUnSend(this.getDateTime(-1), this.getDateTime(1));
                    LogUtils.info("数据库中查到的 messageDataList.size() = " + messageDataList.size());
                    if (messageDataList == null || messageDataList.size() == 0) {
                        Thread.sleep(5000);
                        continue;
                    }
                    this.setPatrolCarNumber(messageDataList.get(0).getPatrolCarNumber()); // 设置一下巡检车车牌号，作为主题使用
                    // 发送文字消息
                    List<Message> messageData = this.createMessageData(messageDataList);
                    this.sendJson(kafkaProducer, messageData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            long threadId = Thread.currentThread().getId();
            LogUtils.info3("发送图片消息线程 threadId: " + threadId);
            KafkaProducer<String, byte[]> kafkaImageProducer = null;
            while (imageConnect) {
                try {
                    kafkaImageProducer = this.getImageKafkaProducer();
                    imageConnect = false;
                } catch (Exception e) {
                    LogUtils.info3("Kafka 图片生产者创建失败 " + e.toString());
                    e.printStackTrace();
                    this.sleep(3000);
                }
            }
            while (true) {
                try {
                    LogUtils.info3("sendImage() 。。。!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    List<MessageData> unSendImageMessageData = this.messageDataService.getUnSendImageByTime(this.getDateTime(-1), this.getDateTime(1));
                    LogUtils.info3("数据库中查到的 unSendImageMessageData.size() = " + unSendImageMessageData.size());
                    if (unSendImageMessageData == null || unSendImageMessageData.size() == 0) {
                        Thread.sleep(5000);
                        continue;
                    }
                    Map<String, byte[]> imageName2UnSendImageData = this.createImageMessageData(unSendImageMessageData);
                    this.sendImage(kafkaImageProducer, imageName2UnSendImageData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        new Thread(() -> {
//            long threadId = Thread.currentThread().getId();
//            LogUtils.info2("发送全部图片消息线程 threadId: " + threadId);
//            KafkaProducer<String, byte[]> kafkaImageProducer = this.getImageKafkaProducer();
//            while (true) {
//                try {
//                    LogUtils.info2("sendAllImage() 。。。$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//                    List<ParkRecord> unSendAllImageByTime = this.messageDataService.getUnSendAllImageByTime(this.getDateTime(-1), this.getDateTime(1));
//                    LogUtils.info2("数据库中查到的 unSendAllImageByTime.size() = " + unSendAllImageByTime.size());
//                    if (unSendAllImageByTime == null || unSendAllImageByTime.size() == 0) {
//                        Thread.sleep(5000);
//                        continue;
//                    }
//                    Map<String, byte[]> imageName2UnSendImageData = this.createAllImageData(unSendAllImageByTime);
//                    this.sendAllImage(kafkaImageProducer, imageName2UnSendImageData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送摄像头拍到的全部的图片
     *
     * @param kafkaImageProducer        Kafka图片生产者
     * @param imageName2UnSendImageData 图片数据
     */
    private void sendAllImage(KafkaProducer<String, byte[]> kafkaImageProducer, Map<String, byte[]> imageName2UnSendImageData) {
        for (String imageName : imageName2UnSendImageData.keySet()) {
            byte[] imageData = imageName2UnSendImageData.get(imageName);
            LogUtils.info2("图片发送中... imageName = " + imageName + " imageData.length = " + imageData.length);
            // TODO 约定好的 topic: request-allImage-B6666    key: B6666@20190921194334007@-2687_84422_181935_1568634238676_35_0_0_0_0_无_前_1.jpg
            String[] split = imageName.split("@");
            if (split.length < 3) {
                continue;
            }
            kafkaImageProducer.send(new ProducerRecord<>("request-allImage-" + split[0], imageName, imageData), (metadata, exception) -> {
                if (metadata != null) {
                    LogUtils.info2("发送图片的 主题 = " + metadata.topic() + "\t 分区 = " + metadata.partition() + "\t offset ==> " + metadata.offset() + "\t 图片大小 = " + imageData.length + "\t 图片名称 = " + imageName);
                    // 更新图片状态为 SEND
                    this.messageDataService.updateAllImageSendStatus(split[2], String.valueOf(SendStatus.SENT));
                }
                if (exception != null) {
                    exception.printStackTrace();
                }
            });
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 准备图片数据
     *
     * @param unSendAllImageByTime 全部拍摄到的图片
     */
    private Map<String, byte[]> createAllImageData(List<ParkRecord> unSendAllImageByTime) {
        Map<String, byte[]> imageName2imageData = new HashMap<>();
        String path = this.getImagePath();
        String imagePath; // 照片路径
        BufferedImage bufferedImage;
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        for (ParkRecord p : unSendAllImageByTime) {
            String imageName = p.getImageHost(); // 照片名
            LogUtils.info2("图片准备中... imageName = " + imageName);
            try {
                imagePath = path + "/" + p.getBatchNumber() + "/";
                LogUtils.info2("图片全路径 ===> " + imagePath + imageName);
                fileInputStream = new FileInputStream(imagePath + imageName);
                bufferedImage = ImageIO.read(fileInputStream);
                outputStream = new ByteArrayOutputStream();
                if (bufferedImage != null && outputStream != null) {
                    ImageIO.write(bufferedImage, "jpg", outputStream);
                }
                if (outputStream != null) {
                    byte[] data = ((ByteArrayOutputStream) outputStream).toByteArray();
                    while (true) {
                        if (patrolCarNumber != null) {
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // TODO 约定好 key:  B6666@20190921194334007@-2687_84422_181935_1568634238676_35_0_0_0_0_无_前_1.jpg
                    imageName2imageData.put(patrolCarNumber + "@" + p.getBatchNumber() + "@" + imageName, data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.close(fileInputStream, outputStream);
            }
        }
        LogUtils.info2("准备好的全部图片数据 imageName2imageData.size() = " + imageName2imageData.size());
        return imageName2imageData;
    }


    /**
     * 发送文字
     *
     * @param kafkaProducer kafka 生产者
     * @param messageData   要发送的文字数据
     */
    private void sendJson(KafkaProducer<String, String> kafkaProducer, List<Message> messageData) {
        for (Message m : messageData) {
            LogUtils.info("文字发送中... ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            // TODO 约定好的 topic: request-B6666
            kafkaProducer.send(new ProducerRecord<>("request-" + m.getMessageData().getPatrolCarNumber(), m.getMessageId(), JsonUtils.objectToJson(m)), (metadata, exception) -> {
                if (metadata != null) {
                    LogUtils.info("分区 ==> " + metadata.partition() + "\t offset ==> " + metadata.offset());
                    // 发送成功(但还未收到响应)就修改数据库状态为 SEND
                    this.messageDataService.updateStatusSend(m.getMessageId());
                }
                if (exception != null) {
                    exception.printStackTrace();
                }
            });
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送图片
     *
     * @param kafkaImageProducer  kafka 图片生产者
     * @param imageName2imageData 图片数据 map 集合
     */
    private void sendImage(KafkaProducer<String, byte[]> kafkaImageProducer, Map<String, byte[]> imageName2imageData) {
        for (String imageName : imageName2imageData.keySet()) {
            byte[] imageData = imageName2imageData.get(imageName);
            LogUtils.info3("图片发送中... imageName = " + imageName + " imageData.length = " + imageData.length);
            // TODO 约定好的 topic: request-image-B6666    key: B6666@20190921194334007@-2687_84422_181935_1568634238676_35_0_0_0_0_无_前_1.jpg
            String[] split = imageName.split("@");
            if (split.length < 3) {
                continue;
            }
            kafkaImageProducer.send(new ProducerRecord<>("request-image-" + split[0], imageName, imageData), (metadata, exception) -> {
                if (metadata != null) {
                    LogUtils.info3("发送图片的 主题 = " + metadata.topic() + "\t 分区 = " + metadata.partition() + "\t offset ==> " + metadata.offset() + "\t 图片大小 = " + imageData.length + "\t 图片名称 = " + imageName);
                    // 更新图片状态为 SEND
                    this.messageDataService.updateImageSendStatus(split[2]);
                }
                if (exception != null) {
                    exception.printStackTrace();
                }
            });
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取要发送的文字消息
     *
     * @param messageDataList 从数据库中查询出来的消息数据
     * @return 返回将要发送的消息数据
     */
    private List<Message> createMessageData(List<MessageData> messageDataList) {
        List<Message> messageList = new ArrayList<>();
        for (MessageData m : messageDataList) {
            Message message = new Message();
            message.setMessageId(m.getMessageId());
            message.setClientId(m.getPatrolCarNumber());
            message.setMessageType(MessageType.REQUEST);
            message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()));
            message.setMessageData(m);
            messageList.add(message);
        }
        return messageList;
    }


    /**
     * 获取要发送的图片数据
     *
     * @param messageDataList 从数据库中查询出来的消息数据
     * @return 返回将要发送的消息数据
     */
    private Map<String, byte[]> createImageMessageData(List<MessageData> messageDataList) {
        Map<String, byte[]> imageName2imageData = new HashMap<>();
        String path = this.getImagePath();
        String imagePath; // 照片路径
        BufferedImage bufferedImage;
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        for (MessageData m : messageDataList) {
            for (ParkSpaceImages parkSpaceImages : m.getParkSpaceImages()) {
                String imageName = parkSpaceImages.getImage(); // 照片名
                LogUtils.info3("图片准备中... imageName = " + imageName);
                try {
                    imagePath = path + "/" + m.getBatchNumber() + "/";
                    LogUtils.info3("图片全路径 ===> " + imagePath + imageName);
                    fileInputStream = new FileInputStream(imagePath + imageName);
                    bufferedImage = ImageIO.read(fileInputStream);
                    outputStream = new ByteArrayOutputStream();
                    if (bufferedImage != null && outputStream != null) {
                        ImageIO.write(bufferedImage, "jpg", outputStream);
                    }
                    if (outputStream != null) {
                        byte[] data = ((ByteArrayOutputStream) outputStream).toByteArray();
                        // TODO 约定好 key:  B6666@20190921194334007@-2687_84422_181935_1568634238676_35_0_0_0_0_无_前_1.jpg
                        imageName2imageData.put(m.getPatrolCarNumber() + "@" + m.getBatchNumber() + "@" + imageName, data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    this.close(fileInputStream, outputStream);
                }
            }
        }
        LogUtils.info3("准备好的图片数据 imageName2imageData.size() = " + imageName2imageData.size());
        return imageName2imageData;
    }

    private void close(FileInputStream fileInputStream, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前时间的前后时间
     *
     * @param day 前(后) day 天
     */
    private String getDateTime(int day) {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, day);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = formatter.format(date);
        return dateTime;
    }

    /**
     * 创建生产者
     */
    private KafkaProducer<String, String> getKafkaProducer() {
        Properties props = new Properties();
        // Kafka服务端的主机名和端口号
//        props.put("bootstrap.servers", "wx.parkmas.com:9092");
        props.put("bootstrap.servers", getKafkaIpAndPort());
        // 等待所有副本节点的应答
        props.put("acks", "all");
        // 消息发送最大尝试次数
        props.put("retries", 0);
        // 一批消息处理大小
        props.put("batch.size", 20971520); // 16384
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

    /**
     * 创建发送图片的生产者
     */
    private KafkaProducer<String, byte[]> getImageKafkaProducer() {
        Properties props = new Properties();
        // Kafka服务端的主机名和端口号
//        props.put("bootstrap.servers", "wx.parkmas.com:9092");
        props.put("bootstrap.servers", getKafkaIpAndPort());
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

    private static String getKafkaIpAndPort() {
        String kafkaIpPort = ReadConfig.readProperties("kafkaIpPort"); // Kafka 服务地址
        if (kafkaIpPort == null || "".equals(kafkaIpPort.replace(" ", ""))) {
            throw new RuntimeException("严重警告: Kafka 服务地址没有配置, 请检查 config.properties 配置文件 !!!");
        }
        return kafkaIpPort.replace(" ", "");
    }

    private String getImagePath() {
        String patrolCarNumber = ReadConfig.readProperties("imagePath"); // 要发送的图片路径
        if (patrolCarNumber == null || "".equals(patrolCarNumber.replace(" ", "")) || patrolCarNumber.replace(" ", "").length() < 2) {
            throw new RuntimeException("严重警告: 图片路径没有配置, 请检查 config.properties 配置文件 !!!");
        }
        return patrolCarNumber.replace(" ", "");
    }

    public String getPatrolCarNumber() {
        return patrolCarNumber;
    }

    public void setPatrolCarNumber(String patrolCarNumber) {
        this.patrolCarNumber = patrolCarNumber;
    }

}
