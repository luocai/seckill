package com.cai.seckill.config;

import com.cai.seckill.access.UserContext;
import com.cai.seckill.pojo.User;
import com.cai.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/*
自定义参数解析器
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	UserService userService;
	
	public boolean supportsParameter(MethodParameter parameter) {
		// 指定参数如果参数类型是User，则使用该解析器。
		// 如果直接返回true，则代表将此解析器用于所有参数

		Class<?> clazz = parameter.getParameterType();
		return clazz==User.class;
	}

	/**
	 * 将request中的请求参数解析到当前Controller参数上
	 * @param parameter 需要被解析的Controller参数，此参数必须首先传给{@link #supportsParameter}并返回true
	 * @param mavContainer 当前request的ModelAndViewContainer
	 * @param webRequest 当前request
	 * @param binderFactory 生成{@link WebDataBinder}实例的工厂
	 * @return 解析后的Controller参数
	 */
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		return UserContext.getUser();
	}

}
