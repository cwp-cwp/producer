package com.cwp.test;

import com.cwp.produce.utils.JsonUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestXxx {

    @Test
    public void test1() {
        String json = "{\n" +
                "    \"clientId\": \"粤B6666\",\n" +
                "    \"messageData\": {\n" +
                "        \"areaName\": \"\",\n" +
                "        \"areaNumber\": \"\",\n" +
                "        \"batchNumber\": \"20190927183322622\",\n" +
                "        \"carNumber\": \"无\",\n" +
                "        \"id\": 502,\n" +
                "        \"messageId\": \"29c5edaa-d02e-4ac0-933e-54f36a48d399\",\n" +
                "        \"minBatchNumber\": \"1177531685445783552\",\n" +
                "        \"parkNumber\": \"10002\",\n" +
                "        \"parkSpaceImages\": [\n" +
                "            {\n" +
                "                \"batchNumber\": \"20190927183322622\",\n" +
                "                \"frequency\": \"ONE\",\n" +
                "                \"image\": \"7/28000_85000_180000_1569580403180_32_0_0_0_0_无_后_2.jpg\",\n" +
                "                \"imagePost\": \"32\",\n" +
                "                \"parkNumber\": \"10002\",\n" +
                "                \"time\": \"2019-09-27 18:33:23 180\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"batchNumber\": \"20190927183322622\",\n" +
                "                \"frequency\": \"ONE\",\n" +
                "                \"image\": \"7/28000_85000_180000_1569580402612_32_0_0_0_0_无_后_2.jpg\",\n" +
                "                \"imagePost\": \"32\",\n" +
                "                \"parkNumber\": \"10002\",\n" +
                "                \"time\": \"2019-09-27 18:33:22 612\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"batchNumber\": \"20190927183322622\",\n" +
                "                \"frequency\": \"ONE\",\n" +
                "                \"image\": \"7/28000_85000_180000_1569580403007_32_0_0_0_0_无_后_2.jpg\",\n" +
                "                \"imagePost\": \"32\",\n" +
                "                \"parkNumber\": \"10002\",\n" +
                "                \"time\": \"2019-09-27 18:33:23 007\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"batchNumber\": \"20190927183322622\",\n" +
                "                \"frequency\": \"ONE\",\n" +
                "                \"image\": \"7/28000_85000_180000_1569580404904_32_0_0_0_0_无_后_2.jpg\",\n" +
                "                \"imagePost\": \"32\",\n" +
                "                \"parkNumber\": \"10002\",\n" +
                "                \"time\": \"2019-09-27 18:33:24 904\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"batchNumber\": \"20190927183322622\",\n" +
                "                \"frequency\": \"ONE\",\n" +
                "                \"image\": \"7/28000_85000_180000_1569580403409_32_0_0_0_0_无_后_2.jpg\",\n" +
                "                \"imagePost\": \"32\",\n" +
                "                \"parkNumber\": \"10002\",\n" +
                "                \"time\": \"2019-09-27 18:33:23 409\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"batchNumber\": \"20190927183322622\",\n" +
                "                \"frequency\": \"ONE\",\n" +
                "                \"image\": \"7/28000_85000_180000_1569580404670_32_0_0_0_0_无_后_2.jpg\",\n" +
                "                \"imagePost\": \"32\",\n" +
                "                \"parkNumber\": \"10002\",\n" +
                "                \"time\": \"2019-09-27 18:33:24 670\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"batchNumber\": \"20190927183322622\",\n" +
                "                \"frequency\": \"ONE\",\n" +
                "                \"image\": \"7/28000_85000_180000_1569580404246_32_0_0_0_0_无_后_2.jpg\",\n" +
                "                \"imagePost\": \"32\",\n" +
                "                \"parkNumber\": \"10002\",\n" +
                "                \"time\": \"2019-09-27 18:33:24 246\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"batchNumber\": \"20190927183322622\",\n" +
                "                \"frequency\": \"ONE\",\n" +
                "                \"image\": \"7/28000_85000_180000_1569580403632_32_0_0_0_0_无_后_2.jpg\",\n" +
                "                \"imagePost\": \"32\",\n" +
                "                \"parkNumber\": \"10002\",\n" +
                "                \"time\": \"2019-09-27 18:33:23 632\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"batchNumber\": \"20190927183322622\",\n" +
                "                \"frequency\": \"ONE\",\n" +
                "                \"image\": \"7/28000_85000_180000_1569580403864_32_0_0_0_0_无_后_2.jpg\",\n" +
                "                \"imagePost\": \"32\",\n" +
                "                \"parkNumber\": \"10002\",\n" +
                "                \"time\": \"2019-09-27 18:33:23 864\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"batchNumber\": \"20190927183322622\",\n" +
                "                \"frequency\": \"ONE\",\n" +
                "                \"image\": \"7/28000_85000_180000_1569580405134_32_0_0_0_0_无_后_2.jpg\",\n" +
                "                \"imagePost\": \"32\",\n" +
                "                \"parkNumber\": \"10002\",\n" +
                "                \"time\": \"2019-09-27 18:33:25 134\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"batchNumber\": \"20190927183322622\",\n" +
                "                \"frequency\": \"ONE\",\n" +
                "                \"image\": \"7/28000_85000_180000_1569580404503_32_0_0_0_0_无_后_2.jpg\",\n" +
                "                \"imagePost\": \"32\",\n" +
                "                \"parkNumber\": \"10002\",\n" +
                "                \"time\": \"2019-09-27 18:33:24 503\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"batchNumber\": \"20190927183322622\",\n" +
                "                \"frequency\": \"ONE\",\n" +
                "                \"image\": \"7/28000_85000_180000_1569580404096_32_0_0_0_0_无_后_2.jpg\",\n" +
                "                \"imagePost\": \"32\",\n" +
                "                \"parkNumber\": \"10002\",\n" +
                "                \"time\": \"2019-09-27 18:33:24 096\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"batchNumber\": \"20190927183322622\",\n" +
                "                \"frequency\": \"ONE\",\n" +
                "                \"image\": \"7/28000_85000_180000_1569580402778_32_0_0_0_0_无_后_2.jpg\",\n" +
                "                \"imagePost\": \"32\",\n" +
                "                \"parkNumber\": \"10002\",\n" +
                "                \"time\": \"2019-09-27 18:33:22 778\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"parkStatus\": \"\",\n" +
                "        \"patrolCarId\": \"B6666\",\n" +
                "        \"patrolCarNumber\": \"粤B6666\",\n" +
                "        \"photographTime\": \"2019-09-27 18:33:23 180\",\n" +
                "        \"pushStatus\": \"\",\n" +
                "        \"recordTime\": \"2019-09-27 18:33:28\",\n" +
                "        \"sendStatus\": \"UNSENT\",\n" +
                "        \"type\": \"NORMAL\"\n" +
                "    },\n" +
                "    \"messageId\": \"29c5edaa-d02e-4ac0-933e-54f36a48d399\",\n" +
                "    \"messageType\": \"REQUEST\",\n" +
                "    \"sendTime\": \"2019-09-27 18:33:37 872\"\n" +
                "}";

        Object o = JsonUtils.jsonToPojo(json, Object.class);

        System.out.println(o);
    }


    @Test
    public void test2() {
        // 1572157935688
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date(1572157935688L));
        System.out.println(time); // 2019-10-27 14:32:15 688

        // 2019-10-28 16:33:30 890
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date(1572251610890L)));
    }

    @Test
    public void test3() {
        while (true) {
            System.err.println(11111111);
            return;
        }
    }

}
