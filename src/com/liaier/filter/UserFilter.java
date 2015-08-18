package com.liaier.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import session.TUserInfo;

public class UserFilter implements Filter,HttpSessionListener  {

    public UserFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		 HttpServletRequest pRequest   = (HttpServletRequest) request;
		 String parameter = pRequest.getParameter("parameter");
		 if(parameter==null || "".equals(parameter)){
			 parameter = "{\"role\":\"user\"}";
		 }else{
			 parameter = parameter.substring(0, parameter.length()-1);
			 parameter = parameter+",\"role\":\"user\"}";
		 }
		 
		 pRequest.setAttribute("parameter", parameter);
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

	public void sessionCreated(HttpSessionEvent arg0) {
		
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		String sessionId = arg0.getSession().getId();
		TUserInfo.removeUserId(sessionId);
	}

}
