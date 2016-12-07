package main.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import main.model.ObjModel;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import trcToSql.trcGrammar.ParseException;
import trcToSql.trcGrammar.TrcGrammar;
import trcToSql.trcQueryElements.Query;
import trcToSql.visitors.VisitorSQLNF;
import trcToSql.visitors.VisitorToString;

@RestController
public class TrcController {
	@RequestMapping(value = "trc/converttosqlnf", method = RequestMethod.POST)
    public String greeting(@RequestBody ObjModel objModel) throws ParseException {
		TrcGrammar parser = new TrcGrammar(new ByteArrayInputStream(objModel.getValue().getBytes()));
		Query p = parser.query(); 
		VisitorToString v = new VisitorToString();
		p.accept(new VisitorSQLNF());
		p.accept(v);
        return v.stringResult;
    }
}
