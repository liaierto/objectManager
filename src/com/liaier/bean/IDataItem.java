package com.liaier.bean;

import java.sql.Blob;
import java.util.Date;

import net.sf.json.JSONObject;

public interface IDataItem {
    
    public void setValue(Object pClomnName,Object pClomnValue);
    
    public Object getValue(Object pClomnName);
    public int    getInt(String pClomnName);
    public String getString(String pClomnName);
    public Integer getInteger(String pClomnName);
    public double getdouble(String pClomnName);
    public Double getDouble(String pClomnName);
    public float  getfloat(String pClomnName);
    public Float  getFloat(String pClomnName);
    public Date   getDate(String pClomnName);
    public long   getlong(String pClomnName);
    public Long   getLong(String pClomnName);
    public Blob   getBlob(String pClomnName);
    public String getStrOfBlob(String pClomnName);
    public boolean modifyKey(String pOldKey,String pNewKey);
    public JSONObject getJsonRow();
}
