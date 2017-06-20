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
import java.util.HashSet;

import main.model.ObjRequest;
import main.model.ObjResponse;






import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import database.DbManager;
import database.DbManagerDefaultImpl;
import database.DbManagerPostgres;



@RestController
public class DatabaseController {
	
	//DbManager dbManager = new DbManagerDefaultImpl();
	DbManager dbManager = new DbManagerPostgres("postgres", "260794", "localhost", "5432");
	
	
	@RequestMapping(value = "/db/availables", method = RequestMethod.POST)
    public ObjResponse<ArrayList <String>> availablesDatabases() throws SQLException, ClassNotFoundException {	
		return new ObjResponse<ArrayList <String>>("OK", dbManager.getAvailablesDbs());
	}
	
	@RequestMapping(value = "/executeSQLQuery/{dbname}", method = RequestMethod.POST)
    public ObjResponse<HashMap<String, Object>> executeSQLQuery(@PathVariable("dbname") String dbName, @RequestBody ObjRequest objModel) throws SQLException, ClassNotFoundException {
		String sqlQuery = objModel.getRequestBody();		
		return new ObjResponse<HashMap<String, Object>>("OK", dbManager.executeSQLQuery(dbName, sqlQuery));
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
	public ObjResponse<HashMap<String, HashSet<String>>> listTables(@PathVariable("dbname") String dbName) throws ClassNotFoundException, SQLException{
			
		return new ObjResponse<HashMap<String, HashSet<String>>>("OK", dbManager.getDbSchema(dbName));
	} 

	
}	
