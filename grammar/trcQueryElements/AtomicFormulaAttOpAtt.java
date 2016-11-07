package trcQueryElements;
public class AtomicFormulaAttOpAtt extends Formula{
	TupleProjection t1;
	TupleProjection t2;
	public AtomicFormulaAttOpAtt(TupleProjection t1, TupleProjection t2){
		this.t1 = t1;
		this.t2 = t2;
	}
}