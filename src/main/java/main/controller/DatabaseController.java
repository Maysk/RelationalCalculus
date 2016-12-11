package main.controller;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import main.model.ObjRequest;
import main.model.ObjResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import trcToSql.trcGrammar.ParseException;
import trcToSql.trcGrammar.TrcGrammar;
import trcToSql.trcQueryElements.Query;
import trcToSql.visitors.VisitorSQLNF;
import trcToSql.visitors.VisitorToString;

@RestController
public class DatabaseController {
	@RequestMapping(value = "/db/listTables/{dbname}", method = RequestMethod.POST)
	public ObjResponse<ArrayList<String>> listTables(@PathVariable("dbname") String dbName){
		
		return null;
	} 

	
	@RequestMapping(value = "/db/{dbname}", method = RequestMethod.POST)
    public ObjResponse<String> testConnection(@PathVariable("dbname") String dbName) throws SQLException, ClassNotFoundException {
			Class.forName("org.sqlite.JDBC");
			Connection c = DriverManager.getConnection("jdbc:sqlite:"+dbName+".db");
			c.close();
			System.out.println("Você conseguiu conectar!! o/");
			return new ObjResponse<String>("OK", "Deu Certo");
		
	}
		
	
}
