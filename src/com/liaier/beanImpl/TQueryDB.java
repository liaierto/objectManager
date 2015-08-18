package com.liaier.beanImpl;

import java.util.Hashtable;

import com.liaier.bean.IQueryDB;


public class TQueryDB implements IQueryDB{
    
    private String[]  mSelectFields  = null;
    private String[]  mTableNames    = null;
    private Hashtable mWhereSql      =  new Hashtable();
            
    private String    mOrderSql      = null; 
    
    private String    mJoinOnField   = null;
    
    public void setQueryField(String[] pSelectFields) {
        
        mSelectFields = pSelectFields;
        
    }

    public void setTableName(String[] pTableNames) {
        mTableNames = pTableNames;
    }

    public void setOrderSql(String pOrderSql) {
        
        mOrderSql = pOrderSql;
    }

    public String[] getQueryField() {
        return mSelectFields;
    }

    public String[] getTableName() {
        return mTableNames;
    }

    public Hashtable getWhereSql() {
        return mWhereSql;
    }

    public String getOrderSql() {
        return mOrderSql;
    }

    public void setWhereFileds(String pName,String pVal) {
        String pString = "'"+pVal+"'";
        mWhereSql.put(pName, pString);
        
    }

    public void setWhereSqlFileds(String pName,String pVal) {
        mWhereSql.put(pName, pVal);
        
    }
    
    
    
    public static TQueryDB getInstance(){
        return new TQueryDB();
    }

	public void setJoinOnField(String pName) {
		mJoinOnField = pName;
		
	}

	public String getJoinOnField() {
		return mJoinOnField;
	}

}
