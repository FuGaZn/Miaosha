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
import java.util.Date;

@Service
public class OperationLogDaoImpl implements OperationLogDao {
    String dir_path = "D:\\Miaosha\\log";

    @Override
    public void saveOperationLog(OperationLog log) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = simpleDateFormat.format(new Date());
        Path path = Paths.get(dir_path);
        try {
            if (!Files.exists(path)){
                Files.createDirectory(path);
            }
            path = Paths.get(dir_path,date+".oplog");
            if (!Files.exists(path)){
                Files.createFile(path);
            }
            BufferedWriter writer = Files.newBufferedWriter
                    (path, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            System.out.println(log.toString());
            writer.write(log.toString());
            writer.write("\n\r");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
