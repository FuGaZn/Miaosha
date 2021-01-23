package com.miaosha.entity;

public class OperationLog {
    String ip_address; // ip地址
    String workerID; //管理员的工号
    String time; //管理员的操作时间
    String description; //针对管理员操作的描述

    public String getWorkerID() {
        return workerID;
    }

    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "OperationLog{" +
                "ip_address='" + ip_address + '\'' +
                ", workerID='" + workerID + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
