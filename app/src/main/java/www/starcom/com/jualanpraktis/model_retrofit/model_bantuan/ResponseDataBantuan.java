package www.starcom.com.jualanpraktis.model_retrofit.model_bantuan;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseDataBantuan{

	@SerializedName("data")
	private List<DataItem> data;

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"ResponseDataBantuan{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}