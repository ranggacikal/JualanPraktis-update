package www.starcom.com.jualanpraktis.model_retrofit.datacheck;

import com.google.gson.annotations.SerializedName;

public class ResponseDataCheck{

	@SerializedName("message")
	private String message;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"ResponseDataCheck{" + 
			"message = '" + message + '\'' + 
			"}";
		}
}