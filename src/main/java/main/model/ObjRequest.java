package main.model;

public class ObjRequest {
	String requestBody;
	
	public ObjRequest(){
		this.setRequestBody("");
	}
	
	public ObjRequest(String value){
		this.setRequestBody(value);
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
	
	
}
