package trcQueryElements;
public class Exists extends Formula{
	String tuple;
	Formula f;
	public Exists(String tuple, Formula f){
		this.tuple = tuple;
		this.f = f;
	}
}