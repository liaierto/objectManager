package com.liaier.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jsonobject.JsonObjectTools;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TMysqlPugin {
	private Statement mStatement;
	private static Log log = LogFactory.getLog(TMysqlPugin.class);
    public  boolean insert(String objectName,String content) {
        JSONArray dataArray = null;
        JSONObject rowData  = null;

        try {
        	JSONObject cont = JsonObjectTools.getJSObj(content);
        	dataArray = JsonObjectTools.getJSAObj(cont.getString("rows"));
        	Iterator iter = dataArray.iterator();
        	while (iter.hasNext()) {
        		StringBuffer sql         = new StringBuffer();
                StringBuffer columnNames = new StringBuffer();
                StringBuffer values      = new StringBuffer();
                sql.append("insert into " + objectName+"(");
        		rowData = (JSONObject) iter.next();
        		for (Iterator itr = rowData.keySet().iterator(); itr.hasNext();) {
                    String pColumnName  = (String) itr.next();
                    String pColumnValue = (String) rowData.get(pColumnName);
                    columnNames.append(pColumnName+",");
                    values.append("'").append( pColumnValue+"'").append(",");
                }
                sql.append(columnNames.deleteCharAt(columnNames.length()-1)+")values("+values.deleteCharAt(values.length()-1)+")");
                String Sql = sql.toString();
                log.info(Sql);
                mStatement.addBatch(Sql);
        	 }
            
        	mStatement.executeBatch();
            return true;
            
        } catch (SQLException e) {
            log.error(e);
            return false;
            
        } 

    }

    public  boolean update(String objectName,String content) {
        JSONArray  dataArray = null;
        JSONObject rowData   = null;
        JSONObject filter    = null;
        try {
        	JSONObject cont = JsonObjectTools.getJSObj(content);
        	dataArray = JsonObjectTools.getJSAObj(cont.getString("rows"));
        	filter = JsonObjectTools.getJSObj(cont.getString("filter"));
        	
        	if(filter==null||"".equals(filter)){
        		return false;
        	}
        	StringBuffer where2 = new StringBuffer();
        	for (Iterator itr = filter.keySet().iterator(); itr.hasNext();) {
        		String whereName  = (String) itr.next();
                String whereValue = (String) filter.get(whereName);
                if(!"".equals(whereValue)){
                	where2.append(" and ").append(whereName+"='"+whereValue+"'");
                }
        	}
        	Iterator iter = dataArray.iterator();
        	while (iter.hasNext()) {
        		StringBuffer sql  =  new StringBuffer();
        		StringBuffer where = new StringBuffer();
                sql.append("update "+ objectName+ " set ");
                where.append(" where 1=1");
                boolean bz = false;
                StringBuffer addSql   = new StringBuffer();
                StringBuffer columnNames = new StringBuffer();
                StringBuffer values      = new StringBuffer();
                addSql.append("insert into " + objectName+"(");
                
                rowData = (JSONObject) iter.next();
        		for (Iterator itr = rowData.keySet().iterator(); itr.hasNext();) {
                    String columnName  = (String) itr.next();
                    String columnValue =  rowData.get(columnName).toString();
                    if("".equals(filter.get(columnName))){
                    	if(columnValue==null || "".equals(columnValue)){
                    		bz = true;
                    		continue;
                    	}else{
                    		where.append(" and ").append(columnName+"='"+columnValue+"'");
                    	}
                    }
                    columnNames.append(columnName+",");
                    values.append("'").append( columnValue+"'").append(",");
                    
                    sql.append(columnName+"='"+columnValue+"'");
                    sql.append(",");
                }
                String Sql = sql.deleteCharAt(sql.length()-1).toString()+where.toString()+where2.toString();
                if(bz){
                	addSql.append(columnNames.deleteCharAt(columnNames.length()-1)+")values("+values.deleteCharAt(values.length()-1)+")");
                    log.info(addSql);
                    mStatement.addBatch(addSql.toString());
                }else{
                	log.info(Sql);
                    mStatement.addBatch(Sql);
                }
        	}
        	mStatement.executeBatch();
             return true;
            
        } catch (SQLException e) {
            log.error(e);
            return false;
            
        }
    }

    public  boolean delete(String objectName,String content) {
        JSONObject filter = null;
        try {
        	JSONObject cont = JsonObjectTools.getJSObj(content);
        	filter = JsonObjectTools.getJSObj(cont.getString("filter"));
        	StringBuffer where = new StringBuffer();
        	for (Iterator itr = filter.keySet().iterator(); itr.hasNext();) {
        		String whereName  = (String) itr.next();
                String whereValue = (String) filter.get(whereName);
                if(!"".equals(whereValue)){
                	if(whereValue.contains(",")){
                		String[] values = whereValue.split(",");
                		where.append(" and ").append(whereName).append(" in (");
                		for(int i=0;i<values.length;i++){
                			where.append("'").append(values[i]).append("'").append(",");
                		}
                		where = where.deleteCharAt(where.length()-1);
                		where.append(")");
                	}else{
                		where.append(" and ").append(whereName+"='"+whereValue).append("'");
                	}
                	
                }
        	}
            StringBuffer pSql  =  new StringBuffer();
            
            pSql.append("delete from " + objectName);
            pSql.append(" where 1=1 ");
            
            String Sql  = pSql.append(where).toString();
            log.info(Sql);
            int pResult = mStatement.executeUpdate(Sql);
            if(pResult>=1){
               return true;
            }else{
                return false;
            }
            
            
        } catch (SQLException e) {
            log.error(e);
            return false;
        }
    }

    public  String query(String objectName,String content,String convernt) throws Exception {
        ResultSet         resultSet    = null;
        JSONObject filter = null;
        JSONObject ret = new JSONObject();
        String fields = null;
        try {
        	JSONObject cont = JsonObjectTools.getJSObj(content);
        	fields = (String) cont.get("fields");
        	filter = JsonObjectTools.getJSObj(cont.getString("filter"));
        	Hashtable<String,String> isConvent = new Hashtable<String,String> ();
        	if(!"".equals(convernt)){
        		String[] convernts = convernt.split(",");
            	int cLen = convernts.length;
            	for(int i=0;i<cLen;i++){
            		String[] kv = convernts[i].split(":");
            		isConvent.put(kv[0], kv[1]);
            	}
        	}
        	
        	StringBuffer sql = new StringBuffer("select ");
        	sql.append(fields);
        	sql.append(" from ");
        	sql.append(objectName);
        	sql.append(" where 1=1");
        	StringBuffer where = new StringBuffer();
        	for (Iterator itr = filter.keySet().iterator(); itr.hasNext();) {
        		String whereName  = (String) itr.next();
                String whereValue = (String) filter.get(whereName);
                if(!"".equals(whereValue)){
                	String[] values = whereValue.split(",");
                	int vl = values.length;
                	if(vl>1){
                		where.append(" and ").append(whereName+" in (");
                		for(int i=0;i<vl;i++){
                			where.append("'"+values[i]+"'").append(",");
                		}
                		where = where.deleteCharAt(where.length()-1).append(")");
                	}else{
                		where.append(" and ").append(whereName+"='"+whereValue+"'");
                	}
                }
        	}
        	sql.append(where);
        	resultSet = mStatement.executeQuery(sql.toString());
        	
        	JSONArray rows = new JSONArray();
        	while(resultSet.next()){
        		JSONObject row = new JSONObject();
        		String[]  fieldsStr = fields.split(",");
        		for(int j=0;j<fieldsStr.length;j++){
        			String fName ="";
        			if(fieldsStr[j].indexOf(" AS ") >0){
        				fName = fieldsStr[j].split(" AS ")[1];
        			}
        			if(fieldsStr[j].indexOf(" as ") >0){
        				fName = fieldsStr[j].split(" as ")[1];
        			}else{
        				fName = fieldsStr[j];
        			}
        			 if(resultSet.getObject(fName)!=null){
        				 String vcode = isConvent.get(fName);
        				 if(vcode!=null && !"".equals(vcode)){
        					 row.put(fName,new String((byte[])resultSet.getObject(fName),vcode)); 
        				 }else{
        					 row.put(fName,resultSet.getObject(fName)); 
        				 }
                     }else{
                    	 row.put(fName,""); 
                     }
        		}
        		rows.add(row);
        	}
        	ret.put("rows", rows);
            return  ret.toString();
            
        } catch (SQLException e) {
            log.error(e);
            return null;
            
        }finally {
            try {
                if (resultSet != null) {
                	resultSet.close();
                }
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }
    public  String queryLike(String objectName,String content,String convernt) throws Exception {
        ResultSet         resultSet    = null;
        JSONObject filter = null;
        JSONObject ret = new JSONObject();
        String fields = null;
        try {
        	JSONObject cont = JsonObjectTools.getJSObj(content);
        	fields = (String) cont.get("fields");
        	filter = JsonObjectTools.getJSObj(cont.getString("filter"));
        	Hashtable<String,String> isConvent = new Hashtable<String,String> ();
        	if(!"".equals(convernt)){
        		String[] convernts = convernt.split(",");
            	int cLen = convernts.length;
            	for(int i=0;i<cLen;i++){
            		String[] kv = convernts[i].split(":");
            		isConvent.put(kv[0], kv[1]);
            	}
        	}
        	
        	StringBuffer sql = new StringBuffer("select ");
        	sql.append(fields);
        	sql.append(" from ");
        	sql.append(objectName);
        	sql.append(" where 1=1");
        	StringBuffer where = new StringBuffer();
        	for (Iterator itr = filter.keySet().iterator(); itr.hasNext();) {
        		String whereName  = (String) itr.next();
                String whereValue = (String) filter.get(whereName);
                if(!"".equals(whereValue)){
                	String[] values = whereValue.split(",");
                	int vl = values.length;
                	if(vl>1){
                		where.append(" and ").append(whereName+" in (");
                		for(int i=0;i<vl;i++){
                			where.append("'"+values[i]+"'").append(",");
                		}
                		where = where.deleteCharAt(where.length()-1).append(")");
                	}else{
                		where.append(" and ").append(whereName+" like '%"+whereValue+"%'");
                	}
                }
        	}
        	sql.append(where);
        	resultSet = mStatement.executeQuery(sql.toString());
        	
        	JSONArray rows = new JSONArray();
        	while(resultSet.next()){
        		JSONObject row = new JSONObject();
        		String[]  fieldsStr = fields.split(",");
        		for(int j=0;j<fieldsStr.length;j++){
        			String fName ="";
        			if(fieldsStr[j].indexOf(" AS ") >0){
        				fName = fieldsStr[j].split(" AS ")[1];
        			}
        			if(fieldsStr[j].indexOf(" as ") >0){
        				fName = fieldsStr[j].split(" as ")[1];
        			}else{
        				fName = fieldsStr[j];
        			}
        			 if(resultSet.getObject(fName)!=null){
        				 String vcode = isConvent.get(fName);
        				 if(vcode!=null && !"".equals(vcode)){
        					 row.put(fName,new String((byte[])resultSet.getObject(fName),vcode)); 
        				 }else{
        					 row.put(fName,resultSet.getObject(fName)); 
        				 }
                     }else{
                    	 row.put(fName,""); 
                     }
        		}
        		rows.add(row);
        	}
        	ret.put("rows", rows);
            return  ret.toString();
            
        } catch (SQLException e) {
            log.error(e);
            return null;
            
        }finally {
            try {
                if (resultSet != null) {
                	resultSet.close();
                }
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }
    public  String queryPage(String objectName,String content,String convernt) throws Exception{
        ResultSet  resultSet  = null;
        JSONObject filter     = null;
        JSONObject ret        = new JSONObject();
        String     fields     = null;
        Integer    curentPage = null;
        Integer    pageRow    = null;
        try {
        	JSONObject cont = JsonObjectTools.getJSObj(content);
        	curentPage = (Integer) cont.get("curentPage");
        	pageRow = (Integer) cont.get("pageRow");
        	fields = (String) cont.get("fields");
        	filter = JsonObjectTools.getJSObj(cont.getString("filter"));
        	Hashtable<String,String> isConvent = new Hashtable<String,String> ();
        	if(!"".equals(convernt)){
        		String[] convernts = convernt.split(",");
            	int cLen = convernts.length;
            	for(int i=0;i<cLen;i++){
            		String[] kv = convernts[i].split(":");
            		isConvent.put(kv[0], kv[1]);
            	}
        	}
        	
        	StringBuffer sql = new StringBuffer("select ");
        	sql.append(fields);
        	sql.append(" from ");
        	sql.append(objectName);
        	sql.append(" where 1=1");
        	StringBuffer where = new StringBuffer();
        	for (Iterator itr = filter.keySet().iterator(); itr.hasNext();) {
        		String whereName  = (String) itr.next();
                String whereValue = (String) filter.get(whereName);
                if(!"".equals(whereValue)){
                	where.append(" and ").append(whereName+"="+whereValue);
                }
        	}
        	sql.append(where);
        	sql.append(" limit "+(pageRow*(curentPage-1))+","+pageRow);
        	resultSet = mStatement.executeQuery(sql.toString());
        	JSONArray rows = new JSONArray();
        	while(resultSet.next()){
        		JSONObject row = new JSONObject();
        		String[]  fieldsStr = fields.split(",");
        		for(int j=0;j<fieldsStr.length;j++){
        			String fName ="";
        			if(fieldsStr[j].contains("AS")){
        				fName = fieldsStr[j].split("AS")[1];
        			}
        			if(fieldsStr[j].contains("as")){
        				fName = fieldsStr[j].split("as")[1];
        			}else{
        				fName = fieldsStr[j];
        			}
        			 if(resultSet.getObject(fName)!=null){
        				 String vcode = isConvent.get(fName);
        				 if(vcode!=null && !"".equals(vcode)){
        					 row.put(fName,new String((byte[])resultSet.getObject(fName),vcode)); 
        				 }else{
        					 row.put(fName,resultSet.getObject(fName)); 
        				 }
                     }else{
                    	 row.put(fName,""); 
                     }
        		}
        		rows.add(row);
        	}
        	ret.put("rows", rows);
        	int totalRows = queryOfRows(objectName,content);
        	int totalPage = totalRows/pageRow;
            int i         = totalRows%pageRow;
            if(i!=0){
               totalPage = totalPage+1;
            }
            ret.put("totalPage", totalPage);
            return  ret.toString();
            
        } catch (SQLException e) {
            log.error(e);
            return null;
            
        }finally {
            try {
                if (resultSet != null) {
                	resultSet.close();
                }
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }
    public  String queryPageLike(String objectName,String content,String convernt) throws Exception{
        ResultSet  resultSet  = null;
        JSONObject filter     = null;
        JSONObject ret        = new JSONObject();
        String     fields     = null;
        Integer    curentPage = null;
        Integer    pageRow    = null;
        try {
        	JSONObject cont = JsonObjectTools.getJSObj(content);
        	curentPage = (Integer) cont.get("curentPage");
        	pageRow = (Integer) cont.get("pageRow");
        	fields = (String) cont.get("fields");
        	filter = JsonObjectTools.getJSObj(cont.getString("filter"));
        	Hashtable<String,String> isConvent = new Hashtable<String,String> ();
        	if(!"".equals(convernt)){
        		String[] convernts = convernt.split(",");
            	int cLen = convernts.length;
            	for(int i=0;i<cLen;i++){
            		String[] kv = convernts[i].split(":");
            		isConvent.put(kv[0], kv[1]);
            	}
        	}
        	
        	StringBuffer sql = new StringBuffer("select ");
        	sql.append(fields);
        	sql.append(" from ");
        	sql.append(objectName);
        	sql.append(" where 1=1");
        	StringBuffer where = new StringBuffer();
        	for (Iterator itr = filter.keySet().iterator(); itr.hasNext();) {
        		String whereName  = (String) itr.next();
                String whereValue = (String) filter.get(whereName);
                if(!"".equals(whereValue)){
                	where.append(" and ").append(whereName+" like '%"+whereValue+"%'");
                }
        	}
        	sql.append(where);
        	sql.append(" limit "+(pageRow*(curentPage-1))+","+pageRow);
        	resultSet = mStatement.executeQuery(sql.toString());
        	JSONArray rows = new JSONArray();
        	while(resultSet.next()){
        		JSONObject row = new JSONObject();
        		String[]  fieldsStr = fields.split(",");
        		for(int j=0;j<fieldsStr.length;j++){
        			String fName ="";
        			if(fieldsStr[j].contains("AS")){
        				fName = fieldsStr[j].split("AS")[1];
        			}
        			if(fieldsStr[j].contains("as")){
        				fName = fieldsStr[j].split("as")[1];
        			}else{
        				fName = fieldsStr[j];
        			}
        			 if(resultSet.getObject(fName)!=null){
        				 String vcode = isConvent.get(fName);
        				 if(vcode!=null && !"".equals(vcode)){
        					 row.put(fName,new String((byte[])resultSet.getObject(fName),vcode)); 
        				 }else{
        					 row.put(fName,resultSet.getObject(fName)); 
        				 }
                     }else{
                    	 row.put(fName,""); 
                     }
        		}
        		rows.add(row);
        	}
        	ret.put("rows", rows);
        	int totalRows = queryOfRows(objectName,content);
        	int totalPage = totalRows/pageRow;
            int i         = totalRows%pageRow;
            if(i!=0){
               totalPage = totalPage+1;
            }
            ret.put("totalPage", totalPage);
            return  ret.toString();
            
        } catch (SQLException e) {
            log.error(e);
            return null;
            
        }finally {
            try {
                if (resultSet != null) {
                	resultSet.close();
                }
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }
    public  String queryTree(String objectName,String content,String convernt) throws Exception {
        ResultSet         resultSet    = null;
        JSONObject filter = null;
        JSONObject ret = new JSONObject();
        String fields = null;
        try {
        	JSONObject cont = JsonObjectTools.getJSObj(content);
        	fields = (String) cont.get("fields");
        	filter = JsonObjectTools.getJSObj(cont.getString("filter"));
        	Hashtable<String,String> isConvent = new Hashtable<String,String> ();
        	if(!"".equals(convernt)){
        		String[] convernts = convernt.split(",");
            	int cLen = convernts.length;
            	for(int i=0;i<cLen;i++){
            		String[] kv = convernts[i].split(":");
            		isConvent.put(kv[0], kv[1]);
            	}
        	}
        	
        	StringBuffer sql = new StringBuffer("select ");
        	sql.append(fields);
        	sql.append(" from ");
        	sql.append(objectName);
        	sql.append(" where 1=1");
        	StringBuffer where = new StringBuffer();
        	for (Iterator itr = filter.keySet().iterator(); itr.hasNext();) {
        		String whereName  = (String) itr.next();
                String whereValue = (String) filter.get(whereName);
                if(!"".equals(whereValue)){
                	String[] values = whereValue.split(",");
                	int vl = values.length;
                	if(vl>1){
                		where.append(" and ").append(whereName+" in (");
                		for(int i=0;i<vl;i++){
                			where.append("'"+values[i]+"'").append(",");
                		}
                		where = where.deleteCharAt(where.length()-1).append(")");
                	}else{
                		where.append(" and ").append(whereName+"='"+whereValue+"'");
                	}
                }
        	}
        	sql.append(where);
        	resultSet = mStatement.executeQuery(sql.toString());
        	
        	JSONArray rows = new JSONArray();
        	while(resultSet.next()){
        		JSONObject row = new JSONObject();
        		String[]  fieldsStr = fields.split(",");
        		for(int j=0;j<fieldsStr.length;j++){
        			String fName ="";
        			if(fieldsStr[j].indexOf(" AS ") >0){
        				fName = fieldsStr[j].split(" AS ")[1];
        			}
        			if(fieldsStr[j].indexOf(" as ") >0){
        				fName = fieldsStr[j].split(" as ")[1];
        			}else{
        				fName = fieldsStr[j];
        			}
        			 if(resultSet.getObject(fName)!=null){
        				 String vcode = isConvent.get(fName);
        				 if(vcode!=null && !"".equals(vcode)){
        					 row.put(fName,new String((byte[])resultSet.getObject(fName),vcode)); 
        				 }else{
        					 row.put(fName,resultSet.getObject(fName)); 
        				 }
                     }else{
                    	 row.put(fName,""); 
                     }
        		}
        		rows.add(row);
        	}
        	ret.put("rows", rows);
            return  ret.toString();
            
        } catch (SQLException e) {
            log.error(e);
            return null;
            
        }finally {
            try {
                if (resultSet != null) {
                	resultSet.close();
                }
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }
    
    public int queryOfRows(String objectName,String content) {
		ResultSet resultSet = null;
	    int       totalRows = 0;
	    JSONObject filter   = null;
	    try {
	    	JSONObject cont = JsonObjectTools.getJSObj(content);
	    	filter = JsonObjectTools.getJSObj(cont.getString("filter"));
	    	
	        StringBuffer sql           = new StringBuffer();
	        sql.append("select count(*)");
	        
	        sql.append(" from ");
	        sql.append(objectName);
	        sql.append(" where 1=1");
        	StringBuffer where = new StringBuffer();
        	for (Iterator itr = filter.keySet().iterator(); itr.hasNext();) {
        		String whereName  = (String) itr.next();
                String whereValue = (String) filter.get(whereName);
                if(!"".equals(whereValue)){
                	where.append(" and ").append(whereName+"="+whereValue);
                }
        	}
        	sql.append(where);
	        resultSet = mStatement.executeQuery(sql.toString());
	        while(resultSet.next()){
	        	totalRows = resultSet.getInt(1);
	         }
	        
	        return  totalRows;
	        
	    } catch (Exception e) {
	        log.error(e);
	        return 0;
	        
	    }
	  }
    
    private String queryTree(String fields,ResultSet resultSet,Hashtable<String,String> isConvent) throws Exception{
    	
    	while(resultSet.next()){
    		JSONObject row = new JSONObject();
    		String[]  fieldsStr = fields.split(",");
    		for(int j=0;j<fieldsStr.length;j++){
    			String fName ="";
    			if(fieldsStr[j].indexOf(" AS ") >0){
    				fName = fieldsStr[j].split(" AS ")[1];
    			}
    			if(fieldsStr[j].indexOf(" as ") >0){
    				fName = fieldsStr[j].split(" as ")[1];
    			}else{
    				fName = fieldsStr[j];
    			}
    			 if(resultSet.getObject(fName)!=null){
    				 String vcode = isConvent.get(fName);
    				 if(vcode!=null && !"".equals(vcode)){
    					 row.put(fName,new String((byte[])resultSet.getObject(fName),vcode)); 
    				 }else{
    					 row.put(fName,resultSet.getObject(fName)); 
    				 }
                 }else{
                	 row.put(fName,""); 
                 }
    		}
    		//rows.add(row);
    	}
    	return null;
    }

    public void setDataSource(Statement statement) {
        this.mStatement = statement;
     }
    public Statement getDataSource() {
        return this.mStatement;
     }
}
