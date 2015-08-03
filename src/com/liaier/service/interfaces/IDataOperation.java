package com.liaier.service.interfaces;

import java.sql.Statement;

public interface IDataOperation {
	
	String add(String objectName,String content);
	String save(String objectName,String content);
	String delete(String objectName,String content);
	String queryObj(String objectName,String content,String convernt);
	String queryObjPage(String objectName,String content,String convernt);
	void setStatement(Statement statement);
}
