package trcQueryElements;
import visitors.*;

public class Constant{
	public String c;
	public Constant(String c){
		this.c = c;
	}

	public void accept(Visitor v){
		v.visit(this);
	}
	
}