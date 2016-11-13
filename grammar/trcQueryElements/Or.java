package trcQueryElements;
import visitors.*;

public class Or extends Formula{
	public Formula f1;
	public Formula f2;
	public Or(Formula f1, Formula f2){
		this.f1 = f1;
		this.f2 = f2;
	}

	public void accept(Visitor v){
		v.visit(this);
	}
	
	public Formula accept(VisitorFormula v){
		return v.visit(this);
	}
}