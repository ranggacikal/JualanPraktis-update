package www.starcom.com.jualanpraktis.feature.akun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import www.starcom.com.jualanpraktis.adapter.TukarPesananAdapter;
import www.starcom.com.jualanpraktis.api.ConfigRetrofit;
import www.starcom.com.jualanpraktis.model_retrofit.tukar_pesanan.ResponseTukarPesanan;
import www.starcom.com.jualanpraktis.utils.FileUtil;

public class TukarkanPesananActivity extends AppCompatActivity {

    TextView txtId, txtTanggal, txtStatus;
    RecyclerView rvProdukTukar;
    EditText edtAlasan;
    LinearLayout linearTambahMedia1, linearTambahMedia2, linearTambahMedia3, linearAjukanTukar;
    ImageView imgTukar1, imgTukar2, imgTukar3;
    ShimmerFrameLayout shimmerProdukBatal;
    loginuser user;
    Uri uri1;

    ArrayList<HashMap<String, String>> listProdukTukar = new ArrayList<>();

    public ArrayList<String> dataNomor = new ArrayList<>();

    private String PicturePath1 = "";
    private String PicturePath2 = "";
    private String PicturePath3 = "";

    String nama_file1 = "", nama_file2, nama_file3;

    ProgressDialog progressDialog;

    LinearLayout parentLinear;
    ImageView iv_add_image;
    ImageView selectedImage;
    List<Uri> files = new ArrayList<>();
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukarkan_pesanan);

        AndroidNetworking.initialize(getApplicationContext());
        user = SharedPrefManager.getInstance(TukarkanPesananActivity.this).getUser();

        txtId = findViewById(R.id.text_id_tukar_transaksi);
        txtTanggal = findViewById(R.id.text_tanggal_tukar_transaksi);
        txtStatus = findViewById(R.id.text_status_tukar_transaksi);
        rvProdukTukar = findViewById(R.id.rv_list_produk_tukar_transaksi);
        edtAlasan = findViewById(R.id.edt_alasan_tukar);
        linearAjukanTukar = findViewById(R.id.linear_ajukan_tukar);
        linearTambahMedia1 = findViewById(R.id.tambah_media_tukar1);
        linearTambahMedia2 = findViewById(R.id.tambah_media_tukar2);
        linearTambahMedia3 = findViewById(R.id.tambah_media_tukar3);
        imgTukar1 = findViewById(R.id.img_tukar_transaksi1);
        imgTukar2 = findViewById(R.id.img_tukar_transaksi2);
        imgTukar3 = findViewById(R.id.img_tukar_transaksi3);
        shimmerProdukBatal = findViewById(R.id.shimmer_tukar_transaksi);
        parentLinear = findViewById(R.id.parent_linear_layout);
        iv_add_image = findViewById(R.id.iv_add_image);

        rvProdukTukar.setHasFixedSize(true);
        rvProdukTukar.setLayoutManager(new LinearLayoutManager(TukarkanPesananActivity.this));

        txtId.setText(getIntent().getStringExtra("id_transaksi"));
        txtTanggal.setText(getIntent().getStringExtra("tanggal"));
        txtStatus.setText(getIntent().getStringExtra("status"));

        linearTambahMedia1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(TukarkanPesananActivity.this)
                        .crop(340, 340)
                        .compress(512)
                        .start(0);
            }
        });

        linearTambahMedia2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PicturePath1.equals("")) {
                    Toast.makeText(TukarkanPesananActivity.this, "Silahkan upload dahulu Dikolom pertama", Toast.LENGTH_SHORT).show();
                } else {

                    ImagePicker.Companion.with(TukarkanPesananActivity.this)
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
                    Toast.makeText(TukarkanPesananActivity.this, "Silahkan upload dahulu Dikolom pertama", Toast.LENGTH_SHORT).show();
                } else {
                    ImagePicker.Companion.with(TukarkanPesananActivity.this)
                            .crop(340, 340)
                            .compress(512)
                            .start(2);
                }
            }
        });

        linearAjukanTukar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("dataNomor", "onClick: " + dataNomor);
                if (nama_file1.equals("")||nama_file1.isEmpty()||nama_file1==null) {
                    Toast.makeText(TukarkanPesananActivity.this, "Upload Gambar Minimal 1", Toast.LENGTH_SHORT).show();
                } else if (edtAlasan.getText().toString().isEmpty()) {
                    Toast.makeText(TukarkanPesananActivity.this, "Alasan Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else {
//                    ajukanTukarPesanan();
                    ajukanTukarPesananRetrofit();
                }
            }
        });

        iv_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });

        loadTukarkanProduk();

    }

    private void addImage() {

        LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView=inflater.inflate(R.layout.image, null);
        // Add the new row before the add field button.
        parentLinear.addView(rowView, parentLinear.getChildCount() - 1);
        parentLinear.isFocusable();

        selectedImage = rowView.findViewById(R.id.number_edit_text);
        selectImage(TukarkanPesananActivity.this);

    }

    private void selectImage(Context context) {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Choose a Media");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "intuenty", null);
        Log.d("image uri",path);
        return Uri.parse(path);
    }

    private void ajukanTukarPesananRetrofit(){

        ProgressDialog progressDialog = new ProgressDialog(TukarkanPesananActivity.this);
        progressDialog.setMessage("Mengajukan Penukaran Produk");
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.show();

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

        RequestBody nomor_retrofit = RequestBody.create(MediaType.parse("text/plain"),
                dataNomor.toString());

        RequestBody alasan_retrofit = RequestBody.create(MediaType.parse("text/plain"),
                edtAlasan.getText().toString());

        ConfigRetrofit.service.tukarPesanan(dataNomor, alasan_retrofit, id_transaksi_retrofit, body, body2, body3)
                .enqueue(new Callback<ResponseTukarPesanan>() {
                    @Override
                    public void onResponse(Call<ResponseTukarPesanan> call, Response<ResponseTukarPesanan> response) {
                        if (response.isSuccessful()){
                            progressDialog.dismiss();
//                    Log.d("dataAjukanBatal", "onResponse: "+id_transaksi+", "+id_member+", "+alasan+", "+image1+", "+
//                            image2+", "+image3);
                            Toast.makeText(TukarkanPesananActivity.this, "Berhasil Mengajukan Retur",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(TukarkanPesananActivity.this, "Gagal Mengajukan Retur",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseTukarPesanan> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(TukarkanPesananActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("tukarPesanan", "onFailure: "+t.getMessage() );
                    }
                });

    }

    private void ajukanTukarPesanan() {

        String nomorString = "";


        progressDialog = new ProgressDialog(TukarkanPesananActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mengajukan Penukaran");
        progressDialog.show();

        HashMap<String, String> params = new HashMap<>();
        params.put("nomor[]", dataNomor.toString());
        params.put("alasan", edtAlasan.getText().toString());
        params.put("id_transaksi", txtId.getText().toString());
        //params.put("password2",picturePath);

        try {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();


            AndroidNetworking.upload("https://jualanpraktis.net/android/retur.php")
                    .addMultipartParameter(params)
                    .addMultipartFile("file1", new File(PicturePath1))
                    .addMultipartFile("file2", new File(PicturePath2))
                    .addMultipartFile("file3", new File(PicturePath3))
                    .setTag(TukarkanPesananActivity.this)
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
                                Toast.makeText(TukarkanPesananActivity.this,
                                        "Berhasil Kirim Ajukan Penukaran", Toast.LENGTH_LONG).show();
                                Log.d("dataParam", "onResponse: " + params.toString());
                                finish();
                            } else {
                                Toast.makeText(TukarkanPesananActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                            }
//
                        }

                        @Override
                        public void onError(ANError anError) {
//                        progressDialog.dismiss();
                            progressDialog.dismiss();
                            Toast.makeText(TukarkanPesananActivity.this, "Gagal Kirim", Toast.LENGTH_SHORT).show();
                            Log.d("dataError", "onResponse: " + anError.getErrorDetail());
                        }
                    });
        } catch (NullPointerException e) {
            //kalau kosong
            e.printStackTrace();
            Toast.makeText(TukarkanPesananActivity.this,
                    "Upload dulu Imagenya, tekan di bagian gambar.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

    private void loadTukarkanProduk() {

        rvProdukTukar.setVisibility(View.GONE);
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
                .setTag(TukarkanPesananActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        shimmerProdukBatal.stopShimmerAnimation();
                        shimmerProdukBatal.setVisibility(View.GONE);

                        listProdukTukar.clear();
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

                                listProdukTukar.add(data);
                                rvProdukTukar.setVisibility(View.VISIBLE);
                                TukarPesananAdapter tukarAdapter = new TukarPesananAdapter(TukarkanPesananActivity.this, listProdukTukar,
                                        TukarkanPesananActivity.this);
                                rvProdukTukar.setAdapter(tukarAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerProdukBatal.stopShimmerAnimation();
                        shimmerProdukBatal.setVisibility(View.GONE);
                        listProdukTukar.clear();

                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(TukarkanPesananActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(TukarkanPesananActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TukarkanPesananActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
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


                uri1 = data.getData();
                nama_file1 = uri1.getLastPathSegment();
                linearTambahMedia1.setVisibility(View.GONE);
                imgTukar1.setVisibility(View.VISIBLE);
                imgTukar1.setImageURI(uri1);
                PicturePath1 = uri1.getPath();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (resultCode == RESULT_OK && requestCode == 1) {


                Uri uri2 = data.getData();
                nama_file2 = uri2.getLastPathSegment();
                linearTambahMedia2.setVisibility(View.GONE);
                imgTukar2.setVisibility(View.VISIBLE);
                imgTukar2.setImageURI(uri2);
                PicturePath2 = uri2.getPath();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (resultCode == RESULT_OK && requestCode == 2) {


                Uri uri3 = data.getData();
                nama_file3 = uri3.getLastPathSegment();
                linearTambahMedia3.setVisibility(View.GONE);
                imgTukar3.setVisibility(View.VISIBLE);
                imgTukar3.setImageURI(uri3);
                PicturePath3 = uri3.getPath();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}