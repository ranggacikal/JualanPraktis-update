package www.starcom.com.jualanpraktis.feature.akun;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.BatalkanTransaksiAdapter;
import www.starcom.com.jualanpraktis.adapter.ProdukRincianTransaksiAdapter;
import www.starcom.com.jualanpraktis.api.ConfigRetrofit;
import www.starcom.com.jualanpraktis.model_retrofit.batalkan_pesanan.ResponseBatalkanPesanan;

public class BatalkanTransaksiActivity extends AppCompatActivity {

    TextView txtId, txtTanggal, txtStatus;
    RecyclerView rvProdukBatal;
    EditText edtAlasan;
    LinearLayout linearTambahMedia1, linearTambahMedia2, linearTambahMedia3, linearAjukanPembatalan;
    ImageView imgBatal1, imgBatal2, imgBatal3;
    ShimmerFrameLayout shimmerProdukBatal;
    loginuser user;
    private Bitmap bitmap;
    private Bitmap bitmap2;
    private Bitmap bitmap3;

    ArrayList<HashMap<String, String>> listProdukBatal = new ArrayList<>();

    private static final int IMG_REQUEST = 1;
    private static final int IMG_REQUEST2 = 2;
    private static final int IMG_REQUEST3 = 3;
    private static final int IMG_REQUEST4 = 4;

    private String PicturePath1 = "";
    private String PicturePath2 = "";
    private String PicturePath3 = "";

    String nama_file1 = "", nama_file2, nama_file3;

    Uri uri1;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batalkan_transaksi);

        AndroidNetworking.initialize(getApplicationContext());
        user = SharedPrefManager.getInstance(BatalkanTransaksiActivity.this).getUser();

        txtId = findViewById(R.id.text_id_batalkan_transaksi);
        txtTanggal = findViewById(R.id.text_tanggal_batalkan_transaksi);
        txtStatus = findViewById(R.id.text_status_batalkan_transaksi);
        rvProdukBatal = findViewById(R.id.rv_list_produk_batalkan_transaksi);
        edtAlasan = findViewById(R.id.edt_alasan_pembatalan);
        linearAjukanPembatalan = findViewById(R.id.linear_ajukan_batal);
        linearTambahMedia1 = findViewById(R.id.tambah_media_batal1);
        linearTambahMedia2 = findViewById(R.id.tambah_media_batal2);
        linearTambahMedia3 = findViewById(R.id.tambah_media_batal3);
        imgBatal1 = findViewById(R.id.img_batal_transaksi1);
        imgBatal2 = findViewById(R.id.img_batal_transaksi2);
        imgBatal3 = findViewById(R.id.img_batal_transaksi3);
        shimmerProdukBatal = findViewById(R.id.shimmer_batalkan_transaksi);

        rvProdukBatal.setHasFixedSize(true);
        rvProdukBatal.setLayoutManager(new LinearLayoutManager(BatalkanTransaksiActivity.this));

        txtId.setText(getIntent().getStringExtra("id_transaksi"));
        txtTanggal.setText(getIntent().getStringExtra("tanggal"));
        txtStatus.setText(getIntent().getStringExtra("status"));

        linearTambahMedia1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(BatalkanTransaksiActivity.this)
                        .crop(340, 340)
                        .compress(512)
                        .start(0);
            }
        });


        linearTambahMedia2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PicturePath1.equals("")) {
                    Toast.makeText(BatalkanTransaksiActivity.this, "Silahkanupload dahulu Dikolom pertama", Toast.LENGTH_SHORT).show();
                } else {

                    ImagePicker.Companion.with(BatalkanTransaksiActivity.this)
                            .crop(340, 340)
                            .compress(512)
                            .start(1);

                }
            }
        });

        linearTambahMedia3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PicturePath1.equals("")) {
                    Toast.makeText(BatalkanTransaksiActivity.this, "Silahkanupload dahulu Dikolom pertama", Toast.LENGTH_SHORT).show();
                } else {
                    ImagePicker.Companion.with(BatalkanTransaksiActivity.this)
                            .crop(340, 340)
                            .compress(512)
                            .start(2);
                }
            }
        });

        linearAjukanPembatalan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nama_file1.equals("")||nama_file1==null||nama_file1.isEmpty()) {
                    Toast.makeText(BatalkanTransaksiActivity.this, "Upload Gambar Minimal 1", Toast.LENGTH_SHORT).show();
                } else if (edtAlasan.getText().toString().isEmpty()) {
                    Toast.makeText(BatalkanTransaksiActivity.this, "Alasan Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else {
//                    ajukanBatalkanPesanan();
                    ajukanBatalkanPesananRetrofit();
                }

            }
        });

        loadBatalkanProduk();

    }

    private void ajukanBatalkanPesananRetrofit() {

        String id_transaksi = txtId.getText().toString();
        String id_member = user.getId();
        String alasan = edtAlasan.getText().toString();

        File file1 = new File(PicturePath1);

        File file2, file3;
        MultipartBody.Part body2, body3;

        if (PicturePath2.isEmpty()||PicturePath2.equals("")||PicturePath2==null){
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            body2 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }else {
            file2 = new File(PicturePath2);
            RequestBody requestFile2 =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file2);
            body2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestFile2);
        }

        if (PicturePath3.isEmpty()||PicturePath3.equals("")||PicturePath3==null){
            RequestBody attachmentEmpty2 = RequestBody.create(MediaType.parse("text/plain"), "");
            body3 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty2);
        }else {
            file3 = new File(PicturePath3);
            RequestBody requestFile3 =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file3);
            body3 = MultipartBody.Part.createFormData("file3", file3.getName(), requestFile3);
        }

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file1);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file1", file1.getName(), requestFile);

        RequestBody id_transaksi_retrofit = RequestBody.create(MediaType.parse("text/plain"),
                txtId.getText().toString());

        RequestBody id_member_retrofit = RequestBody.create(MediaType.parse("text/plain"),
                user.getId());

        RequestBody alasan_retrofit = RequestBody.create(MediaType.parse("text/plain"),
                edtAlasan.getText().toString());

        progressDialog = new ProgressDialog(BatalkanTransaksiActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengajukan Pembatalan");
        progressDialog.show();

        ConfigRetrofit.service.batalkanPesanan(id_transaksi_retrofit, id_member_retrofit, alasan_retrofit,
                body, body2, body3)
                .enqueue(new Callback<ResponseBatalkanPesanan>() {
            @Override
            public void onResponse(Call<ResponseBatalkanPesanan> call, Response<ResponseBatalkanPesanan> response) {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
//                    Log.d("dataAjukanBatal", "onResponse: "+id_transaksi+", "+id_member+", "+alasan+", "+image1+", "+
//                            image2+", "+image3);
                    Toast.makeText(BatalkanTransaksiActivity.this, "Berhasil Mengajukan Pembatalan", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(BatalkanTransaksiActivity.this, "Gagal Mengajukan Pembatalan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBatalkanPesanan> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(BatalkanTransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ajukanBatalError", "onFailure: "+t.getMessage());
            }
        });

    }

    private void ajukanBatalkanPesanan() {

        progressDialog = new ProgressDialog(BatalkanTransaksiActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengajukan Pembatalan");
        progressDialog.show();

        HashMap<String, String> params = new HashMap<>();
        params.put("id_member", user.getId());
        params.put("id_transaksi", txtId.getText().toString());
        params.put("alasan", edtAlasan.getText().toString());
        //params.put("password2",picturePath);

        try {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            AndroidNetworking.upload("https://jualanpraktis.net/android/batalkan_pesanan.php")
                    .addMultipartParameter(params)
                    .addMultipartFile("file1", new File(PicturePath1))
                    .addMultipartFile("file2", new File(PicturePath2))
                    .addMultipartFile("file3", new File(PicturePath3))
                    .setTag(BatalkanTransaksiActivity.this)
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
                            if (response.contains("Data Berhasil Dikirim")) {
                                Toast.makeText(BatalkanTransaksiActivity.this,
                                        "Berhasil Kirim Ajukan Pembatalan", Toast.LENGTH_LONG).show();
                                Log.d("dataParamsBatal", "onResponse: "+params+", "+PicturePath1+", "+PicturePath2+", "+
                                        PicturePath3);
                                finish();
                            } else {
                                Toast.makeText(BatalkanTransaksiActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
//                        progressDialog.dismiss();
                            progressDialog.dismiss();
                            Log.d("dataParamsBatal", "onResponse: "+params+", "+PicturePath1+", "+PicturePath2+", "+
                                    PicturePath3);
                            Toast.makeText(BatalkanTransaksiActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (NullPointerException e) {
            progressDialog.dismiss();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {
            if (resultCode == RESULT_OK && requestCode == 0) {


                uri1 = data.getData();
                nama_file1 = uri1.getLastPathSegment();
                linearTambahMedia1.setVisibility(View.GONE);
                PicturePath1 = uri1.getPath();
                imgBatal1.setVisibility(View.VISIBLE);
                imgBatal1.setImageURI(uri1);

//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri1);
//                imgBatal1.setVisibility(View.VISIBLE);
//                imgBatal1.setImageBitmap(bitmap);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (resultCode == RESULT_OK && requestCode == 1) {


                Uri uri2 = data.getData();
//                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
//                imgBatal2.setVisibility(View.VISIBLE);
//                imgBatal2.setImageBitmap(bitmap2);
                nama_file2 = uri2.getLastPathSegment();
                linearTambahMedia2.setVisibility(View.GONE);
                PicturePath2 = uri2.getPath();
                imgBatal2.setVisibility(View.VISIBLE);
                imgBatal2.setImageURI(uri2);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (resultCode == RESULT_OK && requestCode == 2) {


                Uri uri3 = data.getData();
//                bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri3);
//                imgBatal3.setVisibility(View.VISIBLE);
//                imgBatal3.setImageBitmap(bitmap3);
                nama_file3 = uri3.getLastPathSegment();
                PicturePath3 = uri3.getPath();
                linearTambahMedia3.setVisibility(View.GONE);
                imgBatal3.setVisibility(View.VISIBLE);
                imgBatal3.setImageURI(uri3);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private String imageToString2() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private String imageToString3() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private void loadBatalkanProduk() {

        rvProdukBatal.setVisibility(View.GONE);
        shimmerProdukBatal.setVisibility(View.VISIBLE);
        shimmerProdukBatal.startShimmerAnimation();

        String url = "https://jualanpraktis.net/android/detail_pesanan.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("id_transaksi", getIntent().getStringExtra("id_transaksi"))
                .addBodyParameter("status_kirim", getIntent().getStringExtra("status_kirim"))
                .setTag(BatalkanTransaksiActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        shimmerProdukBatal.stopShimmerAnimation();
                        shimmerProdukBatal.setVisibility(View.GONE);

                        listProdukBatal.clear();
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

                                listProdukBatal.add(data);
                                rvProdukBatal.setVisibility(View.VISIBLE);
                                BatalkanTransaksiAdapter batalkanAdapter = new BatalkanTransaksiAdapter(BatalkanTransaksiActivity.this, listProdukBatal);
                                rvProdukBatal.setAdapter(batalkanAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerProdukBatal.stopShimmerAnimation();
                        shimmerProdukBatal.setVisibility(View.GONE);
                        listProdukBatal.clear();

                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(BatalkanTransaksiActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(BatalkanTransaksiActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BatalkanTransaksiActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }
}