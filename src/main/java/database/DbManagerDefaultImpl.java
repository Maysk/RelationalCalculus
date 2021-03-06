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

public class DbManagerDefaultImpl extends DbManager{
	
	public DbManagerDefaultImpl(){
		super();
	}

	@Override
	public HashMap<String, Object> executeSQLQuery(String dbName, String sql) throws ClassNotFoundException, SQLException {
		Connection c;
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:databases\\" + dbName + ".db");
		HashMap<String, Object> result = this.executeSQLQuery(c, dbName, sql);
		c.close();
		return result;
	}

	@Override
	public HashMap<String, HashSet<String>> getDbSchema(String dbName) throws SQLException, ClassNotFoundException {
		Connection c;
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:databases\\"+dbName + ".db");		
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
		c.close();
		return result;
	}

	@Override
	public ArrayList<String> getAvailablesDbs() {
		ArrayList <String> availablesDbs = new ArrayList<>();
		availablesDbs.add("UIBK - R, S, T");
		availablesDbs.add("Kemper Datenbanksysteme");
		availablesDbs.add("Database Systems The Complete Book - Exercise 2 4 1");
		return availablesDbs;
	}

	@Override
	public boolean testConnection() {
		return true;
	}
	
	
}
