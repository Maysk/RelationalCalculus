package main.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import main.model.ObjRequest;
import main.model.ObjResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import trcToSql.trcGrammar.ParseException;
import trcToSql.trcGrammar.TrcGrammar;
import trcToSql.trcQueryElements.Query;
import trcToSql.visitors.VisitorSQLNF;
import trcToSql.visitors.VisitorToSQL;
import trcToSql.visitors.VisitorToString;

@RestController
public class TrcController {
	@RequestMapping(value = "/trc/converttosqlnf", method = RequestMethod.POST)
    public ObjResponse<ArrayList<String>> greeting(@RequestBody ObjRequest objModel) throws ParseException {
		TrcGrammar parser = new TrcGrammar(new ByteArrayInputStream(objModel.getRequestBody().getBytes()));
		Query p = parser.query(); 
		VisitorToString v = new VisitorToString();
		VisitorToSQL vSql = new VisitorToSQL();
		
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
