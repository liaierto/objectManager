package com.liaier.beanImpl;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.liaier.bean.IDataItem;



public class TDataItem implements IDataItem {
	private static Log log = LogFactory.getLog(TDataItem.class);
    Map mMap = new HashMap();
    public void setValue(Object clomnName, Object clomnValue) {
        mMap.put(clomnName, clomnValue);
    }

    public Object getValue(Object clomnName) {
        Object value = mMap.get(clomnName);
        return value;
    }

    public int getInt(String clomnName) {
        Integer value = getInteger(clomnName);
        return value.intValue();
    }

    public String getString(String clomnName) {
        String value = (String) getValue(clomnName);
        return value;
    }

    public Integer getInteger(String clomnName) {
        Integer value = (Integer) getValue(clomnName);
        return value;
    }

    public double getdouble(String clomnName) {
        Double value = getDouble(clomnName);
        return value.doubleValue();
    }

    public Double getDouble(String clomnName) {
        Double value = (Double) getValue(clomnName);
        return value;
    }

    public float getfloat(String clomnName) {
        Float value = getFloat(clomnName);
        return value.floatValue();
    }

    public Float getFloat(String clomnName) {
        Float value = (Float) getValue(clomnName);
        return value;
    }

    public Date getDate(String clomnName) {
        Date value = (Date) getValue(clomnName);
        return value;
    }

    public long getlong(String clomnName) {
        Long value = getLong(clomnName);
        return value.longValue();
    }

    public Long getLong(String clomnName) {
        Long value = (Long) getValue(clomnName);
        return value;
    }

    public Blob getBlob(String pClomnName) {
        Blob value = (Blob) getValue(pClomnName);
        return value;
    }

   //��һ�����ת����JSON
    public JSONObject getJsonRow() {
        JSONObject row = new JSONObject();
        for (Iterator itr = mMap.keySet().iterator(); itr.hasNext();) {
            String columnName = (String) itr.next();
            Object columnValue = mMap.get(columnName);
            row.put(columnName, columnValue);
        }
        return row;
    }
    public boolean modifyKey(String oldKey,String newKey) {
        Object value = mMap.get(oldKey);
        mMap.remove(oldKey);
        mMap.put(newKey, value);
        return true;
    }
    public static TDataItem getInstance(){
        return new TDataItem();
    }

    public String getStrOfBlob(String clomnName) {
        String value = "";
        try {
            value =  new String((byte[])getValue(clomnName),"utf-8");
        } catch (UnsupportedEncodingException e) {
        	log.error(e);
        }
        
        return value;
    }
}
