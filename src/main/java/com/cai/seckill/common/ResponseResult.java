package com.cai.seckill.common;

public class ResponseResult<T> {

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private T data;

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(int code, String msg, T data) {

        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult(int code) {
        this.code = code;
    }


    public ResponseResult(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
