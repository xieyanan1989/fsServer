package com.xunsi.fs.dao;

import org.apache.commons.dbcp.BasicDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection
{
  private static DatabaseConnection instance;
  private static BasicDataSource dataSource;

  private DatabaseConnection()
    throws SQLException, PropertyVetoException
  {
    dataSource = new BasicDataSource();
//    dataSource.setUsername("lbsys");
//    dataSource.setPassword("iH9k87kb");
//    dataSource.setUrl("jdbc:oracle:thin:@10.0.1.100:1521:orcllbs"); 
//    dataSource.setUrl("jdbc:oracle:thin:@115.29.168.217:1521:orcllbs");
   
    dataSource.setUsername("orcgod");
    dataSource.setPassword("iH9k87kb");
    dataSource.setUrl("jdbc:oracle:thin:@47.93.51.99:1521:orclgod"); 
    dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
    dataSource.setInitialSize(2);
    dataSource.setMinIdle(2);
    dataSource.setMaxActive(300);
    dataSource.setMaxIdle(60);
  }

  public static final DatabaseConnection getInstance() {
    if (instance == null) {
      try {
        instance = new DatabaseConnection();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return instance;
  }

  public final synchronized Connection getConnection() {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return conn;
  }
  
  public static void closeCon(PreparedStatement pre,ResultSet result,Connection con) {
		try{
			if (result != null)
				result.close();
			if (pre != null)
				pre.close();
			if (con != null)
				con.close();
		}catch (Exception e){
			e.printStackTrace();
		}

	}
}
