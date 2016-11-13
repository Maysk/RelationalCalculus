package trcQueryElements;
import visitors.*;

public class InnerFormula extends Formula{
	public Formula f;
	public InnerFormula(Formula f){
		this.f = f;
	}

	public void accept(Visitor v){
		v.visit(this);
	}
	
	public Formula accept(VisitorFormula v){
		return v.visit(this);
	}
}