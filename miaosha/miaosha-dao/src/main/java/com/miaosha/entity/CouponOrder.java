package com.miaosha.entity;

import javax.persistence.*;

@Entity
@Table
public class CouponOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int rid;
    @Column
    int uid;
    @Column
    int cid;
    @Column
    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
