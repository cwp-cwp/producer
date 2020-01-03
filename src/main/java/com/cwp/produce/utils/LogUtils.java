package com.cwp.produce.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("Duplicates")
public class LogUtils {

    private static final String produceLogName = "produce";
    private static final String allImageLogName = "allImage";
    private static final String imageLogName = "image";

    private static FileOutputStream produceLogFileOutputStream;
    private static FileOutputStream allImageLogFileOutputStream;
    private static FileOutputStream imageLogFileOutputStream;

    static {
        produceLogFileOutputStream = getLogFileOutputStream(produceLogName);
        allImageLogFileOutputStream = getLogFileOutputStream(allImageLogName);
        imageLogFileOutputStream = getLogFileOutputStream(imageLogName);
    }

    private static FileOutputStream getLogFileOutputStream(String logName) {
        FileOutputStream logFileOutputStream = null;
        try {
            String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).replace("-", "");
            File file = new File("/var/log/produce", fileName);
            if (!file.exists()) {
                file.mkdirs();
            }
            logFileOutputStream = new FileOutputStream(file.getPath() + "/" + logName + ".log", true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return logFileOutputStream;
    }

    /**
     * 记录日志
     */
    public static void info(String log) {
        try {
            log = "[ " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()) + " ] \t" + log;
            if (produceLogFileOutputStream == null) {
                produceLogFileOutputStream = getLogFileOutputStream(produceLogName);
            }
            produceLogFileOutputStream.write(log.getBytes());
            produceLogFileOutputStream.write("\n".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void info2(String log) {
        try {
            log = "[ " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()) + " ] \t" + log;
            if (allImageLogFileOutputStream == null) {
                allImageLogFileOutputStream = getLogFileOutputStream(allImageLogName);
            }
            allImageLogFileOutputStream.write(log.getBytes());
            allImageLogFileOutputStream.write("\n".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void info3(String log) {
        try {
            log = "[ " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()) + " ] \t" + log;
            if (imageLogFileOutputStream == null) {
                imageLogFileOutputStream = getLogFileOutputStream(imageLogName);
            }
            imageLogFileOutputStream.write(log.getBytes());
            imageLogFileOutputStream.write("\n".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
