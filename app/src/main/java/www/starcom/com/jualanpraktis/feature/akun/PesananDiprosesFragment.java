package www.starcom.com.jualanpraktis.feature.akun;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.PenghasilanBatalAdapter;
import www.starcom.com.jualanpraktis.adapter.PenghasilanSayaAdapter;
import www.starcom.com.jualanpraktis.api.ConfigRetrofit;
import www.starcom.com.jualanpraktis.model.ListPenghasilanSaya;
import www.starcom.com.jualanpraktis.model.Periode;
import www.starcom.com.jualanpraktis.model_retrofit.periode.DataItem;
import www.starcom.com.jualanpraktis.model_retrofit.periode.ResponseDataPeriode;

public class PesananDiprosesFragment extends Fragment {


    loginuser user ;

    public ArrayList<HashMap<String, String>> penghasilanProses = new ArrayList<>();
    public ArrayList<HashMap<String, String>> listProdukProses = new ArrayList<>();

    public RecyclerView rvPesananProses;
    ShimmerFrameLayout shimmerProses;
    TextView txtPotensi, txtKosong;

    String potensiPendapatan;

    Spinner spinnerPeriode;

    public String semuaPeriode;

    String start, end;

    public PesananDiprosesFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_pesanan_diproses, container, false);

        AndroidNetworking.initialize(getActivity().getApplicationContext());
        user = SharedPrefManager.getInstance(getActivity()).getUser();

        String start_date = getArguments().getString("start_date");
        String end_date = getArguments().getString("end_date");
        String semua_periode = getArguments().getString("semua_periode");

        Periode periode = new Periode();

        String testAwal = periode.getStart_date();

        Log.d("cekModel", "onCreateView: "+testAwal);

        rvPesananProses = rootView.findViewById(R.id.recycler_penghasilan_saya);
        shimmerProses = rootView.findViewById(R.id.shimmerPenghasilanSayaProses);
        txtPotensi = rootView.findViewById(R.id.text_total_potensi_pendapatan);
        txtKosong = rootView.findViewById(R.id.text_kosong_pesanan_proses);

        rvPesananProses.setHasFixedSize(true);
        rvPesananProses.setLayoutManager(new LinearLayoutManager(getActivity()));

//        initSpinner();
//
//        spinnerPeriode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selected = parent.getItemAtPosition(position).toString();
//
//                String periodeFilter = start+" - "+end;
//
//                if (selected.equals("Semua Periode")) {
//
//                    Toast.makeText(getActivity(), "Select : " + selected, Toast.LENGTH_SHORT).show();
//                    Log.d("periodeFilter", "onItemSelected: "+periodeFilter);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });



        if (start_date != null && end_date != null) {

            if (!start_date.equals("semua_periode") && !end_date.equals("semua periode")) {
                Log.d("periodeDate", "onCreateView: " + start_date + " - " + end_date);
                loadFilterData(user.getId(), start_date, end_date);
            }else{
                Log.d("periodeDate", "else data: " + start_date + " - " + end_date);
                loadData();
            }
        }else {
            loadData();
        }

        if (semua_periode != null){
            loadData();
        }
//        else if (semua_periode != null) {
//
//            if (semua_periode.equals("semua periode")) {
//                loadData();
//            }



        return rootView;
    }

    private void initSpinner() {
        List<String> listSpinner = new ArrayList<String>();
        listSpinner.clear();

        listSpinner.add("Semua Periode");
        ProgressDialog loading = ProgressDialog.show(getActivity(), null, "harap tunggu...", true, false);

        ConfigRetrofit.service.dataPeriode().enqueue(new Callback<ResponseDataPeriode>() {
            @Override
            public void onResponse(Call<ResponseDataPeriode> call, Response<ResponseDataPeriode> response) {

                if (response.isSuccessful()) {
                    loading.dismiss();
                    List<DataItem> dataPeriode = response.body().getData();
                    for (int i = 0; i < dataPeriode.size(); i++){
                        start = dataPeriode.get(i).getStartDate();
                        end = dataPeriode.get(i).getEndDate();
                        String periode = start +" - "+end;
                        listSpinner.add(periode);
                    }
                    // Set hasil result json ke dalam adapter spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.item_spinner_periode, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPeriode.setAdapter(adapter);
                } else {
                    loading.dismiss();
                    Toast.makeText(getActivity(), "Gagal mengambil data dosen", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseDataPeriode> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void loadData() {

        String url = "https://jualanpraktis.net/android/transaksi_diproses.php";

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
                        shimmerProses.stopShimmerAnimation();
                        shimmerProses.setVisibility(View.GONE);

                        penghasilanProses.clear();
                        try {

                            potensiPendapatan = response.getString("penghasilan");
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                data.put("id_transaksi",jsonObject.getString("id_transaksi"));
                                data.put("status_kirim",jsonObject.getString("status_kirim"));

                                listProdukProses.clear();
                                JSONArray produk = jsonObject.getJSONArray("produk");
                                for (int j = 0; j<produk.length(); j++){
                                    JSONObject jsonObject1 = produk.getJSONObject(j);
                                    HashMap<String,String> data2 = new HashMap<>();
//                                    data2.put("id_transaksi", jsonObject1.getString("id_transaksi"));
                                    data.put("nama_produk", jsonObject1.getString("nama_produk"));
                                    data.put("gambar", jsonObject1.getString("image_o"));
                                    data.put("variasi", jsonObject1.getString("ket2"));
                                    data.put("jumlah", jsonObject1.getString("jumlah"));
                                    data.put("harga_produk", jsonObject1.getString("harga_produk"));
                                    data.put("harga_jual", jsonObject1.getString("harga_jual"));
                                    data.put("untung", jsonObject1.getString("untung"));
//                                    dataProdukSemuaPesanan.add(data);
//                                    listProdukSelesai.add(data);
                                    listProdukProses.add(data2);

                                }

                                Log.d("listProses", "onResponse: "+data.toString()+" + Potensi: "+potensiPendapatan);
                                penghasilanProses.add(data);
                            }

                            Log.d("potonganSaya", "onResponse: "+potensiPendapatan);
                            Log.d("dataPesananSelesai", "onResponse: "+penghasilanProses);
                            rvPesananProses.setVisibility(View.VISIBLE);
                            PenghasilanSayaAdapter adapter = new PenghasilanSayaAdapter(penghasilanProses, getActivity());
                            rvPesananProses.setAdapter(adapter);

                            if (penghasilanProses.isEmpty()){
                                txtKosong.setVisibility(View.VISIBLE);
                            }

                            int potonganInt = Integer.parseInt(potensiPendapatan);
                            txtPotensi.setText("Rp"+NumberFormat.getInstance().format(potonganInt));
                            txtPotensi.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerProses.stopShimmerAnimation();
                        shimmerProses.setVisibility(View.GONE);
                        penghasilanProses.clear();

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

    public void loadFilterData(String id_member, String start_date, String end_date) {

        String url = "https://jualanpraktis.net/android/transaksi_diproses.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("customer", id_member)
                .addBodyParameter("start_date", start_date)
                .addBodyParameter("end_date", end_date)
                .setTag(getActivity())
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        shimmerProses.stopShimmerAnimation();
//                        shimmerProses.setVisibility(View.GONE);

                        penghasilanProses.clear();
                        try {

                            potensiPendapatan = response.getString("penghasilan");
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                data.put("id_transaksi",jsonObject.getString("id_transaksi"));

                                listProdukProses.clear();
                                JSONArray produk = jsonObject.getJSONArray("produk");
                                for (int j = 0; j<produk.length(); j++){
                                    JSONObject jsonObject1 = produk.getJSONObject(j);
                                    HashMap<String,String> data2 = new HashMap<>();
//                                    data2.put("id_transaksi", jsonObject1.getString("id_transaksi"));
                                    data.put("nama_produk", jsonObject1.getString("nama_produk"));
                                    data.put("gambar", jsonObject1.getString("image_o"));
                                    data.put("variasi", jsonObject1.getString("ket2"));
                                    data.put("jumlah", jsonObject1.getString("jumlah"));
                                    data.put("harga_produk", jsonObject1.getString("harga_produk"));
                                    data.put("harga_jual", jsonObject1.getString("harga_jual"));
                                    data.put("untung", jsonObject1.getString("untung"));
//                                    dataProdukSemuaPesanan.add(data);
//                                    listProdukSelesai.add(data);
                                    listProdukProses.add(data2);

                                }

                                Log.d("listProses", "onResponse: "+data.toString()+" + Potensi: "+potensiPendapatan);
                                penghasilanProses.add(data);
                            }

                            Log.d("potonganSaya", "onResponse: "+potensiPendapatan);
                            Log.d("dataPesananSelesai", "onResponse: "+penghasilanProses);
                            rvPesananProses.setVisibility(View.VISIBLE);
                            PenghasilanSayaAdapter adapter = new PenghasilanSayaAdapter(penghasilanProses, getActivity());
                            rvPesananProses.setAdapter(adapter);

                            if (penghasilanProses.isEmpty()){
                                txtKosong.setVisibility(View.VISIBLE);
                            }

                            int potonganInt = Integer.parseInt(potensiPendapatan);
                            txtPotensi.setText("Rp"+NumberFormat.getInstance().format(potonganInt));
                            txtPotensi.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
//                        shimmerProses.stopShimmerAnimation();
//                        shimmerProses.setVisibility(View.GONE);
                        penghasilanProses.clear();

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