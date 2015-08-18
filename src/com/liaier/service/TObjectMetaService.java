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
import com.liaier.service.interfaces.IObjectMetaService;
import com.liaier.utils.ResultMsg;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import datetools.DateTools;




public class TObjectMetaService extends TService implements IObjectMetaService {
    
	private static Log log = LogFactory.getLog(TObjectMetaService.class);
    public TObjectMetaService(){}

	public String queryPage(String content) {
	       IQueryDB     queryDB    = null;
	       JSONObject   obj         = new JSONObject();
	       int          currentPage = 1;
	       int          pageRow     = 10;
	        try {
	            JSONObject contObj = JsonObjectTools.getJSObj(content);
	            queryDB = TQueryDB.getInstance();
	            
	            queryDB.setTableName(new String[]{"meta"});
	            queryDB.setQueryField(new String[]{"m_id","name","description","len","type","iskey"});
	            queryDB.setWhereFileds("object", contObj.getString("object"));
	            queryDB.setWhereFileds("role", contObj.getString("role"));
	            if(contObj.containsKey("currentpage")){
	                currentPage = Integer.valueOf(contObj.getString("currentpage"));
	            }
	            if(contObj.containsKey("pageRow")){
	                pageRow = Integer.valueOf(contObj.getString("pageRow"));
	            }
	            queryDB.setOrderSql("m_id desc");
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
            String id            = rowData.getString("id");
            
            inputForm = TInputForm.getInstance();
            
            inputForm.setTableName("meta");
            inputForm.setWhereFileds("id", id);
            
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
        String object = (String) cont.get("object");
        String role = (String) cont.get("role");
        dataArray = JsonObjectTools.getJSAObj(cont.getString("rows"));
        Iterator iter = dataArray.iterator();
        String     response  = "";
        try{
            while (iter.hasNext()) {
                rowData = (JSONObject) iter.next();
                
                inputForm.setTableName("meta");
                String id     = rowData.getString("m_id");
                String name   = rowData.getString("name");
                String len     = rowData.getString("len");
                String type   = rowData.getString("type");
                String iskey   = rowData.getString("iskey");
            	String description   = rowData.getString("description");
            	inputForm.setStrVal("name", name);
            	inputForm.setStrVal("description", description);     
            	inputForm.setStrVal("len", len);
            	inputForm.setStrVal("type", type); 
            	inputForm.setStrVal("role", role); 
            	inputForm.setStrVal("object", object); 
            	inputForm.setStrVal("iskey", iskey); 
                if(!"".equals(id)){
                	inputForm.setStrVal("update_time",DateTools.getCurrentTime("yyyyMMDDhhmmss"));
                    inputForm.setWhereFileds("m_id", id);
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
}
