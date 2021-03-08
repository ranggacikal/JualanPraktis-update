package www.starcom.com.jualanpraktis.feature.akun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.StatusBelumDibayarAdapter;
import www.starcom.com.jualanpraktis.adapter.StatusTransaksiAdapter;

public class BelumDibayarFragment extends Fragment {

    RecyclerView rvBelumDibayar;
    ShimmerFrameLayout shimmerBelumDiabayar;

    TextView txtKosong;

    loginuser user ;

    ArrayList<HashMap<String, String>> dataSemuaBelumDibayar = new ArrayList<>();
    //    ArrayList<JSONObject> dataProdukSemuaPesanan = new ArrayList<JSONObject>();
    ArrayList<HashMap<String, String>> dataProdukBelumDibayar = new ArrayList<>();


    public BelumDibayarFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_belum_dibayar, container, false);

        AndroidNetworking.initialize(getActivity().getApplicationContext());
        user = SharedPrefManager.getInstance(getActivity()).getUser();

        rvBelumDibayar = rootView.findViewById(R.id.recycler_status_transaksi_belum_dibayar);
        shimmerBelumDiabayar = rootView.findViewById(R.id.shimmerBelumDiBayar);
        txtKosong = rootView.findViewById(R.id.text_kosong_belum_dibayar);

        rvBelumDibayar.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvBelumDibayar.setHasFixedSize(true);

        loadRecycler();

        return rootView;
    }

    private void loadRecycler() {

        rvBelumDibayar.setVisibility(View.GONE);
        shimmerBelumDiabayar.setVisibility(View.VISIBLE);
        shimmerBelumDiabayar.startShimmerAnimation();

        String url = "https://jualanpraktis.net/android/pesanan_pending.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("customer", user.getId())
                .setTag(getActivity())
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        shimmerBelumDiabayar.stopShimmerAnimation();
                        shimmerBelumDiabayar.setVisibility(View.GONE);

                        dataSemuaBelumDibayar.clear();
                        dataProdukBelumDibayar.clear();
                        try {
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                data.put("id_transaksi",jsonObject.getString("id_transaksi"));
                                data.put("tanggal",jsonObject.getString("tgl_transaksi"));
                                data.put("status_pesanan",jsonObject.getString("status_pesanan"));
                                data.put("status_kirim",jsonObject.getString("status_kirim"));



                                JSONArray produk = jsonObject.getJSONArray("produk");
                                for (int j = 0; j<produk.length(); j++){
                                    JSONObject jsonObject1 = produk.getJSONObject(j);
                                    data.put("nama_produk", jsonObject1.getString("nama_produk"));
                                    data.put("gambar", jsonObject1.getString("image_o"));
                                    data.put("variasi", jsonObject1.getString("ket2"));
                                    data.put("jumlah", jsonObject1.getString("jumlah"));
                                    data.put("harga_produk", jsonObject1.getString("harga_produk"));
                                    data.put("harga_jual", jsonObject1.getString("harga_jual"));
                                    data.put("untung", jsonObject1.getString("untung"));
//                                    dataProdukSemuaPesanan.add(data);
                                }

                                dataSemuaBelumDibayar.add(data);
                            }

                            Log.d("dataSemuaPesanan", "onResponse: "+dataSemuaBelumDibayar);
                            rvBelumDibayar.setVisibility(View.VISIBLE);
                            StatusBelumDibayarAdapter adapter = new StatusBelumDibayarAdapter(getActivity(), dataSemuaBelumDibayar);
                            rvBelumDibayar.setAdapter(adapter);
                            if (dataSemuaBelumDibayar.isEmpty()){
                                txtKosong.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerBelumDiabayar.stopShimmerAnimation();
                        shimmerBelumDiabayar.setVisibility(View.GONE);
                        dataSemuaBelumDibayar.clear();

                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(getActivity(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(getActivity(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }
}