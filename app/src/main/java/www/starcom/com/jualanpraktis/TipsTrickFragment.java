package www.starcom.com.jualanpraktis;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.starcom.com.jualanpraktis.adapter.TipsTrickAdapter;
import www.starcom.com.jualanpraktis.api.ConfigRetrofit;
import www.starcom.com.jualanpraktis.model_retrofit.model_tips.DataItem;
import www.starcom.com.jualanpraktis.model_retrofit.model_tips.ResponseDataTips;

public class TipsTrickFragment extends Fragment {

    RecyclerView rvTipsTrick;

    public TipsTrickFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View roptView = inflater.inflate(R.layout.fragment_tips_trick, container, false);

        rvTipsTrick = roptView.findViewById(R.id.rv_tips_trick);

        loadRecycler();

        return roptView;
    }

    private void loadRecycler() {

        ConfigRetrofit.service.dataTips().enqueue(new Callback<ResponseDataTips>() {
            @Override
            public void onResponse(Call<ResponseDataTips> call, Response<ResponseDataTips> response) {
                List<DataItem> dataItems = response.body().getData();

                if (!dataItems.isEmpty()){

                    TipsTrickAdapter adapter = new TipsTrickAdapter(getActivity(), dataItems);
                    rvTipsTrick.setAdapter(adapter);
                    rvTipsTrick.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rvTipsTrick.setHasFixedSize(true);
                }else{
                    Toast.makeText(getActivity(), "Data Kosong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataTips> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}