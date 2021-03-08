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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.PenghasilanBatalAdapter;
import www.starcom.com.jualanpraktis.adapter.PenghasilanSayaAdapter;
import www.starcom.com.jualanpraktis.adapter.PenghasilanSelesaiAdapter;
import www.starcom.com.jualanpraktis.model.ListPenghasilanSaya;

public class PesananDibatalkanFragment extends Fragment {

    loginuser user ;

    ArrayList<HashMap<String, String>> penghasilanBatal = new ArrayList<>();
    ArrayList<HashMap<String, String>> listProdukBatal = new ArrayList<>();

    RecyclerView rvPesananBatal;
    ShimmerFrameLayout shimmerBatal;
    TextView txtPotongan, txtKosong;

    String potongan;

    public PesananDibatalkanFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_pesanan_dibatalkan, container, false);
        AndroidNetworking.initialize(getActivity().getApplicationContext());
        user = SharedPrefManager.getInstance(getActivity()).getUser();

        rvPesananBatal = rootView.findViewById(R.id.recycler_penghasilan_saya_batalkan);
        shimmerBatal = rootView.findViewById(R.id.shimmerPenghasilanSayaBatal);
        txtPotongan = rootView.findViewById(R.id.text_total_potongan);
//        txtKosong = rootView.findViewById(R.id.text_kosong_dibatalkan);

        String start_date = getArguments().getString("start_date");
        String end_date = getArguments().getString("end_date");

        rvPesananBatal.setHasFixedSize(true);
        rvPesananBatal.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (start_date != null && end_date != null) {

            Log.d("dateCheck", "onCreateView: "+start_date+" - "+end_date);

            if (!start_date.equals("semua_periode") && !end_date.equals("semua periode")) {
                Log.d("periodeDate", "onCreateView: " + start_date + " - " + end_date);
                loadFilterData(user.getId(), start_date, end_date);
                Log.d("listProdukBatal", "onCreateView: "+penghasilanBatal);
            }else{
                Log.d("periodeDate", "else data: " + start_date + " - " + end_date);
                loadData();
            }
        }else {
            loadData();
        }
//        Toast.makeText(getActivity(), "INI BATAL", Toast.LENGTH_SHORT).show();

        return rootView;
    }

    public void loadFilterData(String id_member, String start_date, String end_date) {

        String url = "https://jualanpraktis.net/android/transaksi_dibatalkan.php";

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

                        penghasilanBatal.clear();
                        try {

                            potongan = response.getString("potongan");
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                data.put("id_transaksi",jsonObject.getString("id_transaksi"));


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

                                }

                                Log.d("listProses", "onResponse: "+data.toString()+" + Potensi: "+potongan);
                                penghasilanBatal.add(data);
                            }

                            Log.d("potonganSaya", "onResponse: "+potongan);
                            Log.d("dataPesananSelesai", "onResponse: "+penghasilanBatal);
                            rvPesananBatal.setVisibility(View.VISIBLE);
                            PenghasilanBatalAdapter adapter = new PenghasilanBatalAdapter(penghasilanBatal, getActivity());
                            rvPesananBatal.setAdapter(adapter);

                            if (penghasilanBatal.isEmpty()){
                                txtKosong.setVisibility(View.VISIBLE);
                            }

                            int potonganInt = Integer.parseInt(potongan);
                            txtPotongan.setText("Rp"+NumberFormat.getInstance().format(potonganInt));
                            txtPotongan.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
//                        shimmerProses.stopShimmerAnimation();
//                        shimmerProses.setVisibility(View.GONE);
                        penghasilanBatal.clear();

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

    private void loadData() {

        String url = "https://jualanpraktis.net/android/transaksi_dibatalkan.php";

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
                        shimmerBatal.stopShimmerAnimation();
                        shimmerBatal.setVisibility(View.GONE);

                        penghasilanBatal.clear();
                        try {

                            potongan = response.getString("potongan");
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                data.put("id_transaksi",jsonObject.getString("id_transaksi"));
                                data.put("tanggal",jsonObject.getString("tgl_transaksi"));
                                data.put("status_pesanan",jsonObject.getString("status_pesanan"));
                                data.put("status_kirim",jsonObject.getString("status_kirim"));

                                listProdukBatal.clear();
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
                                    listProdukBatal.add(data2);
                                    Log.d("listProduk", "onResponse: "+listProdukBatal);
                                }

                                penghasilanBatal.add(data);
                            }

                            Log.d("potonganSaya", "onResponse: "+potongan);
                            Log.d("dataPesananSelesai", "onResponse: "+penghasilanBatal);
                            rvPesananBatal.setVisibility(View.VISIBLE);
                            PenghasilanBatalAdapter adapter = new PenghasilanBatalAdapter(penghasilanBatal, getActivity());
                            rvPesananBatal.setAdapter(adapter);

//                            if (listProdukBatal.isEmpty()){
//                                txtKosong.setVisibility(View.VISIBLE);
//                            }

                            int potonganInt = Integer.parseInt(potongan);
                            Locale localID = new Locale("in", "ID");
                            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localID);
                            txtPotongan.setText("Rp"+ NumberFormat.getInstance().format(potonganInt));
                            txtPotongan.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerBatal.stopShimmerAnimation();
                        shimmerBatal.setVisibility(View.GONE);
                        penghasilanBatal.clear();

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