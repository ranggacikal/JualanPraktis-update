package www.starcom.com.jualanpraktis.model_retrofit.tukar_pesanan;

import com.google.gson.annotations.SerializedName;

public class ResponseTukarPesanan{

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
			"ResponseTukarPesanan{" + 
			"message = '" + message + '\'' + 
			"}";
		}
}