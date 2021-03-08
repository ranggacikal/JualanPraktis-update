package www.starcom.com.jualanpraktis.model_retrofit.periode;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("end_date")
	private String endDate;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id_date")
	private String idDate;

	@SerializedName("start_date")
	private String startDate;

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public String getEndDate(){
		return endDate;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setIdDate(String idDate){
		this.idDate = idDate;
	}

	public String getIdDate(){
		return idDate;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"end_date = '" + endDate + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id_date = '" + idDate + '\'' + 
			",start_date = '" + startDate + '\'' + 
			"}";
		}
}