package com.liaier.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jsonobject.JsonObjectTools;
import net.sf.json.JSONObject;

import com.liaier.db.DBUtil;
import com.liaier.service.TDataOperation;
import com.liaier.service.interfaces.IDataOperation;

public class DataAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DataAction.class);
    public DataAction() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		 String object  = request.getParameter("object");
		 String methods   = request.getParameter("method");
		 String parameter = (String) request.getAttribute("parameter");
		 Connection connection = null;
		 Statement  statement  = null;
		 String result = "";
		 try {
			 request.setCharacterEncoding("UTF-8");
			 response.setCharacterEncoding("UTF-8");
			 
			 connection =  DBUtil.getConnection();
			 statement = connection.createStatement();
			 
			 IDataOperation dataOperation  = new TDataOperation();
			 dataOperation.setStatement(statement);
			 if("define".equals(object)){
				 JSONObject contObj = JsonObjectTools.getJSObj(parameter);
				 String sql = "select filter from  object_define_method where object_define_method.role='"+contObj.getString("role")+"' and object_define_method.method='"+methods+"'";
				 ResultSet resultSet = statement.executeQuery(sql);
				 String filter = "";
				 if(resultSet.next()){
					 filter = resultSet.getString("filter");
				 }
				 resultSet.close();
				 result = dataOperation.queryDefine(filter,"");
			 
			 }
			 if("add".equals(methods)){
				 result = dataOperation.add(object, parameter);
			 }
			 if("save".equals(methods)){
				 result = dataOperation.save(object, parameter);	 
			 }
			 if("delete".equals(methods)){
				 result = dataOperation.delete(object, parameter);
			 }
			 if("delAndInsrt".equals(methods)){
				 dataOperation.delete(object, parameter);
				 result = dataOperation.add(object, parameter);
			 }
			 if("queryObj".equals(methods)){
				 JSONObject contObj = JsonObjectTools.getJSObj(parameter);
				 String sql = "select filter from object_method,object where object.o_id=object_method.object and object.name='"+object+"' and object_method.role='"+contObj.getString("role")+"' and object_method.method='queryObj'";
				 ResultSet resultSet = statement.executeQuery(sql);
				 String filter = "";
				 if(resultSet.next()){
					 filter = resultSet.getString("filter");
				 }
				 resultSet.close();
				 result = dataOperation.queryObj(object, parameter, filter);
			 }
			 if("queryObjPage".equals(methods)){
				 JSONObject contObj = JsonObjectTools.getJSObj(parameter);
				 String sql = "select filter from object_method,object where object.o_id=object_method.object and object.name='"+object+"' and object_method.role='"+contObj.getString("role")+"' and object_method.method='queryObjPage'";
				 ResultSet resultSet = statement.executeQuery(sql);
				 String filter = "";
				 if(resultSet.next()){
					 filter = resultSet.getString("filter");
				 }
				 resultSet.close();
				 result = dataOperation.queryObjPage(object, parameter, filter);
			 }
			 if("queryObjTree".equals(methods)){
				 JSONObject contObj = JsonObjectTools.getJSObj(parameter);
				 String sql = "select filter from object_method,object where object.o_id=object_method.object and object.name='"+object+"' and object_method.role='"+contObj.getString("role")+"' and object_method.method='queryObjPage'";
				 ResultSet resultSet = statement.executeQuery(sql);
				 String filter = "";
				 if(resultSet.next()){
					 filter = resultSet.getString("filter");
				 }
				 resultSet.close();
				 //result = dataOperation.queryObjTree(object, parameter, filter);
			 }
			 
			 if("queryObjLike".equals(methods)){
				 JSONObject contObj = JsonObjectTools.getJSObj(parameter);
				 String sql = "select filter from object_method,object where object.o_id=object_method.object and object.name='"+object+"' and object_method.role='"+contObj.getString("role")+"' and object_method.method='queryObjPage'";
				 ResultSet resultSet = statement.executeQuery(sql);
				 String filter = "";
				 if(resultSet.next()){
					 filter = resultSet.getString("filter");
				 }
				 resultSet.close();
				 result = dataOperation.queryObjLike(object, parameter, filter);
			 }
			 if("queryObjPageLike".equals(methods)){
				 JSONObject contObj = JsonObjectTools.getJSObj(parameter);
				 String sql = "select filter from object_method,object where object.o_id=object_method.object and object.name='"+object+"' and object_method.role='"+contObj.getString("role")+"' and object_method.method='queryObjPage'";
				 ResultSet resultSet = statement.executeQuery(sql);
				 String filter = "";
				 if(resultSet.next()){
					 filter = resultSet.getString("filter");
				 }
				 resultSet.close();
				 result = dataOperation.queryObjPageLike(object, parameter, filter);
			 }
			 
			 log.info(result);
	         response.getWriter().print(result);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
				try {
					if(statement!=null){
					   statement.close();
					}
					if(connection!=null){
					   connection.close();
				    }
					
				} catch (SQLException e) {
					e.printStackTrace();
			    }
		}
	}

}
