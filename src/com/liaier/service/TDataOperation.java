package com.liaier.service;

import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liaier.bean.IDataItem;
import com.liaier.beanImpl.TDataItem;
import com.liaier.dao.TMysqlPugin;
import com.liaier.service.interfaces.IDataOperation;
import com.liaier.utils.ResultMsg;

public class TDataOperation extends TService implements IDataOperation {

	private static Log log = LogFactory.getLog(TDataOperation.class);
    private TMysqlPugin MysqlPugin;
	
	public TDataOperation(){
		MysqlPugin = new TMysqlPugin();
	}
	
	public String add(String objectName,String content) {
	    String     response  = "";
		IDataItem dataItem = new TDataItem();
		if(MysqlPugin.insert(objectName, content)){
			dataItem.setValue("code", ResultMsg.sucessCode);
    		dataItem.setValue("msg", ResultMsg.sucessMsg);
		}else{
			dataItem.setValue("code", ResultMsg.errorCode);
    		dataItem.setValue("msg", ResultMsg.errorMsg);
		}
		response = dataItem.getJsonRow().toString();
		return response;
	}

	public String save(String objectName,String content) { 
		String     response  = "";
		IDataItem dataItem = new TDataItem();
		if(MysqlPugin.update(objectName, content)){
			dataItem.setValue("code", ResultMsg.sucessCode);
			dataItem.setValue("msg", ResultMsg.sucessMsg);
		}else{
			dataItem.setValue("code", ResultMsg.errorCode);
			dataItem.setValue("msg", ResultMsg.errorMsg);
		}
		response = dataItem.getJsonRow().toString();
		return response;
	}

	public String delete(String objectName,String content) { 
		String     response  = "";
		IDataItem dataItem = new TDataItem();
		if(MysqlPugin.delete(objectName, content)){
			dataItem.setValue("code", ResultMsg.sucessCode);
			dataItem.setValue("msg", ResultMsg.sucessMsg);
		}else{
			dataItem.setValue("code", ResultMsg.errorCode);
			dataItem.setValue("msg", ResultMsg.errorMsg);
		}
		response = dataItem.getJsonRow().toString();
		return response;
	}

	public String queryObj(String objectName,String content,String convernt) {
        try {
        	return MysqlPugin.query(objectName, content,convernt);
        } catch (Exception e) {
            log.error(e);
            return "false";
        }
		
			
	}

	public String queryObjPage(String objectName,String content,String convernt) {
		 try {
	         return MysqlPugin.queryPage(objectName, content,convernt);
	        } catch (Exception e) {
	            log.error(e);
	            return "false";
	        }
	}
	public String queryObjTree(String objectName,String content,String convernt) {
		 try {
	         return MysqlPugin.queryTree(objectName, content,convernt);
	        } catch (Exception e) {
	            log.error(e);
	            return "false";
	        }
	}
	public void setStatement(Statement statement){
		   this.MysqlPugin.setDataSource(statement);
	    }
}
