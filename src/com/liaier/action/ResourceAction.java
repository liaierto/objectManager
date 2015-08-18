package com.liaier.action;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.util.Utils;

/**
 * Servlet implementation class ResourceAction
 */
public class ResourceAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(ResourceAction.class);
    public ResourceAction() {
	        super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	    doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        response.setCharacterEncoding("utf-8");

        if (contextPath == null) { // root context
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(contextPath.length() + servletPath.length());

//        if ("".equals(path)) {
//            if (contextPath.equals("") || contextPath.equals("/")) {
//                response.sendRedirect("/object/main.html");
//            } else {
//                response.sendRedirect("object/main.html");
//            }
//            return;
//        }
//
//        if ("/".equals(path)) {
//            response.sendRedirect("main.html");
//            return;
//        }


        // find file in resources path
	    returnResourceFile(path, uri, response);
	 }
	 protected void returnResourceFile(String fileName, String uri, HttpServletResponse response) throws ServletException, IOException {

		String filePath = getFilePath(fileName);
		if (fileName.endsWith(".jpg") || fileName.endsWith(".gif")|| fileName.endsWith(".png")) {
		byte[] bytes = readByteArrayFromResource(filePath);
		if (bytes != null) {
		    response.getOutputStream().write(bytes);
		}
		return;
		}
		
		String text = readFromResource(filePath);
		if (text == null) {
		    response.sendRedirect(uri + "/main.html");
		    return;
		}
		if (fileName.endsWith(".css")) {
		    response.setContentType("text/css;charset=utf-8");
		} else if (fileName.endsWith(".js")) {
		    response.setContentType("text/javascript;charset=utf-8");
		}
		response.getWriter().write(text);
    }
	 
	 protected String getFilePath(String fileName) {
		 URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
	        String pPath = url.toString();
	        int index = pPath.indexOf("WEB-INF");  
	          
	        if(index != -1){  
	            index = pPath.indexOf("classes");  
	        }  
	          
	          
	        pPath = pPath.substring(6, index+7);  
	        if(fileName.startsWith("/")){
	        	fileName = fileName.substring(fileName.indexOf("/")+1, fileName.length());
	        }
	        log.info(pPath);
	        return pPath+"com/liaier/http/" + fileName;
	    }
	 
	 
	 public  String readFromResource(String resource) throws IOException {
	        InputStream in = null;
	        try {
	            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);

	            String text = Utils.read(in);
	            return text;
	        } finally {
	            JdbcUtils.close(in);
	        }
	    }
	 
	 public  byte[] readByteArrayFromResource(String resource) throws IOException {
	        InputStream in = null;
	        try {
	            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
	            return Utils.readByteArray(in);
	        } finally {
	            JdbcUtils.close(in);
	        }
	    }
}
