package com.cwp.produce.mapper;

import com.cwp.produce.bean.MessageData;
import com.cwp.produce.bean.ParkRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 发送给云平台实体类数据库操作接口
 * Created by chen_wp on 2019-09-18.
 */
@Repository
public interface MessageDataMapper {

    void updateStatus(@Param("messageId") String messageId);

    void updateStatusSend(@Param("messageId") String messageId);

    List<MessageData> getByTimeAndStatusIsUnSend(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<MessageData> getUnSendImageByTime(@Param("startTime") String startTime, @Param("endTime") String endTime);

    void updateImageSendStatus(@Param("image") String image);

    List<ParkRecord> getUnSendAllImageByTime(@Param("startTime") String startTime, @Param("endTime") String endTime);

    void updateAllImageSendStatus(@Param("imageName") String imageName, @Param("sendStatus") String sendStatus);
}
