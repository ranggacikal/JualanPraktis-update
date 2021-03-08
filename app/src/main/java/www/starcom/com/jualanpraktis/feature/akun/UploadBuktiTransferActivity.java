package www.starcom.com.jualanpraktis.feature.akun;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.MainActivity;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.TukarPesananAdapter;
import www.starcom.com.jualanpraktis.adapter.UploadBuktiAdapter;
import www.starcom.com.jualanpraktis.feature.produk.ProdukDetailActivity;

public class UploadBuktiTransferActivity extends AppCompatActivity {

    TextView txtId, txtTanggal, txtStatus;
    RecyclerView rvProdukBelumDibayar;
    LinearLayout linearTambahMedia, linearUpload;
    ImageView imgUpload;
    ShimmerFrameLayout shimmerBelumDibayar;
    loginuser user;

    ArrayList<HashMap<String, String>> listBelumDibayar = new ArrayList<>();

    private String PicturePath;

    String nama_file, total_bayar;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_bukti_transfer);

        AndroidNetworking.initialize(getApplicationContext());
        user = SharedPrefManager.getInstance(UploadBuktiTransferActivity.this).getUser();

        txtId = findViewById(R.id.text_id_upload_bukti);
        txtTanggal = findViewById(R.id.text_tanggal_upload_bukti);
        txtStatus = findViewById(R.id.text_status_upload_bukti);
        rvProdukBelumDibayar = findViewById(R.id.rv_list_produk_upload_bukti);
        linearUpload = findViewById(R.id.linear_upload_bukti);
        linearTambahMedia = findViewById(R.id.tambah_upload_bukti);
        imgUpload = findViewById(R.id.img_upload_bukti);
        shimmerBelumDibayar = findViewById(R.id.shimmer_upload_bukti);

        rvProdukBelumDibayar.setHasFixedSize(true);
        rvProdukBelumDibayar.setLayoutManager(new LinearLayoutManager(UploadBuktiTransferActivity.this));

        txtId.setText(getIntent().getStringExtra("id_transaksi"));
        txtTanggal.setText(getIntent().getStringExtra("tanggal"));
        txtStatus.setText(getIntent().getStringExtra("status"));

        total_bayar = getIntent().getStringExtra("total_bayar");
        Log.d("getTotalBayar", "onCreate: "+total_bayar);

        linearTambahMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(UploadBuktiTransferActivity.this)
                        .crop(340, 340)
                        .compress(512)
                        .start(0);
            }
        });

        linearUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBukti();
            }
        });

        loadProduk();


    }

    private void uploadBukti() {

        progressDialog = new ProgressDialog(UploadBuktiTransferActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Upload Bukti Transfer");
        progressDialog.show();

        HashMap<String, String> params = new HashMap<>();
        params.put("id_customer", user.getId());
        params.put("id_transaksi", txtId.getText().toString());
        params.put("jumlah_bayar", getIntent().getStringExtra("total_bayar"));
        params.put("user_konfirmasi", user.getNama());
        //params.put("password2",picturePath);

        try {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();


            AndroidNetworking.upload("https://jualanpraktis.net/android/konfirmasi-transfer.php")
                    .addMultipartParameter(params)
                    .addMultipartFile("foto", new File(PicturePath))
                    .setTag(UploadBuktiTransferActivity.this)
                    .setPriority(Priority.HIGH)
                    .setOkHttpClient(okHttpClient)
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {

                        }
                    })
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.contains("Data Berhasil Di Kirim")) {
                                Toast.makeText(UploadBuktiTransferActivity.this,
                                        "Berhasil Upload Bukti Transfer", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(UploadBuktiTransferActivity.this, StatusTransaksiActivity.class);
                                intent.putExtra("extraUpload", "uploadTransfer");
                                startActivity(intent);
                                finish();
                            } else {
                                Log.d("dataParam", "onError: "+PicturePath);
                                Toast.makeText(UploadBuktiTransferActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
//                        progressDialog.dismiss();
                            progressDialog.dismiss();
                            Log.d("dataParam", "onError: "+PicturePath);
                            Toast.makeText(UploadBuktiTransferActivity.this, "GAGAL UPLOAD", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (NullPointerException e){
            //kalau kosong
            e.printStackTrace();
            Toast.makeText(UploadBuktiTransferActivity.this,
                    "Upload dulu Imagenya, tekan di bagian gambar.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

    private void loadProduk() {

        rvProdukBelumDibayar.setVisibility(View.GONE);
        shimmerBelumDibayar.setVisibility(View.VISIBLE);
        shimmerBelumDibayar.startShimmerAnimation();

        String url = "https://jualanpraktis.net/android/detail_pesanan.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("id_transaksi", getIntent().getStringExtra("id_transaksi"))
                .setTag(UploadBuktiTransferActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        shimmerBelumDibayar.stopShimmerAnimation();
                        shimmerBelumDibayar.setVisibility(View.GONE);

                        listBelumDibayar.clear();
                        try {

                            JSONArray array = response.getJSONArray("data_produk");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String, String> data = new HashMap<>();
                                data.put("nama_produk", jsonObject.getString("nama_produk"));
                                data.put("gambar", jsonObject.getString("image_o"));
                                data.put("nomor", jsonObject.getString("nomor"));
                                data.put("variasi", jsonObject.getString("ket2"));
                                data.put("harga_produk", jsonObject.getString("harga_item"));
                                data.put("harga_jual", jsonObject.getString("harga_jual"));
                                data.put("harga_produk2", jsonObject.getString("harga_item2"));
                                data.put("harga_jual2", jsonObject.getString("harga_jual2"));
                                data.put("jumlah", jsonObject.getString("jumlah"));
                                data.put("id_member", jsonObject.getString("id_member"));
                                data.put("untung1", jsonObject.getString("untung1"));
                                data.put("untung2", jsonObject.getString("untung2"));

                                listBelumDibayar.add(data);
                                rvProdukBelumDibayar.setVisibility(View.VISIBLE);
                                UploadBuktiAdapter uploadAdapter = new UploadBuktiAdapter(UploadBuktiTransferActivity.this, listBelumDibayar);
                                rvProdukBelumDibayar.setAdapter(uploadAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerBelumDibayar.stopShimmerAnimation();
                        shimmerBelumDibayar.setVisibility(View.GONE);
                        listBelumDibayar.clear();

                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(UploadBuktiTransferActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(UploadBuktiTransferActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UploadBuktiTransferActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {
            if (resultCode == RESULT_OK && requestCode == 0) {


                Uri uri1 = data.getData();
                nama_file = uri1.getLastPathSegment();
                linearTambahMedia.setVisibility(View.GONE);
                imgUpload.setVisibility(View.VISIBLE);
                imgUpload.setImageURI(uri1);
                PicturePath = uri1.getPath();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}