package com.miaosha.uitl;

import java.util.HashMap;
import java.util.Map;

public class Msg {
    int code;
    String message;
    Map data;

    public Msg(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = new HashMap();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
