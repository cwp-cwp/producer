package com.cwp.produce.bean;

/**
 * 发送数据给云平台的状态枚举
 */
public enum SendStatus {
    SUCCESS, // 发送成功并且接收到响应
    FAIL, // 发送失败
    WARN, // 连续发送三次失败
    UNSENT, // 未发送
    SENT // 已经发送，但还没收到响应
}
