package trcToSql.visitors.utils;

public class Pair {
	private String relationName;
	private int scopeControl;
	
	public Pair(String relationName){
		this.relationName = relationName;
		this.scopeControl = 0;
	}
	
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	public int getScopeControl() {
		return scopeControl;
	}
	public void incrementScopeControl() {
		this.scopeControl++;
	}
	public void decrementScopeControl(){
		this.scopeControl--;
	}
}
