package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class DbManager {
	
	public DbManager(){
	}
	
	HashMap<String, Object> executeSQLQuery(Connection c, String dbName, String sql) throws ClassNotFoundException, SQLException{
		HashMap<String, Object> result = new HashMap<String, Object>();
		PreparedStatement p = c.prepareStatement(sql);
		ResultSet rs = p.executeQuery();
		ResultSetMetaData rsMeta = rs.getMetaData();
		ArrayList <String> colunms = new ArrayList<String>();
		
		for(int i =1; i<= rsMeta.getColumnCount(); i++){
			colunms.add( rsMeta.getColumnLabel(i));
		}
		
		ArrayList<ArrayList<Object>> retrivedTuples = new ArrayList<ArrayList<Object>>(); 
		ArrayList<Object> element = new ArrayList<Object>();
		
		while(rs.next()){
			element = new ArrayList<Object>();
			for(int i = 1; i<= rsMeta.getColumnCount(); i++){
				element.add(rs.getObject(i));
			}
			retrivedTuples.add(element);
		}
		
		result.put("colunms", colunms);
		result.put("retrivedTuples", retrivedTuples);
		
		return result;
	}
	
	HashMap<String, HashSet<String>> getDbSchema(Connection c, String dbName) throws SQLException, ClassNotFoundException{
		HashMap<String, HashSet<String>> result;
	
		HashMap<String, HashSet<String>> tablesAndColunms;
		tablesAndColunms = new HashMap<String, HashSet<String>>();
		PreparedStatement p = c.prepareStatement("SELECT distinct (name) FROM sqlite_master WHERE type = 'table'");
		ResultSet rsTables = p.executeQuery();
		
		while(rsTables.next()){
			String tableName = rsTables.getString(1);
			HashSet <String> colunms = new HashSet<String>();
			
			p = c.prepareStatement("SELECT * FROM "+ tableName + " LIMIT 1");
			
			ResultSetMetaData rsColunms = p.executeQuery().getMetaData();
			for(int i =1; i<=rsColunms.getColumnCount(); i++){
				colunms.add(rsColunms.getColumnLabel(i));
			}
			tablesAndColunms.put(tableName, colunms);
		}
		
		result = tablesAndColunms;
	
		return result;
	}
	
	public abstract HashMap<String, Object> executeSQLQuery(String dbName, String sql) throws ClassNotFoundException, SQLException;
	public abstract HashMap<String, HashSet<String>> getDbSchema(String dbName) throws SQLException, ClassNotFoundException;
	public abstract ArrayList<String> getAvailablesDbs();
	
	
		
	

}
