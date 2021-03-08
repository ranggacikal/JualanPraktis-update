package www.starcom.com.jualanpraktis.model_retrofit.batalkan_pesanan;

import com.google.gson.annotations.SerializedName;

public class ResponseBatalkanPesanan{

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
			"ResponseBatalkanPesanan{" + 
			"message = '" + message + '\'' + 
			"}";
		}
}