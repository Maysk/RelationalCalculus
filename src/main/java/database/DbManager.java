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

public class DbManager {
	private static DbManager uniqueInstance;
	private ArrayList <String> availablesDbs;
	HashMap<String, HashMap<String, HashSet<String>>> dbsSchemas;
	
	private DbManager(){
		availablesDbs = new ArrayList<>();
		dbsSchemas = new HashMap<String, HashMap<String, HashSet<String>>>();
	}
	
	public static DbManager getInstance() {
		if(uniqueInstance == null){
			uniqueInstance = new DbManager();
			uniqueInstance.initializeAvailableDbs();
			System.out.println("Criou uma instancia de DbManager!!!!!!!!!!");
		}
		return uniqueInstance;
	}
	
	private void initializeAvailableDbs() {
		availablesDbs.add("Teste");
		availablesDbs.add("UIBK - R, S, T");
		availablesDbs.add("Kemper Datenbanksysteme");
		availablesDbs.add("Database Systems The Complete Book - Exercise 2 4 1");
	}
	
	public ArrayList<String> getAvailablesDbs(){
		return availablesDbs;
	}
	
	
	public HashMap<String, Object> executeSQLQuery(String dbName, String sql) throws ClassNotFoundException, SQLException{
		HashMap<String, Object> result = new HashMap<String, Object>();
		Connection c;
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:databases\\"+dbName + ".db");
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
	
	public HashMap<String, HashSet<String>> getDbSchema(String dbName) throws SQLException, ClassNotFoundException {
		HashMap<String, HashSet<String>> result = dbsSchemas.get(dbName);
		if (result == null){
			Connection c;
			HashMap<String, HashSet<String>> tablesAndColunms;
		
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:databases\\"+dbName + ".db");
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
					//rsColunms.getColumnTypeName(i);
				}
				tablesAndColunms.put(tableName, colunms);
			}
			
			c.close();
			dbsSchemas.put(dbName, tablesAndColunms);
			result = tablesAndColunms;
		
		}
		
		return result;
	}
	
	
	
	
}
