package www.starcom.com.jualanpraktis;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.starcom.com.jualanpraktis.adapter.PenggunaanAppAdapter;
import www.starcom.com.jualanpraktis.api.ConfigRetrofit;
import www.starcom.com.jualanpraktis.model_retrofit.model_penggunaan_app.DataItem;
import www.starcom.com.jualanpraktis.model_retrofit.model_penggunaan_app.ResponseListPanduan;

public class PenggunaanAppFragment extends Fragment {

    RecyclerView rvPanduanApp;

    public PenggunaanAppFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_penggunaan_app, container, false);

        rvPanduanApp = rootView.findViewById(R.id.rv_penggunaan_app);

        loadRecycler();

        return rootView;
    }

    private void loadRecycler() {

        ConfigRetrofit.service.dataPanduan().enqueue(new Callback<ResponseListPanduan>() {
            @Override
            public void onResponse(Call<ResponseListPanduan> call, Response<ResponseListPanduan> response) {
                if (response.isSuccessful()){



                    List<DataItem> dataItemList = response.body().getData();

                    if (!dataItemList.isEmpty()) {
                        PenggunaanAppAdapter adapter = new PenggunaanAppAdapter(getActivity(), dataItemList);
                        rvPanduanApp.setAdapter(adapter);
                        rvPanduanApp.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvPanduanApp.setHasFixedSize(true);
                    }else{
                        Toast.makeText(getActivity(), "Data Masih kosong", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getActivity(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseListPanduan> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}