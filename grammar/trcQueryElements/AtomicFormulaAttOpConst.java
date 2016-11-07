package trcQueryElements;
public class AtomicFormulaAttOpConst extends Formula{
	TupleProjection t;
	Constant c;
	public AtomicFormulaAttOpConst(TupleProjection t, Constant c){
		this.t = t;
		this.c = c;
	}
}