package com.cwp.produce.service;

import com.cwp.produce.bean.MessageData;
import com.cwp.produce.bean.ParkRecord;
import com.cwp.produce.mapper.MessageDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 发送数据到云平台的业务逻辑层
 * Created by chen_wp on 2019-09-18.
 */
@Component
@Transactional
public class MessageDataService {

    @Autowired
    private MessageDataMapper messageDataMapper;

    public void updateStatus(String messageId) {
        this.messageDataMapper.updateStatus(messageId);
    }

    public void updateStatusSend(String messageId) {
        this.messageDataMapper.updateStatusSend(messageId);
    }

    public List<MessageData> getByTimeAndStatusIsUnSend(String startTime, String endTime) {
        return this.messageDataMapper.getByTimeAndStatusIsUnSend(startTime, endTime);
    }

    public List<MessageData> getUnSendImageByTime(String startTime, String endTime) {
        return this.messageDataMapper.getUnSendImageByTime(startTime, endTime);
    }

    public void updateImageSendStatus(String image) {
        this.messageDataMapper.updateImageSendStatus(image);
    }

    public List<ParkRecord> getUnSendAllImageByTime(String startTime, String endTime) {
        return this.messageDataMapper.getUnSendAllImageByTime(startTime, endTime);
    }

    public void updateAllImageSendStatus(String imageName, String sendStatus) {
        this.messageDataMapper.updateAllImageSendStatus(imageName, sendStatus);
    }
}
