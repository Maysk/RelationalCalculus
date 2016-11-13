package trcQueryElements;
import visitors.*;

public class ForAll extends Formula{
	public String tuple;
	public Formula f;
	public ForAll(String tuple, Formula f){
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