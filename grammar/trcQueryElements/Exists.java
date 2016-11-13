package trcQueryElements;
import visitors.*;

public class Exists extends Formula{
	public String tuple;
	public Formula f;
	public Exists(String tuple, Formula f){
		this.tuple = tuple;
		this.f = f;
	}
	public void accept(Visitor v){
		v.visit(this);
	}
	
	public Formula accept(VisitorFormula v){
		return v.visit(this);
	}
}