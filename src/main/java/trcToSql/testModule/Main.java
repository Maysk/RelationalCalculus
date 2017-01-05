package trcToSql.testModule;
import database.DbManager;
import trcToSql.trcGrammar.*;
import trcToSql.trcQueryElements.*;
import trcToSql.visitors.*;

public class Main{
	public static void main(String[] args) throws Exception{
		TrcGrammar parser = new TrcGrammar(System.in);
 		VisitorToString v = new VisitorToString();
 		
 		Query p = parser.query();

		p.accept(v);		
 		System.out.println("\n" + v.stringResult + "\n"); 
		
		p.accept(new VisitorSQLNF());
		p.accept(v);
		System.out.println("\n" + v.stringResult + "\n"); 

		p.accept(new VisitorScope());
		DbManager dbManager = DbManager.getInstance();
		
		String s = p.accept(new VisitorToSQL(dbManager.getDbSchema("Teste")));
		System.out.println("\n" + s + "\n"); 


	}
}