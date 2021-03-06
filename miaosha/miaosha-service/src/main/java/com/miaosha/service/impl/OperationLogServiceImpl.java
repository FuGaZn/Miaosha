package com.miaosha.service.impl;

import com.miaosha.dao.OperationLogDao;
import com.miaosha.dao.impl.OperationLogDaoImpl;
import com.miaosha.entity.OperationLog;
import com.miaosha.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImpl implements OperationLogService {
    @Autowired
    OperationLogDaoImpl operationLogDao;

    @Override
    public void saveOperationLog(OperationLog log) {
        operationLogDao.saveOperationLog(log);
    }
}
