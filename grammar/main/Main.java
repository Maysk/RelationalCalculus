package main;
import trcGrammar.*;
import trcQueryElements.*;
import visitors.*;

public class Main{
	public static void main(String[] args) throws Exception{
		TrcGrammar parser = new TrcGrammar(System.in);
 		Query p = parser.query();
		p.f.accept(new VisitorSQLNF());
		VisitorToString v = new VisitorToString();
		p.accept(v);
		System.out.println("\n" + v.stringResult + "\n"); 
	}
}