package main.controller;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import main.model.ObjResponse;


import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class DatabaseController {
	
	@RequestMapping(value = "/db/availables", method = RequestMethod.POST)
    public ObjResponse<ArrayList <String>> availablesDatabases() throws SQLException, ClassNotFoundException {
			ArrayList <String> availablesDbs = new ArrayList<String>();
			availablesDbs.add("Teste");
			availablesDbs.add("UIBK - R, S, T");
			return new ObjResponse<ArrayList <String>>("OK", availablesDbs);
	}
	
	@RequestMapping(value = "/db/{dbname}", method = RequestMethod.POST)
    public ObjResponse<String> testConnection(@PathVariable("dbname") String dbName) throws SQLException, ClassNotFoundException {
			Class.forName("org.sqlite.JDBC");
			Connection c = DriverManager.getConnection("jdbc:sqlite:databases\\"+dbName+".db");
			c.close();
			System.out.println("Você conseguiu conectar!! o/");
			return new ObjResponse<String>("OK", "Deu Certo");
		
	}
	
	@RequestMapping(value = "/db/listTables/{dbname}", method = RequestMethod.POST)
	public ObjResponse<HashMap<String, ArrayList<String>>> listTables(@PathVariable("dbname") String dbName) throws ClassNotFoundException, SQLException{
		Class.forName("org.sqlite.JDBC");
		Connection c = DriverManager.getConnection("jdbc:sqlite:databases\\"+dbName + ".db");
		HashMap<String, ArrayList<String>> tablesAndColunms = new HashMap<String, ArrayList<String>>();
		
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
		
		System.out.println(tablesAndColunms);
		
		return new ObjResponse<HashMap<String, ArrayList<String>>>("OK", tablesAndColunms);
	} 

	
	
		
	
}	
