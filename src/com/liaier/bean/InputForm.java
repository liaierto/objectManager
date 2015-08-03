package com.liaier.bean;

import java.util.Hashtable;

public interface InputForm {
    
    void setTableName(String pTableName);
    void setIntVal(String pName,int pVal);
    void setStrVal(String pName,String pVal);
    void setWhereFileds(String pName,String pVal);
    void setWhereSqlFileds(String pName,String pVal);
    String getTableName();
    Hashtable getFormInfo();
    Hashtable getwhereInfo();
}
