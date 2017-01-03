package trcToSql.visitors;

import java.util.ArrayList;
import java.util.HashMap;

import trcToSql.visitors.utils.Pair;


public class ScopeManager {
	HashMap<String, String> tupleTypes; // Nome da tupla, relação
	HashMap<String , ArrayList<String>> waitingDeclaration; //Nome da tupla, Atributos
	ArrayList<ScopeManager> innerScopes;
	String freeVariable;
	ScopeManager parentScope;
	
	
	
	public ScopeManager(ScopeManager parentScope){
		this.tupleTypes = new HashMap<String, String>(); 
		this.waitingDeclaration = new HashMap<String, ArrayList<String>>();
		this.innerScopes = new ArrayList<ScopeManager>();
		this.parentScope = parentScope;
		this.freeVariable = null;
	}
	
	public ScopeManager beginScope(){
		ScopeManager sm = new ScopeManager(this);
		this.addInnerScope(sm);
		return sm;
	}
	
	public ScopeManager endScope(){
		ScopeManager ps = null;
		
		if(this.parentScope != null){
			ps = this.parentScope;
			if(freeVariable!=null){
				if(waitingDeclaration.get(freeVariable) == null){
					System.out.println("Variavel livre não foi atrelada a nenhuma relação");
				}
				else{
					
				}
				
			}
			
			
		}
		
		return ps;
	}
	
	public void addInnerScope(ScopeManager inner){
		this.innerScopes.add(inner);
	}
	
	public void addSymbolToTheScope(){}
	public void addRequestSymbolToWaitingList(){}
	public void searchForDeclaration(String a){}
}
