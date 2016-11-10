package trcQueryElements;
public class ForAll extends Formula{
	String tuple;
	Formula f;
	public ForAll(String tuple, Formula f){
		this.tuple = tuple;
		this.f = f;
	}
}