package com.liaier.service;


import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jsonobject.JsonObjectTools;

import com.liaier.bean.IDataItem;
import com.liaier.bean.IFormData;
import com.liaier.bean.IQueryDB;
import com.liaier.bean.InputForm;
import com.liaier.beanImpl.TDataItem;
import com.liaier.beanImpl.TInputForm;
import com.liaier.beanImpl.TQueryDB;
import com.liaier.service.interfaces.IObjectService;
import com.liaier.utils.ResultMsg;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import datetools.DateTools;




public class TObjectService extends TService implements IObjectService {
	
	private static Log log = LogFactory.getLog(TObjectService.class);
    public TObjectService(){
    
    }

	public String add(String content) {
		InputForm inputForm = TInputForm.getInstance();
        JSONObject rowData   = null;
        boolean    result    = false ;
        String     response  = "";
        rowData = JSONObject.fromObject(content);
        String name          = rowData.getString("name");
        String type          = rowData.getString("type");
    	String description   = rowData.getString("description");
    	String role   = rowData.getString("role");
    	inputForm.setTableName("object");
    	inputForm.setStrVal("name", name);
    	inputForm.setStrVal("type", type);
    	inputForm.setStrVal("description", description);
    	inputForm.setStrVal("role", role);
    	try {
			inputForm.setStrVal("create_time",DateTools.getCurrentTime("yyyyMMDDhhmmss"));
		} catch (Exception e) {
			log.error(e);
		} 
    	result = super.insert(inputForm);
    	IDataItem dataItem = new TDataItem();
    	if(result){
    		dataItem.setValue("code", ResultMsg.sucessCode);
    		dataItem.setValue("msg", ResultMsg.sucessMsg);
    	}else{
    		dataItem.setValue("code", ResultMsg.errorCode);
    		dataItem.setValue("msg", ResultMsg.errorMsg);
    	}
    	response = dataItem.getJsonRow().toString();
		return response;
	}

	public String queryPage(String content) {
	       IQueryDB     queryDB    = null;
	       JSONObject   obj         = new JSONObject();
	       int          currentPage = 1;
	       int          pageRow     = 10;
	        try {
	            JSONObject contObj = JsonObjectTools.getJSObj(content);
	            queryDB = TQueryDB.getInstance();
	            
	            queryDB.setTableName(new String[]{"object"});
	            queryDB.setQueryField(new String[]{"o_id","name","description","type","create_time","update_time"});
	            queryDB.setWhereFileds("role", contObj.getString("role"));
	            if(contObj.containsKey("currentpage")){
	                currentPage = Integer.valueOf(contObj.getString("currentpage"));
	            }
	            if(contObj.containsKey("pageRow")){
	                pageRow = Integer.valueOf(contObj.getString("pageRow"));
	            }
	            queryDB.setOrderSql("o_id desc");
	            int totalRows = super.queryOfRows(queryDB);
	            int totalPage = totalRows/pageRow;
	            int i         = totalRows%pageRow;
	            if(i!=0){
	               totalPage = totalPage+1;
	            }
	            IFormData formData = super.queryByPage(queryDB, pageRow, currentPage);
	            obj.put("rows", formData.getJsonArrayRows().toString());
	            obj.put("total", totalPage);
	            return obj.toString();
	        } catch (Exception e) {
	            log.error(e);
	            return "false";
	        }
	    
	}

	public String remove(String content) {
		InputForm inputForm = null;
	    boolean    result    = false ;
	    String     response = "";
	    IDataItem dataItem = new TDataItem();
        try {
        	
        	JSONObject rowData   = null;
            rowData = JsonObjectTools.getJSObj(content);
            String id            = rowData.getString("o_id");
            
            inputForm = TInputForm.getInstance();
            
            inputForm.setTableName("object");
            inputForm.setWhereFileds("o_id", id);
            
            result = super.delete(inputForm);
        	if(result){
        		dataItem.setValue("code", ResultMsg.sucessCode);
        		dataItem.setValue("msg", ResultMsg.sucessMsg);
        	}else{
        		dataItem.setValue("code", ResultMsg.errorCode);
        		dataItem.setValue("msg", ResultMsg.errorMsg);
        	}
        } catch (Exception e) {
            log.error(e);
            dataItem.setValue("code", ResultMsg.unknownCode);
    		dataItem.setValue("msg", ResultMsg.unknownMsg);
        }
        response = dataItem.getJsonRow().toString();
		return response;
       
	}

	public String update(String content) {
        InputForm inputForm = TInputForm.getInstance();
        JSONArray dataArray = null;
        JSONObject rowData = null;
        IDataItem dataItem = new TDataItem();
        JSONObject cont = JsonObjectTools.getJSObj(content);
        String role = (String) cont.get("role");
        dataArray = JsonObjectTools.getJSAObj(cont.getString("rows"));
        Iterator iter = dataArray.iterator();
        String     response  = "";
        try{
            while (iter.hasNext()) {
                rowData = (JSONObject) iter.next();
                
                inputForm.setTableName("object");
                String id     = rowData.getString("o_id");
                String name   = rowData.getString("name");
                String type   = rowData.getString("type");
            	String description   = rowData.getString("description");
            	inputForm.setStrVal("name", name);
            	inputForm.setStrVal("type", type);
            	inputForm.setStrVal("description", description);   
            	inputForm.setStrVal("role", role);
                if(!"".equals(id)){
                	inputForm.setStrVal("update_time",DateTools.getCurrentTime("yyyyMMDDhhmmss"));
                    inputForm.setWhereFileds("o_id", id);
                    super.update(inputForm);
                }else{
                	inputForm.setStrVal("create_time",DateTools.getCurrentTime("yyyyMMDDhhmmss"));
                    super.insert(inputForm);
                }
                
            }
            dataItem.setValue("code", ResultMsg.sucessCode);
    		dataItem.setValue("msg", ResultMsg.sucessMsg);
        }catch(Exception e){
        	 dataItem.setValue("code", ResultMsg.unknownCode);
     		 dataItem.setValue("msg", ResultMsg.unknownMsg);
             log.error(e);
        }
        response = dataItem.getJsonRow().toString();
    	return response;
        
    }

	public String query(String content) {
	       IQueryDB     queryDB    = null;
	        try {
	            JSONObject contObj = JsonObjectTools.getJSObj(content);
	            queryDB = TQueryDB.getInstance();
	            
	            queryDB.setTableName(new String[]{"object"});
	            queryDB.setQueryField(new String[]{"o_id","name"});
	            queryDB.setWhereFileds("role", contObj.getString("role"));
	            IFormData formData = super.query(queryDB);
	            return formData.getJsonArrayRows().toString();
	        } catch (Exception e) {
	            log.error(e);
	            return "false";
	        }
	    
	}

	public String createObject(String content) {
	       IQueryDB     queryDB    = null;
	       IDataItem dataItem = new TDataItem();
	        try {
	            JSONObject contObj = JsonObjectTools.getJSObj(content);
	            queryDB = TQueryDB.getInstance();
	            String obj = contObj.getString("object");
	            String name = contObj.getString("name");
	            queryDB.setTableName(new String[]{"meta"});
	            queryDB.setQueryField(new String[]{"name","type","len","iskey"});
	            queryDB.setWhereFileds("role", contObj.getString("role"));
	            queryDB.setWhereFileds("object",obj );
	            IFormData formData = super.query(queryDB);
	            if(createTable(name,formData)){
	            	dataItem.setValue("code", ResultMsg.sucessCode);
	        		dataItem.setValue("msg", ResultMsg.sucessMsg);
	            }else{
	            	dataItem.setValue("code", ResultMsg.errorCode);
	        		dataItem.setValue("msg", ResultMsg.errorMsg);
	            }
	            return dataItem.getJsonRow().toString();
	        } catch (Exception e) {
	            log.error(e);
	            dataItem.setValue("code", ResultMsg.unknownCode);
	     		dataItem.setValue("msg", ResultMsg.unknownMsg);
	     		return dataItem.getJsonRow().toString();
	        }
	        
	} 
	
	private boolean createTable(String obj,IFormData formData){
		String key = "";
		StringBuffer sql = new StringBuffer();
    	sql.append("create table ");
    	sql.append(obj).append(" (");
    	int count = formData.getRowCount();
    	for(int i=0;i<count;i++){
    		 IDataItem dataItem = formData.getRowItem(i);
    		 String field = dataItem.getString("name");
    		 String type  = dataItem.getString("type");
    		 String len   = dataItem.getString("len");
    		 if("string".equals(type)){
    			 type = "varchar";
    		 }
    		 sql.append(field).append(" ");
    		 sql.append(type);
    		 if(len!=null && !"".equals(len)){
    			 sql.append("(").append(dataItem.getString("len")).append(") "); 
    		 }
    		 String isKey = dataItem.getString("iskey");
    		 if("Yes".equals(isKey)){
    			 sql.append("auto_increment");
    			 key = field;
    		 }
    		sql.append(",");
    	}
    	sql.append(" primary key("+key+"))");
    	return super.excute(sql.toString());
		
	}
}
