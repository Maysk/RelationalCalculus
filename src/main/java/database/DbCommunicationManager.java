package database;

public class DbCommunicationManager {
	
	public static void setInstance(){
		
	}
	
	public static DbManager getInstance() {
		if(uniqueInstance == null){
			uniqueInstance = new DbManager();
			uniqueInstance.initializeAvailableDbs();
			System.out.println("Criou uma instancia de DbManager!!!!!!!!!!");
		}
		return uniqueInstance;
	}
}
