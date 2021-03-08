package www.starcom.com.jualanpraktis.model_retrofit.bank_populer;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("role")
	private String role;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("icon")
	private String icon;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id_bank")
	private String idBank;

	@SerializedName("nama_bank")
	private String namaBank;

	public void setRole(String role){
		this.role = role;
	}

	public String getRole(){
		return role;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setIcon(String icon){
		this.icon = icon;
	}

	public String getIcon(){
		return icon;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setIdBank(String idBank){
		this.idBank = idBank;
	}

	public String getIdBank(){
		return idBank;
	}

	public void setNamaBank(String namaBank){
		this.namaBank = namaBank;
	}

	public String getNamaBank(){
		return namaBank;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"role = '" + role + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",icon = '" + icon + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id_bank = '" + idBank + '\'' + 
			",nama_bank = '" + namaBank + '\'' + 
			"}";
		}
}