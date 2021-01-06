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
}
