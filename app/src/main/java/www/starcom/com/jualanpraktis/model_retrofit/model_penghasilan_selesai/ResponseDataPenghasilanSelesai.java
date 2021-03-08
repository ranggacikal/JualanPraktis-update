package www.starcom.com.jualanpraktis.model_retrofit.model_penghasilan_selesai;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseDataPenghasilanSelesai{

	@SerializedName("penghasilan")
	private String penghasilan;

	@SerializedName("data")
	private List<DataItem> data;

	public void setPenghasilan(String penghasilan){
		this.penghasilan = penghasilan;
	}

	public String getPenghasilan(){
		return penghasilan;
	}

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"ResponseDataPenghasilanSelesai{" + 
			"penghasilan = '" + penghasilan + '\'' + 
			",data = '" + data + '\'' + 
			"}";
		}
}