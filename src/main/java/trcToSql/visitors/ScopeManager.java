package trcToSql.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import trcToSql.visitors.utils.Pair;


public class ScopeManager {
	HashMap<String, HashSet<String>> dbSchema;
	HashMap<String, String> tupleTypes; // Nome da tupla, relação
	HashMap<String , ArrayList<String>> waitingDeclaration; //Nome da tupla, Atributos
	HashSet<String> namesUsedOnInnerScopes; //Nomes de variaveis utilizados em escopos mais internos
	ArrayList<ScopeManager> innerScopes;
	String freeVariableName;
	String freeVariableType;
	int relationScopeCounter;
	ScopeManager parentScope;
	
	
	public ScopeManager(ScopeManager parentScope, HashMap<String, HashSet<String>> dbSchema){
		this.tupleTypes = new HashMap<String, String>(); 
		this.waitingDeclaration = new HashMap<String, ArrayList<String>>();
		this.innerScopes = new ArrayList<ScopeManager>();
		this.namesUsedOnInnerScopes = new HashSet<String>();
		this.parentScope = parentScope;
		this.freeVariableName = null;
		this.freeVariableType = null;
		this.relationScopeCounter = 0;
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
			
			//Operacoes realizadas para escopo de Exists x
			if(freeVariableName!=null){
				if(freeVariableType!=null){
					ArrayList<String> temp = waitingDeclaration.get(freeVariableName);
					if(temp != null){
						HashSet<String> atributosDaRelacao = dbSchema.get(freeVariableType);
						if(atributosDaRelacao== null){
							//TODO: Relação não existe
						}
						else{
							
							for(String i:temp){
								if(!atributosDaRelacao.contains(i)){
									//TODO: Elemento pedido não existe na relação
								}
							}
						}
					}
					
				}
				else{
					System.out.println("Variavel livre não foi atrelada a nenhuma relação");
				}
				
			}
			
			//TODO: Atender a waitingList do escopo
			
			
			//Nomes de tuplas utilizados no escopo atual
			ps.namesUsedOnInnerScopes.addAll(this.tupleTypes.keySet());
			
			for(String k:this.waitingDeclaration.keySet()){
				
			}
			
		}
		else{
			if(this.relationScopeCounter == 0){
				//TODO: A consulta principal deve ter pelo menos uma tabela;
			};
			if(!waitingDeclaration.isEmpty()){
				//TODO: Existem tuplas utilizadas não atreladas a nenhuma relação 
			}
		}
		
		return ps;
	}
	
	public void addInnerScope(ScopeManager inner){
		this.innerScopes.add(inner);
	}
	
	public boolean bindTupleToRelation(String tuple, String relation){
		if(this.lookupSymbol(tuple) != null){
			//TODO: Tupla já está atrelada a outra relação
			return false;
		}
		else if(this.namesUsedOnInnerScopes.contains(tuple)){
			//TODO: Tupla já foi atrelada em um escopo mais interno
			return false;
		}
		else{
			this.tupleTypes.put(tuple, relation);
			return true;
		}
		
		
	}
	
	
	//Verifica se a tuple já foi atrelada a algumar relação
	public void checkTupleAtribute(String tuple, String atribute){
		String temp = lookupSymbol(tuple);
		if(temp!=null){
			if(!dbSchema.get(temp).contains(atribute)){}
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
