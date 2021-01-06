package com.miaosha.uitl.vo;

public class CouponOrderVO {
    int cid;
    int uid;

    public CouponOrderVO() {
    }

    public CouponOrderVO(int cid, int uid) {
        this.cid = cid;
        this.uid = uid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
