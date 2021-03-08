package www.starcom.com.jualanpraktis.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseGetDataBank{

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
			"ResponseGetDataBank{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}