package www.starcom.com.jualanpraktis.model_retrofit;

import com.google.gson.annotations.SerializedName;

public class ResponseRegister{

	@SerializedName("kode_member")
	private String kodeMember;

	@SerializedName("message")
	private String message;

	@SerializedName("email")
	private String email;

	public void setKodeMember(String kodeMember){
		this.kodeMember = kodeMember;
	}

	public String getKodeMember(){
		return kodeMember;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"ResponseRegister{" + 
			"kode_member = '" + kodeMember + '\'' + 
			",message = '" + message + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}