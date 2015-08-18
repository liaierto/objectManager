package com.liaier.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DBUtil{
  private static Log log = LogFactory.getLog(DBUtil.class);
  private static DataSource db = null;   
  public DBUtil(){}

  static {
      try{
    	  
          InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
          Properties props = new Properties();
          props.load(in);
          in.close();
          System.out.println(props);
          db = DruidDataSourceFactory.createDataSource(props);
      }catch(Exception ex){
    	  log.error(ex);
      }
  }

  public static Connection getConnection() {
    try {
      return db.getConnection(); 
      } catch (SQLException e) {
    	  throw new RuntimeException("获取连接失败！", e);
    }
   
  }
}