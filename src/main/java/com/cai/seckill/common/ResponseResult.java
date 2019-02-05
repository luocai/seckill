package com.cai.seckill.common;

public class ResponseResult<T> {

    private int resultCode;
    private String msg;
    private T data;

    public ResponseResult(int resultCode, String msg) {
        this.resultCode = resultCode;
        this.msg = msg;
    }

    public ResponseResult(int resultCode, String msg, T data) {

        this.resultCode = resultCode;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult(int resultCode) {
        this.resultCode = resultCode;
    }



}
