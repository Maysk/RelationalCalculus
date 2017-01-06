package trcToSql.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import trcToSql.visitors.utils.ErrorLog;




public class ScopeManager {
	ErrorLog errorLog;
	HashMap<String, HashSet<String>> dbSchema;
	HashMap<String, String> tupleTypes; // Nome da tupla, relação
	HashMap<String , ArrayList<String>> waitingDeclaration; //Nome da tupla, Atributos
	HashSet<String> namesUsedOnInnerScopes; //Nomes de variaveis utilizados em escopos mais internos
	ArrayList<ScopeManager> innerScopes;
	String freeVariableName;
	int relationScopeCounter;
	ScopeManager parentScope;
	
	boolean initFlag;
	
	
	public ScopeManager(HashMap<String, HashSet<String>> dbSchema, ErrorLog errorLog){
		this.dbSchema = dbSchema;
		this.errorLog = errorLog;
		this.initFlag = true;
	}
	
	private ScopeManager(ScopeManager parentScope, HashMap<String, HashSet<String>> dbSchema, ErrorLog errorLog){
		this.dbSchema = dbSchema;
		this.errorLog = errorLog;
		this.tupleTypes = new HashMap<String, String>(); 
		this.waitingDeclaration = new HashMap<String, ArrayList<String>>();
		this.innerScopes = new ArrayList<ScopeManager>();
		this.namesUsedOnInnerScopes = new HashSet<String>();
		this.parentScope = parentScope;
		this.freeVariableName = null;
		this.relationScopeCounter = 0;
		this.initFlag = false;
	}	
	
	
	
	
	public ScopeManager beginScope(){
		ScopeManager sm;
		
		if(this.initFlag){
			sm = new ScopeManager(null, this.dbSchema, this.errorLog);
		}
		else{
			sm = new ScopeManager(this, this.dbSchema, this.errorLog);
			this.addInnerScope(sm);
		}
		
		return sm;
	}
	
	public ScopeManager endScope(){
		ScopeManager ps = null;
		
		if(this.parentScope != null){
			ps = this.parentScope;
			
			//Operacoes realizadas para escopo de Exists x
			if(freeVariableName!=null){
				String freeVariableType = tupleTypes.get(freeVariableName);
				if(freeVariableType!=null){
					ArrayList<String> temp = waitingDeclaration.get(freeVariableName);
					if(temp != null){
						HashSet<String> atributosDaRelacao = dbSchema.get(freeVariableType);
						if(atributosDaRelacao == null){
							errorLog.addScopeError("RelationNotFound: A relacao " + freeVariableType + " atrelada a tupla " + freeVariableName + " não existe!");
						}
						else{
							for(String i:temp){
								if(!atributosDaRelacao.contains(i)){
									errorLog.addScopeError("AttrNotFound: A tupla " + freeVariableName + " atrelada a tabela " + freeVariableType + "não possui o atributo " + i );
								}
							}
						}
					}
					
				}
				else{
					errorLog.addScopeError("TupleNotBinded: A tupla " + freeVariableName + " não foi atrelada nenhuma relação!");
				}
				waitingDeclaration.remove(freeVariableName);
				//tupleTypes.remove(freeVariableName);
			}
			
			
			Set<String> waitingTuples = waitingDeclaration.keySet();
			for(String waitingTuple: waitingTuples){
				String tupleType = this.tupleTypes.get(waitingTuple);
				if(tupleType != null){
					//this.tupleTypes.remove(waitingTuple);
					HashSet<String> typeAttr = dbSchema.get(tupleType);
					if(typeAttr == null){
						errorLog.addScopeError("RelationNotFound: A relacao " + tupleType + " atrelada a tupla " + waitingTuple + " não existe!");
					}
					else{
						ArrayList<String> attributesWaiting = this.waitingDeclaration.get(waitingTuple);
						this.waitingDeclaration.remove(waitingTuple);
						for(String i: attributesWaiting){
							if(!dbSchema.get(tupleType).contains(i)){
								errorLog.addScopeError("AttrNotFound: A tupla " + waitingTuple + " atrelada a tabela " + tupleType + "não possui o atributo " + i );
							}
						}
						
					}
				}
				
			
			}
			
			
			
			//Nomes de tuplas utilizados no escopo atual
			ps.namesUsedOnInnerScopes.addAll(this.tupleTypes.keySet());
			
			for(String k:this.waitingDeclaration.keySet()){
				if(ps.waitingDeclaration.containsKey(k)){
					ps.waitingDeclaration.get(k).addAll(this.waitingDeclaration.get(k));
				}
				else{
					ps.waitingDeclaration.put(k, this.waitingDeclaration.get(k));
				}
			}
			
		}
		else{
			
			Set<String> waitingTuples = waitingDeclaration.keySet();
			for(String waitingTuple: waitingTuples){
				String tupleType = this.tupleTypes.get(waitingTuple);
				if(tupleType != null){
					//this.tupleTypes.remove(waitingTuple);
					HashSet<String> typeAttr = dbSchema.get(tupleType);
					if(typeAttr == null){
						errorLog.addScopeError("RelationNotFound: A relacao " + tupleType + " atrelada a tupla " + waitingTuple + " não existe!");
					}
					else{
						ArrayList<String> attributesWaiting = this.waitingDeclaration.get(waitingTuple);
						this.waitingDeclaration.remove(waitingTuple);
						for(String i: attributesWaiting){
							if(!dbSchema.get(tupleType).contains(i)){
								errorLog.addScopeError("AttrNotFound: A tupla " + waitingTuple + " atrelada a tabela " + tupleType + "não possui o atributo " + i );
							}
						}
						
					}
				}
				else{
					errorLog.addScopeError("TupleNotBinded: A tupla " + waitingTuple + " não foi atrelada nenhuma relação!");
				}
				
			
			}
			
			if(this.tupleTypes.isEmpty()){
				errorLog.addFormulaError("OutOfScopeFormula: A formula principal deve conter pelo menos uma tupla atrelada a uma relação!");
			};
		}
		
		return ps;
	}
	
	public void addInnerScope(ScopeManager inner){
		this.innerScopes.add(inner);
	}
	
	public boolean bindTupleToRelation(String tuple, String relation){
		if(this.lookupSymbol(tuple) != null){
			errorLog.addScopeError("TuplesMultipleBinding: A tupla " + tuple + " está gerando conflito.");
			return false;
		}
		else if(this.namesUsedOnInnerScopes.contains(tuple)){
			errorLog.addScopeError("TuplesMultipleBinding: A tupla " + tuple + " está gerando conflito.");
			return false;
		}
		else if(this.dbSchema.get(relation) == null){
			errorLog.addScopeError("RelationNotFound: A relacao " + relation + " atrelada a tupla " + tuple + " não existe!");
			return false;
		}
		else{
			this.tupleTypes.put(tuple, relation);
			this.relationScopeCounter++;
			return true;
		}
		
		
	}
	
	
	//Verifica se a tuple já foi atrelada a algumar relação
	public void checkTupleAtribute(String tuple, String atribute){
		String temp = lookupSymbol(tuple);
		if(temp!=null){
			if(!dbSchema.get(temp).contains(atribute)){
				errorLog.addScopeError("AttrNotFound: A tupla " + tuple + " atrelada a tabela " + temp + "não possui o atributo " + atribute );
			}
		}else{
			if(waitingDeclaration.get(tuple)==null){
				waitingDeclaration.put(tuple, new ArrayList<String>());
			}
			
			waitingDeclaration.get(tuple).add(atribute);
		}
	}
	
	//Verifica se o simbolo já foi atrelado a alguma relação em um escopo acima;
	public String lookupSymbol(String s){
		String temp = tupleTypes.get(s);
		
		if(temp == null && this.parentScope != null){
			temp = this.parentScope.lookupSymbol(s);
		}
		
		return temp;
	}
	
}
