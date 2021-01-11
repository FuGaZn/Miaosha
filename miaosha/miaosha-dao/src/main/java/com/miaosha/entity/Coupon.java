package com.miaosha.entity;

import javax.persistence.*;

@Entity
@Table
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int cid;

    @Column
    String description;

    @Column
    int total;

    @Column
    int sale;

    //发布时间
    @Column
    String publishTime;

    @Column
    int able;

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getAble() {
        return able;
    }

    public void setAble(int able) {
        this.able = able;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "cid=" + cid +
                ", description='" + description + '\'' +
                ", total=" + total +
                ", sale=" + sale +
                ", publishTime='" + publishTime + '\'' +
                ", able=" + able +
                '}';
    }
}
