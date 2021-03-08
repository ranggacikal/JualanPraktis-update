package www.starcom.com.jualanpraktis.model_retrofit.model_tips;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("id_panduan")
	private String idPanduan;

	@SerializedName("id_video")
	private String idVideo;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("kategori")
	private String kategori;

	@SerializedName("title")
	private String title;

	@SerializedName("durasi")
	private String durasi;

	public void setIdPanduan(String idPanduan){
		this.idPanduan = idPanduan;
	}

	public String getIdPanduan(){
		return idPanduan;
	}

	public void setIdVideo(String idVideo){
		this.idVideo = idVideo;
	}

	public String getIdVideo(){
		return idVideo;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setKategori(String kategori){
		this.kategori = kategori;
	}

	public String getKategori(){
		return kategori;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setDurasi(String durasi){
		this.durasi = durasi;
	}

	public String getDurasi(){
		return durasi;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"id_panduan = '" + idPanduan + '\'' + 
			",id_video = '" + idVideo + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",kategori = '" + kategori + '\'' + 
			",title = '" + title + '\'' + 
			",durasi = '" + durasi + '\'' + 
			"}";
		}
}