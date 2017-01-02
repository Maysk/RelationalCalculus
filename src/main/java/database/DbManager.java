package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DbManager {
	private static DbManager uniqueInstance;
	private ArrayList <String> availablesDbs;
	HashMap<String, HashMap<String, ArrayList<String>>> dbsSchemas;
	
	private DbManager(){
		availablesDbs = new ArrayList<>();
		dbsSchemas = new HashMap<String, HashMap<String, ArrayList<String>>>();
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
	}
	
	public ArrayList<String> getAvailablesDbs(){
		return availablesDbs;
	}
	
	
	public HashMap<String, ArrayList<String>> getDbSchema(String dbName) throws SQLException, ClassNotFoundException {
		HashMap<String, ArrayList<String>> result = dbsSchemas.get(dbName);
		if (result == null){
			Connection c;
			HashMap<String, ArrayList<String>> tablesAndColunms;
		
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:databases\\"+dbName + ".db");
			tablesAndColunms = new HashMap<String, ArrayList<String>>();
			
			PreparedStatement p = c.prepareStatement("SELECT distinct (name) FROM sqlite_master WHERE type = 'table'");
			ResultSet rsTables = p.executeQuery();
			
			while(rsTables.next()){
				String tableName = rsTables.getString(1);
				ArrayList <String> colunms = new ArrayList<String>();
				
				p = c.prepareStatement("SELECT * FROM "+ tableName + " LIMIT 1");
				
				ResultSetMetaData rsColunms = p.executeQuery().getMetaData();
				for(int i =1; i<=rsColunms.getColumnCount(); i++){
					colunms.add(rsColunms.getColumnLabel(i));
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
