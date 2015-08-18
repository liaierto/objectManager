package com.liaier.beanImpl;

import java.util.Hashtable;

import com.liaier.bean.InputForm;



public class TInputForm implements InputForm{
    Hashtable mHashtable  =  new Hashtable();
    Hashtable mWheretable  =  new Hashtable();
    String    mTableName  ="";
    public TInputForm(){
        
    }
    
    public void setIntVal(String pName,int pVal){
        Integer pVaInteger = pVal;
        mHashtable.put(pName, pVaInteger.toString());
    }
 
    public void setStrVal(String pName,String pVal){
        String pString = "'"+pVal+"'";
        mHashtable.put(pName, pString);
    }
    
    public static TInputForm getInstance(){
        return new TInputForm();
    }

    public  Hashtable getFormInfo(){
        return mHashtable;
    }
    public void setTableName(String pTableName) {
        mTableName = pTableName;
    }
   
    public String getTableName() {
        
        return mTableName;
    }

    public void setWhereFileds(String pName, String pVal) {
        String pString = "'"+pVal+"'";
        mWheretable.put(pName, pString);
        
    }

    public Hashtable getwhereInfo() {
        return mWheretable;
    }

    public void setWhereSqlFileds(String pName, String pVal) {
        mWheretable.put(pName, pVal);
        
    }
}
