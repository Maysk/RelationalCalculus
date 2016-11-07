package trcQueryElements;
public class And extends Formula{
	Formula f1;
	Formula f2;
	public And(Formula f1, Formula f2){
		this.f1 = f1;
		this.f2 = f2;
	}
}