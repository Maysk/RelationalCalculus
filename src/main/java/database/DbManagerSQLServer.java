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

public class DbManagerSQLServer extends DbManager {
	private String userName;
	private String password;
	private String hostName;
	private String port;

	public DbManagerSQLServer(String userName, String password, String hostName, String port) {
		super();
		this.userName = userName;
		this.password = password;
		this.hostName = hostName;
		this.port = port;
		
	}

	@Override
	public HashMap<String, Object> executeSQLQuery(String dbName, String sql)
			throws ClassNotFoundException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		String url = "jdbc:sqlserver://" + hostName + ":" + port + ";databaseName=" + dbName;
		Connection connection;
		if(userName.trim().equals(""))
			connection = DriverManager.getConnection(url);
		else
			connection = DriverManager.getConnection(url, userName, password);
		
		HashMap<String, Object> result = executeSQLQuery(connection, dbName, sql);
		connection.close();
		return result;
	}

	@Override
	public HashMap<String, HashSet<String>> getDbSchema(String dbName) throws SQLException, ClassNotFoundException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		String url = "jdbc:sqlserver://" + hostName + ":" + port + ";databaseName=" + dbName;
		Connection connection;
		if(userName.trim().equals(""))
			connection = DriverManager.getConnection(url);
		else
			connection = DriverManager.getConnection(url, userName, password);

		HashMap<String, HashSet<String>> result;

		HashMap<String, HashSet<String>> tablesAndColunms;
		tablesAndColunms = new HashMap<String, HashSet<String>>();
		PreparedStatement p = connection.prepareStatement(
				"SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_CATALOG='"
						+ dbName + "'");
		ResultSet rsTables = p.executeQuery();

		while (rsTables.next()) {
			String tableName = rsTables.getString(1);
			HashSet<String> colunms = new HashSet<String>();

			p = connection.prepareStatement("SELECT * FROM " + tableName + " LIMIT 1");

			ResultSetMetaData rsColunms = p.executeQuery().getMetaData();
			for (int i = 1; i <= rsColunms.getColumnCount(); i++) {
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
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String url = "jdbc:sqlserver://" + hostName + ":" + port + ";databaseName=master";
			if(userName.trim().equals(""))
				connection = DriverManager.getConnection(url);
			else
				connection = DriverManager.getConnection(url, userName, password);
			PreparedStatement p = connection.prepareStatement(
					"SELECT name FROM sys.databases WHERE name NOT IN ('master', 'model', 'tempdb', 'msdb', 'Resource')");
			ResultSet rsTables = p.executeQuery();

			while (rsTables.next()) {
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

	@Override
	public boolean testConnection() {
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String url = "jdbc:sqlserver://" + hostName + ":" + port + ";databaseName=master";;
			System.out.println(url);
			Connection connection;
			if(userName.trim().equals(""))
				connection = DriverManager.getConnection(url);
			else
				connection = DriverManager.getConnection(url, userName, password);
			
			connection.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
