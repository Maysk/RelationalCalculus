package trcQueryElements;
public class AtomicFormulaIsA extends AtomicFormula{
	String table;
	String tuple;
	public AtomicFormulaIsA(String table, String tuple){
		this.table = table;
		this.tuple = tuple;
	}
}