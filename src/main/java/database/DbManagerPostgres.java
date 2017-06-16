package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

public class DbManagerPostgres extends DbManager{
	private String userName;
	private String password;
	private String hostName;
	public DbManagerPostgres(){
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
		HashMap<String, HashSet<String>> result = getDbSchema(connection, dbName);
		return result;
	}

	@Override
	public ArrayList<String> getAvailablesDbs() {
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://"+hostName+"/postgres";
		Properties props = new Properties();
		props.setProperty("user", userName);
		props.setProperty("password", password);	
		Connection connection = DriverManager.getConnection(url, props);
		
		return null;
	}

}
