package com.liaier.bean;

import net.sf.json.JSONArray;

public interface IFormData {
  
    public void addItem(IDataItem pItem);
    public IDataItem getRowItem(int index);
    public int getRowCount();
    public JSONArray getJsonArrayRows();
    //�����ݿ��е�״ֵ̬������庭��
    public void changeValue(String pKey,String pYes,String pNo);
}
