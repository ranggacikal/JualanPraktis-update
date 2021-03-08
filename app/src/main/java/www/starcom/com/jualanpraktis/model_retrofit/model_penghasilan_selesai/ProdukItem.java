package www.starcom.com.jualanpraktis.model_retrofit.model_penghasilan_selesai;

import com.google.gson.annotations.SerializedName;

public class ProdukItem{

	@SerializedName("nama_produk")
	private String namaProduk;

	@SerializedName("tgl_transaksi")
	private String tglTransaksi;

	@SerializedName("untung")
	private String untung;

	@SerializedName("ket2")
	private String ket2;

	public void setNamaProduk(String namaProduk){
		this.namaProduk = namaProduk;
	}

	public String getNamaProduk(){
		return namaProduk;
	}

	public void setTglTransaksi(String tglTransaksi){
		this.tglTransaksi = tglTransaksi;
	}

	public String getTglTransaksi(){
		return tglTransaksi;
	}

	public void setUntung(String untung){
		this.untung = untung;
	}

	public String getUntung(){
		return untung;
	}

	public void setKet2(String ket2){
		this.ket2 = ket2;
	}

	public String getKet2(){
		return ket2;
	}

	@Override
 	public String toString(){
		return 
			"ProdukItem{" + 
			"nama_produk = '" + namaProduk + '\'' + 
			",tgl_transaksi = '" + tglTransaksi + '\'' + 
			",untung = '" + untung + '\'' + 
			",ket2 = '" + ket2 + '\'' + 
			"}";
		}
}