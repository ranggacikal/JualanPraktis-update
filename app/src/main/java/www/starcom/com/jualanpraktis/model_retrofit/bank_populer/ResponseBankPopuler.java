package www.starcom.com.jualanpraktis.model_retrofit.bank_populer;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseBankPopuler{

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
			"ResponseBankPopuler{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}