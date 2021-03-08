package www.starcom.com.jualanpraktis.model_retrofit.model_penggunaan_app;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseListPanduan{

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
			"ResponseListPanduan{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}