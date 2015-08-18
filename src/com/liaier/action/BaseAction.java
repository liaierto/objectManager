package com.liaier.action;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liaier.db.DBUtil;
import com.liaier.utils.ResultMsg;

import xmlanalytic.XmlAnalytic;

public class BaseAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(BaseAction.class);
    public BaseAction() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	    doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    String pService  = request.getParameter("service");
	    String pMethods   = request.getParameter("method");
	    String pParameter = (String) request.getAttribute("parameter");
	    
	    
	    Connection pConnection = null;
	    try {
	        request.setCharacterEncoding("UTF-8");
	        response.setCharacterEncoding("UTF-8");
	        //String path = request.getRealPath("/")+this.getServletContext().getInitParameter("location");
	        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("service.xml");
	        Map    pMap   = XmlAnalytic.ParseXmlData(in);
	        in.close();
	        String pClass = (String) pMap.get(pService);
            Class  pClazz = Class.forName(pClass);
          
            Object pObject = pClazz.newInstance();
            Class[] args   = new Class[1];
            args[0] = Statement.class;
            Class[] args2   = new Class[1];
            args2[0] = String.class;
            Method pMethod =  pClazz.getMethod("setStatement",args);
            Method pMethod2 =  pClazz.getMethod(pMethods,args2);
            String pResult = "";
            pConnection =  DBUtil.getConnection();
            Statement statement = pConnection.createStatement();
            pMethod.invoke(pObject, new Object[]{statement});
            pResult = (String) pMethod2.invoke(pObject,new Object[]{pParameter});
            log.info(pResult);
            response.getWriter().print(pResult);
        } catch (Exception e) {
            try {
                pConnection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            response.getWriter().print(ResultMsg.unknownMsg);
            log.error(e);
        }
	    finally{
            try {
                pConnection.close();
            } catch (SQLException e) {
                log.error(e);
            }
        } 
	    
	    
	}
	public  String getIP(HttpServletRequest request) {  
	    String ip = request.getHeader("x-forwarded-for");  
	    if (!checkIP(ip)) {  
	        ip = request.getHeader("Proxy-Client-IP");  
	    }  
	    if (!checkIP(ip)) {  
	        ip = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if (!checkIP(ip)) {  
	        ip = request.getRemoteAddr();  
	    }  
	    return ip;  
	}  
	private  boolean checkIP(String ip) {  
	    if (ip == null || ip.length() == 0 || "unkown".equalsIgnoreCase(ip)  
	            || ip.split(".").length != 4) {  
	        return false;  
	    }  
	    return true;  
	}  
}
