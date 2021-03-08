package www.starcom.com.jualanpraktis.model_retrofit.bank_lainnya;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseBankLainnya{

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
			"ResponseBankLainnya{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}