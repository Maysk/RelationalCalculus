package trcQueryElements;
import visitors.*;

public class AtomicFormulaIsA extends Formula{
	public String table;
	public String tuple;
	public AtomicFormulaIsA(String table, String tuple){
		this.table = table;
		this.tuple = tuple;
	}

	public void accept(Visitor v){
		v.visit(this);
	}
	
	public Formula accept(VisitorFormula v){
		return v.visit(this);
	}
}