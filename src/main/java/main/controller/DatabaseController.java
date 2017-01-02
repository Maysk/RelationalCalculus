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

import database.DbManager;



@RestController
public class DatabaseController {
	
	DbManager dbManager = DbManager.getInstance();
	
	@RequestMapping(value = "/db/availables", method = RequestMethod.POST)
    public ObjResponse<ArrayList <String>> availablesDatabases() throws SQLException, ClassNotFoundException {	
		return new ObjResponse<ArrayList <String>>("OK", dbManager.getAvailablesDbs());
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
			
		return new ObjResponse<HashMap<String, ArrayList<String>>>("OK", dbManager.getDbSchema(dbName));
	} 

	
	
		
	
}	
