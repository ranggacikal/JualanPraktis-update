package www.starcom.com.jualanpraktis.model_retrofit;

import com.google.gson.annotations.SerializedName;

public class ResponseUpdateRekening{

	@SerializedName("no_rek")
	private String noRek;

	@SerializedName("nama_bank")
	private String namaBank;

	@SerializedName("message")
	private String message;

	@SerializedName("atas_nama")
	private String atasNama;

	public void setNoRek(String noRek){
		this.noRek = noRek;
	}

	public String getNoRek(){
		return noRek;
	}

	public void setNamaBank(String namaBank){
		this.namaBank = namaBank;
	}

	public String getNamaBank(){
		return namaBank;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setAtasNama(String atasNama){
		this.atasNama = atasNama;
	}

	public String getAtasNama(){
		return atasNama;
	}

	@Override
 	public String toString(){
		return 
			"ResponseUpdateRekening{" + 
			"no_rek = '" + noRek + '\'' + 
			",nama_bank = '" + namaBank + '\'' + 
			",message = '" + message + '\'' + 
			",atas_nama = '" + atasNama + '\'' + 
			"}";
		}
}