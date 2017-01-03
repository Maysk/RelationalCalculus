package trcToSql.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import trcToSql.visitors.utils.Pair;


public class ScopeManager {
	HashMap<String, HashSet<String>> dbSchema;
	HashMap<String, String> tupleTypes; // Nome da tupla, relação
	HashMap<String , ArrayList<String>> waitingDeclaration; //Nome da tupla, Atributos
	ArrayList<ScopeManager> innerScopes;
	String freeVariableName;
	String freeVariableType;
	ScopeManager parentScope;
	
	
	
	public ScopeManager(ScopeManager parentScope, HashMap<String, HashSet<String>> dbSchema){
		this.tupleTypes = new HashMap<String, String>(); 
		this.waitingDeclaration = new HashMap<String, ArrayList<String>>();
		this.innerScopes = new ArrayList<ScopeManager>();
		this.parentScope = parentScope;
		this.freeVariableName = null;
		this.freeVariableType = null;
		this.dbSchema = dbSchema;
	}
	
	public ScopeManager beginScope(){
		ScopeManager sm = new ScopeManager(this, this.dbSchema);
		this.addInnerScope(sm);
		return sm;
	}
	
	public ScopeManager endScope(){
		ScopeManager ps = null;
		
		if(this.parentScope != null){
			ps = this.parentScope;
			if(freeVariableType!=null){
				if(waitingDeclaration.get(freeVariableName) != null){
					ArrayList<String> temp = waitingDeclaration.get(freeVariableName);	
					
					for(String i:temp){
						
					}
				
				}
				
			}
			else{
				System.out.println("Variavel livre não foi atrelada a nenhuma relação");
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
