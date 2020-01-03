package com.cwp.produce.bean;

import com.cwp.produce.utils.FormatClassInfo;

/**
 * 停车记录实体类
 * Created by chen_wp on 2019-08-03.
 */
public class ParkRecord {

    private Integer id; // 主键自增
    private String batchNumber; // 巡检批次（这里的批次只用于在分析时区分其他批次的记录）
    private String parkNumber; // 车位号
    private String carNumber; // 车牌号
    private String recordTime; // 拍摄记录时间
    private String imageHost; //图片
    private String systemTime; // 系统记录时间
    private SendStatus sendStatus; // 记录是否已经发送到云平台

    public ParkRecord() {
    }

    public ParkRecord(String batchNumber, String parkNumber, String carNumber, String recordTime, String imageHost, String systemTime, SendStatus sendStatus) {
        this.batchNumber = batchNumber;
        this.parkNumber = parkNumber;
        this.carNumber = carNumber;
        this.recordTime = recordTime;
        this.imageHost = imageHost;
        this.systemTime = systemTime;
        this.sendStatus = sendStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getParkNumber() {
        return parkNumber;
    }

    public void setParkNumber(String parkNumber) {
        this.parkNumber = parkNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public String getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(String systemTime) {
        this.systemTime = systemTime;
    }

    public SendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    @Override
    public String toString() {
        return FormatClassInfo.format(this);
    }
}
