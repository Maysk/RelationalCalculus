package visitors;
import trcQueryElements.*;
import java.util.List;
import java.util.ArrayList;

public class VisitorToSQL implements VisitorString{
	List<String> currentTableList;

	public String visit(Query n){
		this.currentTableList = new ArrayList<String>();
		
		String cond = n.f.accept(this);
		
		String s = "SELECT ";
		int i = 0; 
		for(TupleProjection tuple : n.tpl){
			s += tuple.accept(this);
			if(i != n.tpl.size()){
				s += ",";
			}
			i++;
		}

		s += " FROM ";

		i = 0;
		for(String table : this.currentTableList){
			s += table;
			if(i != this.currentTableList.size()){
				s += ", ";
			}
			i++;
		}

		if(cond != null){
			s += " WHERE " + cond;			
		}
		return s;		
	}
	public String visit(And n){
		String s1 = n.f1.accept(this);
		String s2 = n.f2.accept(this);
		String s;

		if(s1 == null && s2 == null){
			s = null; 
		}
		else if (s1 == null){
			s = s2;
		}
		else if (s2 == null){
			s = s1;
		}else {
			s = s1 + " AND " + s2;
		} 

		return s;
	}

	public String visit(Or n){
		String s1 = n.f1.accept(this);
		String s2 = n.f2.accept(this);
		String s;

		if(s1 == null && s2 == null){
			s = null; 
		}
		else if (s1 == null){
			s = s2;
		}
		else if (s2 == null){
			s = s1;
		}else {
			s = s1 + " OR " + s2;
		}

		return s;
	}

	public String visit(Not n){
		String s = n.f.accept(this);
		return  " NOT ( " + s + " ) ";
	}

	public String visit(Exists n){
		List<String> previousTableList = this.currentTableList;
		this.currentTableList = new ArrayList<String>();
		
		String cond = n.f.accept(this);
		
		String s = " EXISTS ( " + "SELECT * FROM ";

		int i = 0;
		for(String t : this.currentTableList){
			s += t;
			if(i != this.currentTableList.size()){
				s += ", ";
			}
			i++;
		}

		if (cond != null){
			s += " WHERE " + cond;
		}

		s +=  " ) ";

		this.currentTableList = previousTableList;
		return s;
	}
	public String visit(InnerFormula n){
		String s = n.f.accept(this);
		return  " ( " + s + " ) ";	
	}
	public String visit(AtomicFormulaAttOpAtt n){
		String s = n.t1.tupleName + "." + n.t1.attribute + " " + n.op + " " + n.t2.tupleName + "." + n.t2.attribute;
		return  " " + s + " ";
	}
	public String visit(AtomicFormulaAttOpConst n){
		String s = n.t.tupleName + "." + n.t.attribute + " " + n.op + " " + n.c.accept(this);
		return  " " + s + " ";
	}
	public String visit(AtomicFormulaIsA n){
		this.currentTableList.add(n.table + " " + n.tuple);
		return null;
	}
	public String visit(TupleProjection n){
		return n.tupleName + "." + n.attribute;
	}
	public String visit(Constant n){
		return n.c;
	}
	

	//This nodes will not appear in SQLNF
	public String visit(ForAll n){
		return null;
	}
	public String visit(Implication n){
		return null;
	}
}