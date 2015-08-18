package com.liaier.service;


import java.sql.Statement;

import com.liaier.bean.IFormData;
import com.liaier.bean.InputForm;
import com.liaier.bean.IQueryDB;
import com.liaier.dao.TDao;
import com.liaier.dao.interfaces.IDao;
import com.liaier.service.interfaces.IService;

public class TService implements IService {
	private IDao mDao;
	
	public TService(){
		mDao = new TDao();
	}
	public boolean insert(InputForm inputForm) {
		
		return this.mDao.insert(inputForm);
	}

	public boolean delete(InputForm inputForm) {
		return this.mDao.delete(inputForm);
	}

	public boolean update(InputForm inputForm) {
		return this.mDao.update(inputForm);
	}

	public IFormData query(IQueryDB queryDBManager) {
		return this.mDao.query(queryDBManager);
	}

	public IFormData queryByPage(IQueryDB queryDBManager, Integer pageRow,int curentPage) {
		return this.mDao.queryByPage(queryDBManager, pageRow, curentPage);
	}

	public int queryOfRows(IQueryDB queryDBManager) {
		return this.mDao.queryOfRows(queryDBManager);
	}
	
	public boolean excute(String sql) {
		return this.mDao.excute(sql);
	}
	
//	public void setDao(IDao dao){
//		this.mDao = dao;
//	}
//	public IDao getDao(){
//		return this.mDao;
//	}
	
	public void setStatement(Statement statement){
	   this.mDao.setDataSource(statement);
    }
	
}
