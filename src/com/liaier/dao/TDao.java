package com.liaier.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liaier.bean.IFormData;
import com.liaier.bean.InputForm;
import com.liaier.bean.IQueryDB;
import com.liaier.beanImpl.TDataItem;
import com.liaier.beanImpl.TFormData;
import com.liaier.dao.interfaces.IDao;
import com.x.base.meta.TTableMeta;

public class TDao implements IDao {

	private static Log log = LogFactory.getLog(TDao.class);
	private Statement mStatement;
	
	public boolean insert(InputForm inputForm) {
        String tableName = inputForm.getTableName();
        Hashtable tableContent = inputForm.getFormInfo();
        
        if(!isNull(tableName,tableContent)){
            return false;
        }
        try {
            StringBuffer sql         = new StringBuffer();
            StringBuffer columnNames = new StringBuffer();
            StringBuffer values      = new StringBuffer();
            
            sql.append("insert into " + tableName+"(");
            
            for (Iterator itr = tableContent.keySet().iterator(); itr.hasNext();) {
                 String columnName  = (String) itr.next();
                 String columnValue = (String) tableContent.get(columnName);
                 columnNames.append(columnName+",");
                 values.append( columnValue+",");
            }
            
            sql.append(columnNames.deleteCharAt(columnNames.length()-1)+")values("+values.deleteCharAt(values.length()-1)+")");
           
            String Sql = sql.toString();
            log.info(Sql);
            mStatement.executeUpdate(Sql);
            return true;
            
        } catch (Exception e) {
            log.error(e);
            return false;
            
        }

    }

	public boolean delete(InputForm inputForm) {
        String    tableName      =  inputForm.getTableName();
        Hashtable whereContent   =  inputForm.getwhereInfo();
        if (tableName == null) {
            return false;
        }
        try {
            StringBuffer sql  =  new StringBuffer();
            
            sql.append("delete from " + tableName);
            
            sql.append(" where ");
            int j = 0;
            int pSize = whereContent.size();
            for (Iterator itr  = whereContent.keySet().iterator(); itr.hasNext();) {
                String columnName  = (String) itr.next();
                String columnValue = (String) whereContent.get(columnName);
                
                sql.append(columnName+"="+columnValue);
                if(j!=pSize-1){
                    sql.append("and ");
                }
                j=j+1;
                
            }
            String Sql  = sql.toString();
            log.info(Sql);
            int result = mStatement.executeUpdate(Sql);
            if(result==1){
               return true;
            }else{
                return false;
            }
            
            
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

	public boolean update(InputForm inputForm) {
        String    tableName    = inputForm.getTableName();
        Hashtable tableContent = inputForm.getFormInfo();
        Hashtable whereContent = inputForm.getwhereInfo();
        if (!isNull(tableName,tableContent) || whereContent.isEmpty() ) {
            return false;
        }
        try {
            StringBuffer sql  =  new StringBuffer();
            sql.append("update "+ tableName+ " set ");
            int i = 0;
            int Size = tableContent.size();
            for (Iterator itr  = tableContent.keySet().iterator(); itr.hasNext();) {
                String columnName  = (String) itr.next();
                String columnValue = (String) tableContent.get(columnName);
                
                sql.append(columnName+"="+columnValue);
                if(i!=Size-1){
                    sql.append(", ");
                }
                i=i+1;
            }
            
            sql.append(" where ");
            int j = 0;
            int pSize = whereContent.size();
            for (Iterator itr  = whereContent.keySet().iterator(); itr.hasNext();) {
                String columnName  = (String) itr.next();
                String columnValue = (String) whereContent.get(columnName);
                
                sql.append(columnName+"="+columnValue);
                if(j!=pSize-1){
                    sql.append("and ");
                }
                j=j+1;
                
            }
            
            String Sql  = sql.toString();
            log.info(Sql);
            int result = mStatement.executeUpdate(Sql);
            if(result==1){
                return true;
             }else{
                 return false;
             }
            
        } catch (Exception e) {
            log.error(e);
            return false;
            
        }
    }

	public IFormData query(IQueryDB queryDBManager){
		ResultSet         pResultSet    = null;
        TFormData         form            = new TFormData();
        TTableMeta        pMeta         = TTableMeta.getInstance();
        String[]          tablesName      = queryDBManager.getTableName();
        String[]          queryField      = queryDBManager.getQueryField();
        Hashtable         whereContent    = queryDBManager.getWhereSql();
        String            order           = queryDBManager.getOrderSql();
        try {
            StringBuffer  sql             = new StringBuffer();
            sql.append("select ");
            int qFieldLen = queryField.length;
            for(int i=0;i<qFieldLen;i++){
                sql.append(queryField[i]);
                if(i!=qFieldLen-1){
                    sql.append(",");
                }
            }
            sql.append(" from ");
            int pTablesLen = tablesName.length;
            for(int j=0;j<pTablesLen;j++){
                sql.append(tablesName[j]);
                if(j!=pTablesLen-1){
                    sql.append(",");
                }
            }
            if(!whereContent.isEmpty()){
                sql.append(" where ");
                int k = 0;
                int pSize = whereContent.size();
                for (Iterator itr  = whereContent.keySet().iterator(); itr.hasNext();) {
                    String columnName  = (String) itr.next();
                    String columnValue = (String) whereContent.get(columnName);
                    
                    sql.append(columnName+"="+columnValue);
                    if(k!=pSize-1){
                        sql.append(" and ");
                    }
                    k=k+1;
                    
                }
            }
            
            
            if(order!=null && !"".equals(order)){
               sql.append("  order by "+order);
            }
            System.out.println(sql.toString());
            pResultSet = this.mStatement.executeQuery(sql.toString());
            String tableNameStr =  tablesName[0];//��ʱʵ��һ���������������Ĳ��ð�
            if(!pMeta.isEmpty(tableNameStr)){
                Map pTableMap =  new HashMap();
                ResultSetMetaData  pMetaData = pResultSet.getMetaData();
                for(int i=0;i<pMetaData.getColumnCount();i++){
                    String pCName = pMetaData.getColumnName(i+1);
                    String pType = pMetaData.getColumnTypeName(i+1);
                    pTableMap.put(pCName, pType);
                }
                pMeta.setMeta(tableNameStr, pTableMap);
            }
            while(pResultSet.next()){
                TDataItem pItem      = new TDataItem();
                for(int i=0;i<qFieldLen;i++){
                     String pFieldName = queryField[i];
                     if("BLOB".equals(pMeta.getMeta(tableNameStr, pFieldName))){
                         pItem.setValue(pFieldName,new String((byte[])pResultSet.getObject(pFieldName),"utf-8")); 
                     }else{
                         pItem.setValue(pFieldName,pResultSet.getString(pFieldName));
                     }
                }
                form.addItem(pItem);
             }
            return  form;
            
        } catch (Exception e) {
            log.error(e);
            return null;
            
        }
    }

	public IFormData queryByPage(IQueryDB queryDBManager, Integer pageRow,int curentPage) {
		ResultSet         pResultSet    = null;
        IFormData         form            = new TFormData();
        TTableMeta        pMeta         = TTableMeta.getInstance();
        String[]          tablesName      = queryDBManager.getTableName();
        String[]          queryField      = queryDBManager.getQueryField();
        Hashtable         whereContent    = queryDBManager.getWhereSql();
        String            order           = queryDBManager.getOrderSql();
        String            joinfield       = queryDBManager.getJoinOnField();
        try {
            
            StringBuffer sql              = new StringBuffer();
            
            sql.append("select ");
            int qFieldLen = queryField.length;
            for(int i=0;i<qFieldLen;i++){
                sql.append(queryField[i]);
                if(i!=qFieldLen-1){
                    sql.append(",");
                }
            }
            sql.append(" from ");
            if(joinfield !=null && !"".equals(joinfield)){
            	 sql.append(tablesName[0]).append(" left join ").append(tablesName[1]).append(" on ").append(joinfield);
            }else{
	            int pTablesLen = tablesName.length;
	            for(int j=0;j<pTablesLen;j++){
	                sql.append(tablesName[j]);
	                if(j!=pTablesLen-1){
	                    sql.append(",");
	                }
	            }
            }
            if(!whereContent.isEmpty()){
                sql.append(" where ");
                int k = 0;
                int size = whereContent.size();
                for (Iterator itr  = whereContent.keySet().iterator(); itr.hasNext();) {
                    String columnName  = (String) itr.next();
                    String columnValue = (String) whereContent.get(columnName);
                    
                    sql.append(columnName+"="+columnValue);
                    if(k!=size-1){
                        sql.append(" and ");
                    }
                    k=k+1;
                    
                }
            }
            
            
            if(order!=null && !"".equals(order)){
               sql.append("  order by "+order);
            }
            
            //��ҳ��ѯ
            sql.append(" limit "+(pageRow*(curentPage-1))+","+pageRow);
            System.out.println(sql.toString());
            pResultSet =this.mStatement.executeQuery(sql.toString());
            String pTableNameStr =  tablesName[0];//��ʱʵ��һ���������������Ĳ��ð�
            if(!pMeta.isEmpty(pTableNameStr)){
                Map pTableMap =  new HashMap();
                ResultSetMetaData  pMetaData = pResultSet.getMetaData();
                for(int i=0;i<pMetaData.getColumnCount();i++){
                    String pCName = pMetaData.getColumnName(i+1);
                    String pType = pMetaData.getColumnTypeName(i+1);
                    pTableMap.put(pCName, pType);
                }
                pMeta.setMeta(pTableNameStr, pTableMap);
            }
            
            while(pResultSet.next()){
                TDataItem pItem = new TDataItem();
                for(int i=0;i<qFieldLen;i++){
                	 String pFieldName = "";
                	  if(queryField[i].contains(".")){
                		  pFieldName =  queryField[i].split("\\.")[1];
                	  }else{
                		  pFieldName =  queryField[i];
                	  }
                    if("BLOB".equals(pMeta.getMeta(pTableNameStr, pFieldName))){
                        if(pResultSet.getObject(pFieldName)!=null){
                            pItem.setValue(pFieldName,new String((byte[])pResultSet.getObject(pFieldName),"utf-8")); 
                        }else{
                            pItem.setValue(pFieldName,""); 
                        }
                        
                    }else{
                        pItem.setValue(pFieldName,pResultSet.getString(pFieldName));
                    }
                   
                }
                form.addItem(pItem);
             }
            return  form;
            
        } catch (Exception e) {
            log.error(e);
            return null;
            
        }
    }

	public int queryOfRows(IQueryDB queryDBManager) {
		ResultSet         pResultSet    = null;
	    int               totalRows     = 0;
	    String[]          tablesName    = queryDBManager.getTableName();
	    Hashtable         whereContent  = queryDBManager.getWhereSql();
	    String            order         = queryDBManager.getOrderSql();
	    
	    try {
	        StringBuffer sql           = new StringBuffer();
	        sql.append("select count(*)");
	        
	        sql.append(" from ");
	        int tablesLen = tablesName.length;
	        for(int j=0;j<tablesLen;j++){
	            sql.append(tablesName[j]);
	            if(j!=tablesLen-1){
	                sql.append(",");
	            }
	        }
	        if(!whereContent.isEmpty()){
	            sql.append(" where ");
	            int k = 0;
	            int pSize = whereContent.size();
	            for (Iterator itr  = whereContent.keySet().iterator(); itr.hasNext();) {
	                String columnName  = (String) itr.next();
	                String columnValue = (String) whereContent.get(columnName);
	                
	                sql.append(columnName+"="+columnValue);
	                if(k!=pSize-1){
	                    sql.append(" and ");
	                }
	                k=k+1;
	                
	            }
	        }
	        
	        
	        if(order!=null && !"".equals(order)){
	           sql.append("  order by "+order);
	        }
	        
	        pResultSet = this.mStatement.executeQuery(sql.toString());
	        while(pResultSet.next()){
	        	totalRows = pResultSet.getInt(1);
	         }
	        
	        return  totalRows;
	        
	    } catch (Exception e) {
	        log.error(e);
	        return 0;
	        
	    }
	  }

    public void setDataSource(Statement statement) {
       this.mStatement = statement;
    }
    
    private  boolean isNull(String tableName, Hashtable tableContent){
        if (tableName == null || tableContent == null || "".equals(tableName) || tableContent.isEmpty()) {
            return false;
        }
        return true;
    }

	public boolean excute(String sql) {
		try {
			log.info(sql);
			this.mStatement.execute(sql);
			return true;
		} catch (SQLException e) {
			log.error(e);
			return false;
		}
	}
}
