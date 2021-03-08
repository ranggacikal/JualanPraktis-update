package www.starcom.com.jualanpraktis.model;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("bank")
	private String bank;

	@SerializedName("no_rek")
	private String noRek;

	@SerializedName("atas_nama")
	private String atasNama;

	public void setBank(String bank){
		this.bank = bank;
	}

	public String getBank(){
		return bank;
	}

	public void setNoRek(String noRek){
		this.noRek = noRek;
	}

	public String getNoRek(){
		return noRek;
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
			"DataItem{" + 
			"bank = '" + bank + '\'' + 
			",no_rek = '" + noRek + '\'' + 
			",atas_nama = '" + atasNama + '\'' + 
			"}";
		}
}