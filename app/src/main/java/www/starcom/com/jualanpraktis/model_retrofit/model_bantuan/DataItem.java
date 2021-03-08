package www.starcom.com.jualanpraktis.model_retrofit.model_bantuan;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("icon")
	private String icon;

	@SerializedName("link")
	private String link;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id_bantuan")
	private String idBantuan;

	@SerializedName("platform")
	private String platform;

	public void setIcon(String icon){
		this.icon = icon;
	}

	public String getIcon(){
		return icon;
	}

	public void setLink(String link){
		this.link = link;
	}

	public String getLink(){
		return link;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setIdBantuan(String idBantuan){
		this.idBantuan = idBantuan;
	}

	public String getIdBantuan(){
		return idBantuan;
	}

	public void setPlatform(String platform){
		this.platform = platform;
	}

	public String getPlatform(){
		return platform;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"icon = '" + icon + '\'' + 
			",link = '" + link + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id_bantuan = '" + idBantuan + '\'' + 
			",platform = '" + platform + '\'' + 
			"}";
		}
}