package www.starcom.com.jualanpraktis.model_retrofit.data_check;

import com.google.gson.annotations.SerializedName;

public class ResponseDataCheck{

	@SerializedName("status")
	private String status;

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ResponseDataCheck{" + 
			"status = '" + status + '\'' + 
			"}";
		}
}