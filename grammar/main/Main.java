package main;
import trcGrammar.*;
import trcQueryElements.*;
import visitors.*;
public class Main{
	public static void main(String[] args) {
		Query q = new TrcGrammar(System.in).query();
		q.f.accept(new VisitorSQLNF());
	}
}