package trcQueryElements;
import visitors.*;

public class AtomicFormulaAttOpConst extends Formula{
	public TupleProjection t;
	public Constant c;
	public AtomicFormulaAttOpConst(TupleProjection t, Constant c){
		this.t = t;
		this.c = c;
	}

	public void accept(Visitor v){
		v.visit(this);
	}
	
	public Formula accept(VisitorFormula v){
		return v.visit(this);
	}
}