package com.cai.seckill.exception;


public class GlobalException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private String msg;
	private int code;
	
	public GlobalException(int code,String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
