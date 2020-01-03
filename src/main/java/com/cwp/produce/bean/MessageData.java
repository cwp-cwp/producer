package com.cwp.produce.bean;

import com.cwp.produce.utils.FormatClassInfo;

import java.util.List;

/**
 * 发送给云平台的消息实体类
 * Created by chen_wp on 2019-09-18.
 */
public class MessageData {

    private int id; // 主键自增
    private String messageId; // 消息唯一标识
    private String batchNumber; // 巡检批次号(对于云平台没有用，为了方便核对数据加入批次号，根据批次号和车位号可以定位到具体某一条数据)
    private String minBatchNumber; // 小批次号(为了提高效率，以小批次数据为单位发送给云平台)
    private String patrolCarId; // 巡检车编号
    private String patrolCarNumber; // 巡检车车牌号
    private String areaNumber; // 区域编号
    private String areaName; // 区域名称
    private String parkNumber; // 车位号
    private String parkStatus; // 车位状态
    private String carNumber; // 车牌号
    private String photographTime; // 拍照时间
    private String pushStatus; // 推送状态
    private String pushTime; // 推送时间
    private SendStatus sendStatus; // 发送状态
    private ResultType type; // 是否可疑结果
    private String recordTime; // 系统调用时间
    private String newCarNumber; // 修改后的车牌号
    private String panorama; // 全景图

    private List<ParkSpaceImages> parkSpaceImages; // 图片

    public MessageData() {
    }

    public MessageData(String messageId, String batchNumber, String minBatchNumber, String patrolCarId, String patrolCarNumber, String areaNumber, String areaName, String parkNumber, String parkStatus, String carNumber, String photographTime, String pushStatus, String pushTime, SendStatus sendStatus, ResultType type, String recordTime) {
        this.messageId = messageId;
        this.batchNumber = batchNumber;
        this.minBatchNumber = minBatchNumber;
        this.patrolCarId = patrolCarId;
        this.patrolCarNumber = patrolCarNumber;
        this.areaNumber = areaNumber;
        this.areaName = areaName;
        this.parkNumber = parkNumber;
        this.parkStatus = parkStatus;
        this.carNumber = carNumber;
        this.photographTime = photographTime;
        this.pushStatus = pushStatus;
        this.pushTime = pushTime;
        this.sendStatus = sendStatus;
        this.type = type;
        this.recordTime = recordTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMinBatchNumber() {
        return minBatchNumber;
    }

    public void setMinBatchNumber(String minBatchNumber) {
        this.minBatchNumber = minBatchNumber;
    }

    public SendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getPatrolCarId() {
        return patrolCarId;
    }

    public void setPatrolCarId(String patrolCarId) {
        this.patrolCarId = patrolCarId;
    }

    public String getPatrolCarNumber() {
        return patrolCarNumber;
    }

    public void setPatrolCarNumber(String patrolCarNumber) {
        this.patrolCarNumber = patrolCarNumber;
    }

    public String getAreaNumber() {
        return areaNumber;
    }

    public void setAreaNumber(String areaNumber) {
        this.areaNumber = areaNumber;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getParkNumber() {
        return parkNumber;
    }

    public void setParkNumber(String parkNumber) {
        this.parkNumber = parkNumber;
    }

    public String getParkStatus() {
        return parkStatus;
    }

    public void setParkStatus(String parkStatus) {
        this.parkStatus = parkStatus;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getPhotographTime() {
        return photographTime;
    }

    public void setPhotographTime(String photographTime) {
        this.photographTime = photographTime;
    }

    public String getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(String pushStatus) {
        this.pushStatus = pushStatus;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public List<ParkSpaceImages> getParkSpaceImages() {
        return parkSpaceImages;
    }

    public void setParkSpaceImages(List<ParkSpaceImages> parkSpaceImages) {
        this.parkSpaceImages = parkSpaceImages;
    }

    public ResultType getType() {
        return type;
    }

    public void setType(ResultType type) {
        this.type = type;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getNewCarNumber() {
        return newCarNumber;
    }

    public void setNewCarNumber(String newCarNumber) {
        this.newCarNumber = newCarNumber;
    }

    public String getPanorama() {
        return panorama;
    }

    public void setPanorama(String panorama) {
        this.panorama = panorama;
    }

    @Override
    public String toString() {
        return FormatClassInfo.format(this);
    }
}
