package www.starcom.com.jualanpraktis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import www.starcom.com.jualanpraktis.adapter.PenerimaRincianAdapter;
import www.starcom.com.jualanpraktis.adapter.ProdukRincianSemuaAdapter;
import www.starcom.com.jualanpraktis.adapter.ProdukRincianTransaksiAdapter;
import www.starcom.com.jualanpraktis.feature.akun.RincianTransaksiActivity;
import www.starcom.com.jualanpraktis.feature.akun.UploadBuktiTransferActivity;

public class RincianTransaksiSemuaActivity extends AppCompatActivity {

    RecyclerView rvProduk, rvDataTransaksi;
    ShimmerFrameLayout shimmerProduk, shimmerDataTransaksi;
    ArrayList<HashMap<String, String>> listDataTransaksi = new ArrayList<>();
    ArrayList<HashMap<String, String>> listProduk = new ArrayList<>();
    TextView txtId, txtTanggal, txtStatus;
    ImageView imgBack;
    TextView txtTotalHargaProduk, txtTotalKeuntungan, txtTotalOngkosKirim, txtTotalBayar;
    int total_bayar, totalHargaProduk, totalKeuntungan, totalOngkosKirim;
    String opsi_pembayaran;
    LinearLayout linearBatalkanPesanan, linearTukarkanPesanan, linearBelumDibayar, linearUpdateStatus;
    Dialog alertDialog;

    loginuser user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian_transaksi_semua);

        txtId = findViewById(R.id.text_id_rincian_transaksi_semua);
        txtTanggal = findViewById(R.id.text_tanggal_rincian_transaksi_semua);
        txtStatus = findViewById(R.id.text_status_rincian_transaksi_semua);
        imgBack = findViewById(R.id.imgBackRincianTransaksiSemua);
        txtTotalHargaProduk = findViewById(R.id.text_hargaproduk_rincian_semua);
        txtTotalKeuntungan = findViewById(R.id.text_totalkeuntungan_rincian_semua);
        txtTotalOngkosKirim = findViewById(R.id.text_ongkir_rincian_semua);
        txtTotalBayar = findViewById(R.id.text_totalbayar_rincian_semua);

        AndroidNetworking.initialize(getApplicationContext());
        user = SharedPrefManager.getInstance(RincianTransaksiSemuaActivity.this).getUser();

        rvProduk = findViewById(R.id.rv_list_produk_rincian_transaksi_semua);
        shimmerProduk = findViewById(R.id.shimmerRincianProdukSemua);
        rvDataTransaksi = findViewById(R.id.rv_list_penerima_rincian_transaksi_semua);
        shimmerDataTransaksi = findViewById(R.id.shimmerRincianTransaksiSemua);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtId.setText(getIntent().getStringExtra("id_transaksi"));
        txtTanggal.setText(getIntent().getStringExtra("tanggal"));
        txtStatus.setText(getIntent().getStringExtra("status"));

        rvProduk.setLayoutManager(new LinearLayoutManager(RincianTransaksiSemuaActivity.this));
        rvProduk.setHasFixedSize(true);

        rvDataTransaksi.setLayoutManager(new LinearLayoutManager(RincianTransaksiSemuaActivity.this));
        rvDataTransaksi.setHasFixedSize(true);

        loadDataProduk();
        loadDataTransaksi();
    }

    private void loadDataProduk() {

        rvProduk.setVisibility(View.GONE);
        shimmerProduk.setVisibility(View.VISIBLE);
        shimmerProduk.startShimmerAnimation();

        String url = "http://jualanpraktis.net/android/detail_semua_pesanan.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("id_transaksi", getIntent().getStringExtra("id_transaksi"))
                .setTag(RincianTransaksiSemuaActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        shimmerProduk.stopShimmerAnimation();
                        shimmerProduk.setVisibility(View.GONE);

                        listProduk.clear();
                        try {

                            JSONArray array = response.getJSONArray("data_produk");
                            for (int i = 0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                data.put("nama_produk",jsonObject.getString("nama_produk"));
                                data.put("gambar",jsonObject.getString("image_o"));
                                data.put("nomor",jsonObject.getString("nomor"));
                                data.put("variasi",jsonObject.getString("ket2"));
                                data.put("harga_produk",jsonObject.getString("harga_item"));
                                data.put("harga_jual",jsonObject.getString("harga_jual"));
                                data.put("harga_produk2",jsonObject.getString("harga_item2"));
                                data.put("harga_jual2",jsonObject.getString("harga_jual2"));
                                data.put("jumlah",jsonObject.getString("jumlah"));
                                data.put("id_member",jsonObject.getString("id_member"));
                                data.put("untung1",jsonObject.getString("untung1"));
                                data.put("untung2",jsonObject.getString("untung2"));
                                data.put("status_pesanan",jsonObject.getString("status_pesanan"));

                                listProduk.add(data);
                                rvProduk.setVisibility(View.VISIBLE);
                                ProdukRincianSemuaAdapter produkAdapter = new ProdukRincianSemuaAdapter(RincianTransaksiSemuaActivity.this, listProduk);
                                rvProduk.setAdapter(produkAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerProduk.stopShimmerAnimation();
                        shimmerProduk.setVisibility(View.GONE);
                        listProduk.clear();

                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(RincianTransaksiSemuaActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(RincianTransaksiSemuaActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RincianTransaksiSemuaActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    private void loadDataTransaksi() {

        rvDataTransaksi.setVisibility(View.GONE);
        shimmerDataTransaksi.setVisibility(View.VISIBLE);
        shimmerDataTransaksi.startShimmerAnimation();

        String url = "http://jualanpraktis.net/android/detail_semua_pesanan.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("id_transaksi", getIntent().getStringExtra("id_transaksi"))
                .addBodyParameter("status_kirim", getIntent().getStringExtra("status_kirim"))
                .setTag(RincianTransaksiSemuaActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        shimmerDataTransaksi.stopShimmerAnimation();
                        shimmerDataTransaksi.setVisibility(View.GONE);



                        listDataTransaksi.clear();
                        try {

                            String total =response.getString("total_bayar");
                            totalHargaProduk = Integer.parseInt(response.getString("total_harga_produk"));
                            totalKeuntungan = Integer.parseInt(response.getString("total_untung"));
                            totalOngkosKirim = Integer.parseInt(response.getString("total_ongkos_kirim"));
                            total_bayar = Integer.parseInt(response.getString("total_bayar"));

                            JSONArray array = response.getJSONArray("data_transaksi");
                            for (int i = 0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                data.put("id_transaksi",jsonObject.getString("id_transaksi"));
                                data.put("tanggal",jsonObject.getString("tgl_transaksi"));
                                data.put("status",jsonObject.getString("status_pesanan"));
                                data.put("nama_penerima",jsonObject.getString("nama_penerima"));
                                data.put("no_hp",jsonObject.getString("no_hp"));
                                data.put("alamat",jsonObject.getString("alamat"));
                                data.put("kode_pos",jsonObject.getString("kode_pos"));
                                data.put("kurir",jsonObject.getString("kurir"));
                                data.put("opsi_pembayaran",jsonObject.getString("id_opsi_bayar"));
                                data.put("kecamatan",jsonObject.getString("subdistrict_name"));
                                data.put("kota",jsonObject.getString("city_name"));

                                listDataTransaksi.add(data);
                                rvDataTransaksi.setVisibility(View.VISIBLE);
                                PenerimaRincianAdapter rincianAdapter = new PenerimaRincianAdapter(RincianTransaksiSemuaActivity.this, listDataTransaksi);
                                rvDataTransaksi.setAdapter(rincianAdapter);

                                Locale localID = new Locale("in", "ID");
                                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localID);
                                txtTotalHargaProduk.setText(formatRupiah.format(totalHargaProduk));
                                txtTotalKeuntungan.setText(formatRupiah.format(totalKeuntungan));
                                txtTotalOngkosKirim.setText(formatRupiah.format(totalOngkosKirim));
                                txtTotalBayar.setText(formatRupiah.format(total_bayar));

//                                linearBelumDibayar.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(RincianTransaksiSemuaActivity.this, UploadBuktiTransferActivity.class);
//                                        intent.putExtra("id_transaksi", txtId.getText().toString());
//                                        intent.putExtra("tanggal", txtTanggal.getText().toString());
//                                        intent.putExtra("status", txtStatus.getText().toString());
//                                        intent.putExtra("total_bayar", total);
//                                        startActivity(intent);
//                                    }
//                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerDataTransaksi.stopShimmerAnimation();
                        shimmerDataTransaksi.setVisibility(View.GONE);
                        listDataTransaksi.clear();

                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(RincianTransaksiSemuaActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(RincianTransaksiSemuaActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RincianTransaksiSemuaActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }
}