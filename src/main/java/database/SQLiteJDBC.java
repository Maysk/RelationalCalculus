package database;

import java.sql.*;

public class SQLiteJDBC
{
	Connection c;
	
	
  public static void main( String args[] ) throws ClassNotFoundException, SQLException
  {
    SQLiteJDBC t = new SQLiteJDBC("teste.db");
    t.createTables();
    t.c.close();
  }
  
  public SQLiteJDBC(String databaseName) throws SQLException, ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:"+databaseName);
  }
  
  public void createTables() throws SQLException{
	  Statement stmt = this.c.createStatement();
	  String sql = "CREATE TABLE COMPANY " +
              "(ID INT PRIMARY KEY     NOT NULL," +
              " NAME           TEXT    NOT NULL, " + 
              " AGE            INT     NOT NULL, " + 
              " ADDRESS        CHAR(50), " + 
              " SALARY         REAL)"; 
	  stmt.executeUpdate(sql);
	  stmt.close();
  }
}
