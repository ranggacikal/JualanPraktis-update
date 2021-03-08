package www.starcom.com.jualanpraktis.model_retrofit;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseDataFavorite{

	@SerializedName("dataFavorite")
	private List<DataFavoriteItem> dataFavorite;

	public void setDataFavorite(List<DataFavoriteItem> dataFavorite){
		this.dataFavorite = dataFavorite;
	}

	public List<DataFavoriteItem> getDataFavorite(){
		return dataFavorite;
	}

	@Override
 	public String toString(){
		return 
			"ResponseDataFavorite{" + 
			"dataFavorite = '" + dataFavorite + '\'' + 
			"}";
		}
}