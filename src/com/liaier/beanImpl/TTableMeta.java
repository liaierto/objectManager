package com.liaier.beanImpl;

import java.util.HashMap;
import java.util.Map;


public class  TTableMeta {
    private static  TTableMeta mTableMetas = null;
    private static  Map<String,Map<String,String>>  mTableMeta = null;
    public TTableMeta(){
        mTableMeta = new HashMap<String,Map<String,String>>();
    }
    public void setMeta(String pKey,Map<String,String> pVal){
        mTableMeta.put(pKey, pVal);
    }

    public String getMeta(String pKey,String pFieldKey){
        Map<String,String> pVal   = (Map<String,String>) mTableMeta.get(pKey);
        String pFV = (String) pVal.get(pFieldKey);
        return pFV;
    }
    
    public boolean isEmpty(String pKey){
        return mTableMeta.containsKey(pKey);
    }
    
    public static TTableMeta getInstance(){
    	if(mTableMetas == null){
           return new TTableMeta();
    	}
    	return mTableMetas;
    }
}
