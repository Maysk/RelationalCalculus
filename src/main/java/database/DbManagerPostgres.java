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
import java.util.Properties;

public class DbManagerPostgres extends DbManager{
	private String userName;
	private String password;
	private String hostName;
	
	public DbManagerPostgres(String userName, String password, String hostName){
		super();
	}
	
	@Override
	public HashMap<String, Object> executeSQLQuery(String dbName, String sql)
			throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://"+hostName+"/"+ dbName;
		Properties props = new Properties();
		props.setProperty("user", userName);
		props.setProperty("password", password);	
		Connection connection = DriverManager.getConnection(url, props);
		HashMap<String, Object> result = executeSQLQuery(connection, dbName, sql);
		connection.close();
		return result;
	}

	@Override
	public HashMap<String, HashSet<String>> getDbSchema(String dbName) throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://"+hostName+"/"+ dbName;
		Properties props = new Properties();
		props.setProperty("user", userName);
		props.setProperty("password", password);	
		Connection connection = DriverManager.getConnection(url, props);

		HashMap<String, HashSet<String>> result;
		
		HashMap<String, HashSet<String>> tablesAndColunms;
		tablesAndColunms = new HashMap<String, HashSet<String>>();
		PreparedStatement p = connection.prepareStatement("select table_name from information_schema.tables where table_schema = 'public'");
		ResultSet rsTables = p.executeQuery();
		
		while(rsTables.next()){
			String tableName = rsTables.getString(1);
			HashSet <String> colunms = new HashSet<String>();
			
			p = connection.prepareStatement("SELECT * FROM "+ tableName + " LIMIT 1");
			
			ResultSetMetaData rsColunms = p.executeQuery().getMetaData();
			for(int i =1; i<=rsColunms.getColumnCount(); i++){
				colunms.add(rsColunms.getColumnLabel(i));
			}
			tablesAndColunms.put(tableName, colunms);
		}
		
		rsTables.close();
		result = tablesAndColunms;
		p.close();
		connection.close();
		
		return result;
	}

	@Override
	public ArrayList<String> getAvailablesDbs() {
		ArrayList<String> result = new ArrayList<>();
		Connection connection;
		try {
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://"+hostName+"/postgres";
			Properties props = new Properties();
			props.setProperty("user", userName);
			props.setProperty("password", password);	
			connection = DriverManager.getConnection(url, props);
			PreparedStatement p = connection.prepareStatement("SELECT datname FROM pg_database WHERE datistemplate = false and datname != 'postgres'");
			ResultSet rsTables = p.executeQuery();
			
			while(rsTables.next()){
				String databaseName = rsTables.getString(1);
				result.add(databaseName);
			}
			
			rsTables.close();
			p.close();
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

}
