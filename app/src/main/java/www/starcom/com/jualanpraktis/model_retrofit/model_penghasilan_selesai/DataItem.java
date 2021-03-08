package www.starcom.com.jualanpraktis.model_retrofit.model_penghasilan_selesai;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("produk")
	private List<ProdukItem> produk;

	@SerializedName("id_transaksi")
	private String idTransaksi;

	public void setProduk(List<ProdukItem> produk){
		this.produk = produk;
	}

	public List<ProdukItem> getProduk(){
		return produk;
	}

	public void setIdTransaksi(String idTransaksi){
		this.idTransaksi = idTransaksi;
	}

	public String getIdTransaksi(){
		return idTransaksi;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"produk = '" + produk + '\'' + 
			",id_transaksi = '" + idTransaksi + '\'' + 
			"}";
		}
}