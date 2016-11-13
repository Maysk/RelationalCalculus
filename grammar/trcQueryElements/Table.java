package trcQueryElements;
import visitors.*;

public class Table{
	public String tupleIdentifier;
	public Table(String tableIdentifier){
		this.tupleIdentifier = tupleIdentifier;
	}

	public void accept(Visitor v){
		v.visit(this);
	}
	
}