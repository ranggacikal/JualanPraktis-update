package www.starcom.com.jualanpraktis.model_retrofit;

import com.google.gson.annotations.SerializedName;

public class DataFavoriteItem{

	@SerializedName("nama_produk")
	private String namaProduk;

	@SerializedName("berat")
	private String berat;

	@SerializedName("kode")
	private String kode;

	@SerializedName("disc")
	private String disc;

	@SerializedName("image_o")
	private String imageO;

	public void setNamaProduk(String namaProduk){
		this.namaProduk = namaProduk;
	}

	public String getNamaProduk(){
		return namaProduk;
	}

	public void setBerat(String berat){
		this.berat = berat;
	}

	public String getBerat(){
		return berat;
	}

	public void setKode(String kode){
		this.kode = kode;
	}

	public String getKode(){
		return kode;
	}

	public void setDisc(String disc){
		this.disc = disc;
	}

	public String getDisc(){
		return disc;
	}

	public void setImageO(String imageO){
		this.imageO = imageO;
	}

	public String getImageO(){
		return imageO;
	}

	@Override
 	public String toString(){
		return 
			"DataFavoriteItem{" + 
			"nama_produk = '" + namaProduk + '\'' + 
			",berat = '" + berat + '\'' + 
			",kode = '" + kode + '\'' + 
			",disc = '" + disc + '\'' + 
			",image_o = '" + imageO + '\'' + 
			"}";
		}
}