package com.cwp.produce.bean;

import com.cwp.produce.utils.FormatClassInfo;

/**
 * 发送给云平台的消息结果类
 * Created by chen_wp on 2019-09-18.
 */
public class Message {

    private String messageId; // 消息的唯一标识
    private String clientId; // 客户端id
    private MessageType messageType; // 消息类型(请求/响应)
    private String sendTime; // 发送时间
    private MessageData messageData; // 消息内容

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageData getMessageData() {
        return messageData;
    }

    public void setMessageData(MessageData messageData) {
        this.messageData = messageData;
    }

    @Override
    public String toString() {
        return FormatClassInfo.format(this);
    }
}
