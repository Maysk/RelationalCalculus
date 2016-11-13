package trcQueryElements;
import visitors.*;

public class AtomicFormulaAttOpAtt extends Formula{
	public TupleProjection t1;
	public TupleProjection t2;
	public AtomicFormulaAttOpAtt(TupleProjection t1, TupleProjection t2){
		this.t1 = t1;
		this.t2 = t2;
	}

	public void accept(Visitor v){
		v.visit(this);
	}
	
	public Formula accept(VisitorFormula v){
		return v.visit(this);
	}
}