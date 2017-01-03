package main.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import main.model.ObjRequest;
import main.model.ObjResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import database.DbManager;
import trcToSql.trcGrammar.ParseException;
import trcToSql.trcGrammar.TrcGrammar;
import trcToSql.trcQueryElements.Query;
import trcToSql.visitors.VisitorSQLNF;
import trcToSql.visitors.VisitorToSQL;
import trcToSql.visitors.VisitorToString;

@RestController
public class TrcController {
	
	DbManager dbManager = DbManager.getInstance();
	
	@RequestMapping(value = "/trc/converttosqlnf/{dbname}", method = RequestMethod.POST)
    public ObjResponse<ArrayList<String>> greeting(@RequestBody ObjRequest objModel, @PathVariable("dbname") String dbName) throws ParseException, ClassNotFoundException, SQLException {
		TrcGrammar parser = new TrcGrammar(new ByteArrayInputStream(objModel.getRequestBody().getBytes()));
		Query p = parser.query(); 
		
		HashMap<String, HashSet<String>> dbSchema = dbManager.getDbSchema(dbName);
		VisitorToString v = new VisitorToString();
		VisitorToSQL vSql = new VisitorToSQL(dbSchema);
		
		
		
		System.out.println(dbName);
		
		p.accept(new VisitorSQLNF());	
		p.accept(v);
		
		String stringSqlnf = v.stringResult;
		
		String stringSQL = p.accept(vSql);
		
		if(vSql.error){
			System.out.println(vSql.errorMsg);
		}
		
		System.out.println(v.stringResult);        
		System.out.println(stringSQL);
		
		ArrayList<String> test = new ArrayList<String>();
		test.add(stringSqlnf);
		test.add(stringSQL);
		
		return new ObjResponse<ArrayList<String>>("OK", test);
    }
	
	@ExceptionHandler(ParseException.class)
	public ObjResponse<String> parseError(ParseException ex){
		System.out.println(ex.getMessage());
		return new ObjResponse<String>("ERROR", ex.getMessage());
	}
	
	
}
