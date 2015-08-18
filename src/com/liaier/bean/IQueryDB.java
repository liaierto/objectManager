package com.liaier.bean;

import java.util.Hashtable;

public interface IQueryDB {

    void setQueryField(String[] pSelectFields);
    
    void setTableName(String[] pTableNames);
    
    void setWhereFileds(String pName,String pVal);
   
    void setWhereSqlFileds(String pName,String pVal);
    
    void setOrderSql(String pOrderSql);
    
    void setJoinOnField(String pName);
    
    String[] getQueryField();
    
    String[] getTableName();
    
    Hashtable getWhereSql();
    
    String getOrderSql();
    String getJoinOnField();
}
