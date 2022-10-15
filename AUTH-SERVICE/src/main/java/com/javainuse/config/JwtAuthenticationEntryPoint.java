package com.javainuse.config;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
//		System.out.println("there is a problem happened while auth user");
//		System.out.println(request.getHeaderNames().toString());
//		System.out.println(request.getMethod());
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "FORBIDDEN");
	}

}
