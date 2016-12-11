package main.model;

public class ObjResponse <T>{
	private String status;
	private T responseBody;
	
	public ObjResponse(String status, T obj){
		this.setStatus(status);
		this.setResponseBody(obj);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public T getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(T responseBody) {
		this.responseBody = responseBody;
	}

}
