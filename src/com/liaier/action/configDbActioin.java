package com.liaier.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liaier.db.DBUtil;
import com.liaier.utils.TableXmlAnalytic;

public class configDbActioin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(configDbActioin.class);
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url  = request.getParameter("url");
	    String user   = request.getParameter("user");
	    String password  = request.getParameter("pass");
	    
	  
        Connection connection = null;
        try {
        	String path = request.getRealPath("/")+this.getServletContext().getInitParameter("location");
	        
	        InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
	        Properties props = new Properties();
	        props.load(in);
	        in.close();
		    FileOutputStream out = new FileOutputStream(path+File.separator+"db.properties");
	        props.setProperty("url", url);
	        props.setProperty("username", user);
	        props.setProperty("password", password);
	        props.store(out, "UTF-8");
	        out.close();  
	        
	        connection = DBUtil.getConnection();
	        List<StringBuffer> sqls = TableXmlAnalytic.ParseXmlData(path+File.separator+"base");
			Statement statement = connection.createStatement();
			int size = sqls.size();
			for(int i=0;i<size;i++){
				StringBuffer sql =  sqls.get(i);
				log.info(sql);
				statement.addBatch(sql.toString());
			}
			statement.executeBatch();
			request.getRequestDispatcher("/main.html").forward(request, response);
		} catch (SQLException e) {
			log.error(e);
			request.getRequestDispatcher("/config.html").forward(request, response);
		}finally{
            try {
                connection.close();
            } catch (SQLException e) {
                log.error(e);
            }
        } 
	}

}
