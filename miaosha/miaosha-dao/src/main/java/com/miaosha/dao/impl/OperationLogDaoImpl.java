package com.miaosha.dao.impl;

import com.miaosha.dao.OperationLogDao;
import com.miaosha.entity.OperationLog;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;

@Service
public class OperationLogDaoImpl implements OperationLogDao {
    String dir_path = "D:\\Miaosha\\log";

    /**
     * 向文件系统写入操作日志
     * 操作日志内容：ip、用户id、操作时间、操作内容
     * 为每天都创建一个日志文件，该文件保存该天所有的操作内容
     * @param log
     */
    @Override
    public void saveOperationLog(OperationLog log) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = simpleDateFormat.format(Calendar.getInstance());
        Path path = Paths.get(dir_path,date,".oplog");
        try {
            if (!Files.exists(path)){
                Files.createFile(path);
            }
            BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            writer.write(log.toString());
            writer.write("\n\r");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
