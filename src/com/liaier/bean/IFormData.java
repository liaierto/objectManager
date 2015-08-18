package com.liaier.bean;

import net.sf.json.JSONArray;

public interface IFormData {
  
    public void addItem(IDataItem pItem);
    public IDataItem getRowItem(int index);
    public int getRowCount();
    public JSONArray getJsonArrayRows();
    //将数据库中的状态值赋予具体涵义
    public void changeValue(String pKey,String pYes,String pNo);
}
