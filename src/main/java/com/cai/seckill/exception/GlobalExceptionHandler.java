package com.cai.seckill.exception;

import com.cai.seckill.common.Constants;
import com.cai.seckill.common.ResponseResult;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	@ExceptionHandler(value=Exception.class)
	public ResponseResult exceptionHandler(HttpServletRequest request, Exception e){
		e.printStackTrace();
		if(e instanceof GlobalException) {
			GlobalException ex = (GlobalException)e;
			return new ResponseResult(ex.getCode(),ex.getMsg());
		}else if(e instanceof BindException) {
			BindException ex = (BindException)e;
			List<ObjectError> errors = ex.getAllErrors();
			ObjectError error = errors.get(0);
			String msg = error.getDefaultMessage();
			return new ResponseResult(Constants.verifyCode_ERROR,msg);
		}else {
			return new ResponseResult(Constants.SERVER_ERROR,"服务器异常");
		}
	}
}
