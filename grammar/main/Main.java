package main;
import trcGrammar.*;
import trcQueryElements.*;
import visitors.*;

public class Main{
	public static void main(String[] args) throws Exception{
		TrcGrammar parser = new TrcGrammar(System.in);
 		VisitorToString v = new VisitorToString();
 		
 		Query p = parser.query();

		p.accept(v);		
 		System.out.println("\n" + v.stringResult + "\n"); 
		
		p.f.accept(new VisitorSQLNF());
		p.accept(v);
		System.out.println("\n" + v.stringResult + "\n"); 

	}
}