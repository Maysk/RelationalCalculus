package trcQueryElements;
public class Implication extends Formula{
	Formula f1;
	Formula f2;
	public Implication(Formula f1, Formula f2){
		this.f1 = f1;
		this.f2 = f2;
	}
}