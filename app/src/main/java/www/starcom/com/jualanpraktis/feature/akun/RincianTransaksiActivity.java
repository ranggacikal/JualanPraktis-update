package www.starcom.com.jualanpraktis.feature.akun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.PenerimaRincianAdapter;
import www.starcom.com.jualanpraktis.adapter.PenghasilanBatalAdapter;
import www.starcom.com.jualanpraktis.adapter.ProdukRincianTransaksiAdapter;
import www.starcom.com.jualanpraktis.daftar;

public class RincianTransaksiActivity extends AppCompatActivity {

    RecyclerView rvProduk, rvDataTransaksi;
    ShimmerFrameLayout shimmerProduk, shimmerDataTransaksi;
    ArrayList<HashMap<String, String>> listDataTransaksi = new ArrayList<>();
    ArrayList<HashMap<String, String>> listProduk = new ArrayList<>();
    TextView txtId, txtTanggal, txtStatus;
    ImageView imgBack;
    TextView txtTotalHargaProduk, txtTotalKeuntungan, txtTotalOngkosKirim, txtTotalBayar;
    int total_bayar, totalHargaProduk, totalKeuntungan, totalOngkosKirim;
    String opsi_pembayaran;
    LinearLayout linearBatalkanPesanan, linearTukarkanPesanan, linearBelumDibayar, linearUpdateStatus, linearBarangReturReferensi;
    TextView txtNoReferensi;
    Dialog alertDialog;
    String id_member2;
    String status_retur, id_referensi;
    String fragment, imgBukti2;

    loginuser user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian_transaksi);

        txtId = findViewById(R.id.text_id_rincian_transaksi);
        txtTanggal = findViewById(R.id.text_tanggal_rincian_transaksi);
        txtStatus = findViewById(R.id.text_status_rincian_transaksi);
        imgBack = findViewById(R.id.imgBackRincianTransaksi);
        txtTotalHargaProduk = findViewById(R.id.text_hargaproduk_rincian);
        txtTotalKeuntungan = findViewById(R.id.text_totalkeuntungan_rincian);
        txtTotalOngkosKirim = findViewById(R.id.text_ongkir_rincian);
        txtTotalBayar = findViewById(R.id.text_totalbayar_rincian);
        linearBatalkanPesanan = findViewById(R.id.linear_batalkan_pesanan);
        linearTukarkanPesanan = findViewById(R.id.linear_tukar_pesanan);
        linearBelumDibayar = findViewById(R.id.linear_belum_dibayar);
        linearUpdateStatus = findViewById(R.id.linear_update_status);

        AndroidNetworking.initialize(getApplicationContext());
        user = SharedPrefManager.getInstance(RincianTransaksiActivity.this).getUser();

        rvProduk = findViewById(R.id.rv_list_produk_rincian_transaksi);
        shimmerProduk = findViewById(R.id.shimmerRincianProduk);
        rvDataTransaksi = findViewById(R.id.rv_list_penerima_rincian_transaksi);
        shimmerDataTransaksi = findViewById(R.id.shimmerRincianTransaksi);
        linearBarangReturReferensi = findViewById(R.id.linear_barang_retur_referensi);
        txtNoReferensi = findViewById(R.id.text_id_referensi_tukar);

        laodDataProduk();
        loadDataTransaksi();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fragment = getIntent().getStringExtra("fragmentDipesan");

        if (fragment!=null) {
            if (fragment.equals("fragmentDipesan")) {

                String status_kirim_intent = getIntent().getStringExtra("status_kirim");

                if (status_kirim_intent.equals("110")){
                    linearBatalkanPesanan.setVisibility(View.GONE);
                }else {
                    linearBatalkanPesanan.setVisibility(View.VISIBLE);
                }

            }
//            else if (fragment.equals("fragmentDiterima")){
//
//                if (status_retur!=null) {
//
//                    if (status_retur.equals("0")) {
//                        linearTukarkanPesanan.setVisibility(View.VISIBLE);
//                    } else if (status_retur.equals("1")) {
//                        linearBarangReturReferensi.setVisibility(View.VISIBLE);
//                        txtNoReferensi.setText(id_referensi);
//                    }
//
//                }
//
//                linearUpdateStatus.setVisibility(View.VISIBLE);
//
//            }
//            else if (fragment.equals("fragmentBelumDibayar")){
//
//                Log.d("imgBukti2", "onCreate: "+imgBukti2);
//
//                if (imgBukti2!=null) {
//                    linearBelumDibayar.setVisibility(View.GONE);
//                }else{
//                    linearBelumDibayar.setVisibility(View.VISIBLE);
//                }
//
//            }
        }

        linearBatalkanPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RincianTransaksiActivity.this, BatalkanTransaksiActivity.class);
                intent.putExtra("id_transaksi", txtId.getText().toString());
                intent.putExtra("tanggal", txtTanggal.getText().toString());
                intent.putExtra("status", txtStatus.getText().toString());
                intent.putExtra("status_kirim", getIntent().getStringExtra("status_kirim"));
                startActivity(intent);
            }
        });

        linearTukarkanPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RincianTransaksiActivity.this, TukarkanPesananActivity.class);
                intent.putExtra("id_transaksi", txtId.getText().toString());
                intent.putExtra("tanggal", txtTanggal.getText().toString());
                intent.putExtra("status", txtStatus.getText().toString());
                intent.putExtra("status_kirim", getIntent().getStringExtra("status_kirim"));
                startActivity(intent);
            }
        });

        linearUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new Dialog(RincianTransaksiActivity.this);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(R.layout.dialog_update_status);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button btnSelesai = alertDialog.findViewById(R.id.btn_selesaikan_update_pesanan);
                Button btnBatal = alertDialog.findViewById(R.id.btn_batal_update_pesanan);

                alertDialog.show();

                btnSelesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateStatus();
                    }
                });

                btnBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });


        txtId.setText(getIntent().getStringExtra("id_transaksi"));
        txtTanggal.setText(getIntent().getStringExtra("tanggal"));
        txtStatus.setText(getIntent().getStringExtra("status"));

        rvProduk.setLayoutManager(new LinearLayoutManager(RincianTransaksiActivity.this));
        rvProduk.setHasFixedSize(true);

        rvDataTransaksi.setLayoutManager(new LinearLayoutManager(RincianTransaksiActivity.this));
        rvDataTransaksi.setHasFixedSize(true);
    }

    private void updateStatus() {

        String host = "https://jualanpraktis.net/android/update_status_transaksi.php";

        String id_transaksi = txtId.getText().toString();
        String id_member = user.getId();

        ProgressDialog progressDialog = new ProgressDialog(RincianTransaksiActivity.this);
        progressDialog.setTitle("Menyelesaikan pesanan");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Map<String, String> params = new HashMap<String, String>();
        params.put("id_customer", id_member);
        params.put("id_transaksi", id_transaksi);
        params.put("id_member["+"]", id_member2);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post(host)
                .addBodyParameter(params)
                .setTag(RincianTransaksiActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Toast.makeText(RincianTransaksiActivity.this, "Berhasil Menyelesaikan Pesanan",
                                Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();


                        try {
                            Toast.makeText(RincianTransaksiActivity.this, response.getString("response"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Gagal menyelesaikan pesanan", Toast.LENGTH_SHORT).show();
                    }

                });

    }

    private void loadDataTransaksi() {

        rvDataTransaksi.setVisibility(View.GONE);
        shimmerDataTransaksi.setVisibility(View.VISIBLE);
        shimmerDataTransaksi.startShimmerAnimation();

        String url = "https://jualanpraktis.net/android/detail_pesanan.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("id_transaksi", getIntent().getStringExtra("id_transaksi"))
                .addBodyParameter("status_kirim", getIntent().getStringExtra("status_kirim"))
                .setTag(RincianTransaksiActivity.this)
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
                            String no_referensi, imgBukti;

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
                                status_retur = jsonObject.getString("status_retur");
                                id_referensi = jsonObject.getString("no_referensi");
                                no_referensi = jsonObject.getString("no_referensi");
                                imgBukti = jsonObject.getString("img");
                                imgBukti2 = jsonObject.getString("img");
                                Log.d("checkNoReferensi", "onResponse: "+no_referensi);

                                listDataTransaksi.add(data);
                                rvDataTransaksi.setVisibility(View.VISIBLE);
                                PenerimaRincianAdapter rincianAdapter = new PenerimaRincianAdapter(RincianTransaksiActivity.this, listDataTransaksi);
                                rvDataTransaksi.setAdapter(rincianAdapter);

                                Locale localID = new Locale("in", "ID");
                                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localID);
                                txtTotalHargaProduk.setText("Rp" + NumberFormat.getInstance().format(totalHargaProduk));
                                txtTotalKeuntungan.setText("Rp" + NumberFormat.getInstance().format(totalKeuntungan));
                                txtTotalOngkosKirim.setText("Rp" + NumberFormat.getInstance().format(totalOngkosKirim));
                                txtTotalBayar.setText("Rp" + NumberFormat.getInstance().format(total_bayar));

                                Log.d("checkImage", "onResponse: "+imgBukti);

//                                    if (imgBukti==null||imgBukti.isEmpty()||imgBukti.equals("")){
//                                        linearBelumDibayar.setVisibility(View.VISIBLE);
//                                    }else{
//                                        linearBelumDibayar.setVisibility(View.GONE);
//                                    }


                                linearBelumDibayar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(RincianTransaksiActivity.this, UploadBuktiTransferActivity.class);
                                        intent.putExtra("id_transaksi", txtId.getText().toString());
                                        intent.putExtra("tanggal", txtTanggal.getText().toString());
                                        intent.putExtra("status", txtStatus.getText().toString());
                                        intent.putExtra("total_bayar", total);
                                        startActivity(intent);
                                    }
                                });

                                if (fragment!=null) {
                                    if (fragment.equals("fragmentDiterima")) {

                                        if (status_retur != null) {

                                            if (status_retur.equals("1")) {
                                                linearBarangReturReferensi.setVisibility(View.VISIBLE);
                                                txtNoReferensi.setText(no_referensi);
                                            } else {
                                                linearTukarkanPesanan.setVisibility(View.VISIBLE);
                                            }

                                        }

                                        linearUpdateStatus.setVisibility(View.VISIBLE);

                                    } else if (fragment.equals("fragmentBelumDibayar")){

                                        Log.d("imgBukti", "onCreate: "+imgBukti);

                                        if (imgBukti==null||imgBukti.equals("null")) {
                                            linearBelumDibayar.setVisibility(View.VISIBLE);
                                        }

                                    }
                                }

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
                            Toast.makeText(RincianTransaksiActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(RincianTransaksiActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RincianTransaksiActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void laodDataProduk() {

        rvProduk.setVisibility(View.GONE);
        shimmerProduk.setVisibility(View.VISIBLE);
        shimmerProduk.startShimmerAnimation();

        String url = "https://jualanpraktis.net/android/detail_pesanan.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("id_transaksi", getIntent().getStringExtra("id_transaksi"))
                .addBodyParameter("status_kirim", getIntent().getStringExtra("status_kirim"))
                .setTag(RincianTransaksiActivity.this)
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
                                id_member2 = jsonObject.getString("id_member");

                                listProduk.add(data);
                                rvProduk.setVisibility(View.VISIBLE);
                                ProdukRincianTransaksiAdapter produkAdapter = new ProdukRincianTransaksiAdapter(RincianTransaksiActivity.this, listProduk);
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
                            Toast.makeText(RincianTransaksiActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(RincianTransaksiActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RincianTransaksiActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }
}