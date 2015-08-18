package com.liaier.beanImpl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import com.liaier.bean.IDataItem;
import com.liaier.bean.IFormData;

public class TFormData implements IFormData{
    List mList = new ArrayList();
    public void addItem(IDataItem pItem) {
        mList.add(pItem);
    }

    public IDataItem getRowItem(int index) {
        IDataItem pItem = (IDataItem) mList.get(index);
        return pItem;
    }

    public int getRowCount() {
        return mList.size();
    }

    public JSONArray getJsonArrayRows() {
        JSONArray pRows = new JSONArray();
        int pSize = mList.size();
        for(int i=0;i<pSize;i++){
            IDataItem pItem = getRowItem(i);
            pRows.add(pItem.getJsonRow());
        }
        return pRows;
    }

    public static TFormData getInstance(){
        return new TFormData();
    }

	public void changeValue(String pKey,String pYes, String pNo) {
		int pSize = mList.size();
        for(int i=0;i<pSize;i++){
            IDataItem pItem = getRowItem(i);
            String pValue = pItem.getString(pKey);
            if("1".equals(pValue)){
            	pItem.setValue(pKey, pYes);
            }else{
            	pItem.setValue(pKey, pNo);
            }
        }
	}
}
