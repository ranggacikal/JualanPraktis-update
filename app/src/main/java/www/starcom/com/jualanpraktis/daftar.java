package www.starcom.com.jualanpraktis;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import www.starcom.com.jualanpraktis.Login.VolleySingleton;
import www.starcom.com.jualanpraktis.Spinner.Kecamatan;
import www.starcom.com.jualanpraktis.Spinner.KotaKabupaten;
import www.starcom.com.jualanpraktis.Spinner.Provinsi;
import www.starcom.com.jualanpraktis.api.ConfigRetrofit;
import www.starcom.com.jualanpraktis.databinding.ActivityResultTransferBinding;
import www.starcom.com.jualanpraktis.feature.akun.UbahKataSandiActivity;
import www.starcom.com.jualanpraktis.feature.produk.ProdukDetailActivity;
import www.starcom.com.jualanpraktis.model_retrofit.ResponseRegister;

import static www.starcom.com.jualanpraktis.alamat_pengiriman.BASE_URL;
import static www.starcom.com.jualanpraktis.alamat_pengiriman.KEY;

/**
 * Created by ADMIN on 12/02/2018.
 */

public class daftar extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getName();

    Activity activity = daftar.this;
    //ActivityResultTransferBinding binding;


    private EditText edtNama, edtEmail, edtPassword;
    private CardView btn_daftar ;
    private String mediaPath;
    private ImageView uploadPicture;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    String nama_kota,nama_wilayah,nama_provinsi ;
    Spinner spinner1,spinner2,spin_provinsi ;
    List<KotaKabupaten> kotaKabupatenList = new ArrayList<>();
    List<Kecamatan> kecamatanList = new ArrayList<>();
    List<Provinsi> provinsiList = new ArrayList<>();
    ProgressDialog progressDialog;

    //String jk = "" ;
    String id_kota,id_wilayah,id_provinsi ;
    String url = "https://jualanpraktis.net/android/signup.php";

    ImageView imghidePassword;

    ImageView imgKebijakanUncheck, imgKebijakanChecked;

    TextView txtKebijakaPrivasi;

    boolean checked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        progressDialog  = new ProgressDialog(daftar.this);
        AndroidNetworking.initialize(getApplicationContext());

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Register");

//        nama = findViewById(R.id.input_nama);
//        email = findViewById(R.id.input_email);
//        no_hp = findViewById(R.id.input_nohp);
//        password = findViewById(R.id.input_pass);
//        ulangpass = findViewById(R.id.input_ulang);
//        spinner1 = findViewById(R.id.spin_kota);
//        spinner2 = findViewById(R.id.spin_kecamatan);
//        spin_provinsi = findViewById(R.id.spin_provinsi);
//        input_alamat = findViewById(R.id.input_alamat);
//        nama_toko = findViewById(R.id.input_nama_toko);
//        no_ktp = findViewById(R.id.input_no_ktp);
//        no_npwp = findViewById(R.id.input_no_npwp);
//        atas_nama = findViewById(R.id.input_atas_nama);
//        no_rek = findViewById(R.id.input_no_rek);
//        nama_bank = findViewById(R.id.input_nama_bank);
//        uploadPicture = findViewById(R.id.upload_profile_picture);
//        btn_daftar = findViewById(R.id.btn_daftar);

        edtNama = findViewById(R.id.edt_nama_daftar);
        edtEmail = findViewById(R.id.edt_email_daftar);
        edtPassword = findViewById(R.id.edt_password_daftar);
        btn_daftar = findViewById(R.id.card_daftar);
        imghidePassword = findViewById(R.id.img_hide_password);
        imgKebijakanUncheck = findViewById(R.id.img_uncheck_daftar);
        imgKebijakanChecked = findViewById(R.id.img_checked_daftar);
        txtKebijakaPrivasi = findViewById(R.id.text_kebijakan_privasi);

//        jenis_kelamin = findViewById(R.id.RG);
//        laki = findViewById(R.id.laki);
//        perempuan = findViewById(R.id.perempuan);

//        UlangPass();
        btn_daftar.setOnClickListener(this);
//        uploadPicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ImagePicker.Companion.with(activity)
//                        .crop(340,340)
//                        .compress(512)
//                        .start();
//            }
//        });
//       getProvinsi();

//        spin_provinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Provinsi provinsi = provinsiList.get(spin_provinsi.getSelectedItemPosition());
//                id_provinsi = provinsi.getProvince_id();
//                nama_provinsi = provinsi.getProvince();
//                if (id_provinsi != null) {
//                    // spinnerData2(id_kota);
//                    getKotaKabupaten(id_provinsi);
//                    Log.d(TAG, id_provinsi);
//                }else {
//                    Log.d(TAG,""+ id_provinsi);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //   id_kota = ((SObject1.Object1.Results) spinner1.getSelectedItem()).idKota;
//                KotaKabupaten kotaKabupaten = kotaKabupatenList.get(spinner1.getSelectedItemPosition());
//                id_kota = kotaKabupaten.getCity_id();
//                nama_kota = kotaKabupaten.getCity_name();
//                if (id_kota != null) {
//                    // spinnerData2(id_kota);
//                    getKecamatan(id_kota);
//                    Log.d(TAG, id_kota);
//                }else {
//                    Log.d(TAG,""+ id_kota);
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Log.d(TAG, id_kota);
//            }
//        });
//        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Kecamatan kecamatan = kecamatanList.get(spinner2.getSelectedItemPosition());
//                id_wilayah = kecamatan.getSubdistrict_id();
//                nama_wilayah = kecamatan.getSubdistrict_name();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        txtKebijakaPrivasi.setPaintFlags(txtKebijakaPrivasi.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtKebijakaPrivasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(daftar.this, WebLawActivity.class);
                intent.putExtra("title", "Kebijakan Privasi");
                intent.putExtra("url", "file:///android_asset/kebijakan_privasi.html");
                startActivity(intent);
            }
        });

        showHidePass(imghidePassword);

    }

    public void showHidePass(View view){

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(view.getId()==R.id.img_hide_password){

                    if(edtPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        ((ImageView)(view)).setImageResource(R.drawable.icon_awesome_eye);
                        //Show Password
                        edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        ((ImageView)(view)).setImageResource(R.drawable.icon_awesome_eye_slash);

                        //Hide Password
                        edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }

            }
        });

    }

    @Override
    public void onClick(View v) {

        imgKebijakanUncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgKebijakanUncheck.setVisibility(View.GONE);
                imgKebijakanChecked.setVisibility(View.VISIBLE);
            }
        });

        imgKebijakanChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgKebijakanUncheck.setVisibility(View.VISIBLE);
                imgKebijakanChecked.setVisibility(View.GONE);
            }
        });

        String password = edtPassword.getText().toString();
        int min_length = 6;

        if (edtEmail.getText().toString().equals("")) {
            edtEmail.setError("Email belum di isi");
            edtEmail.requestFocus();
            return;
        }
        if (edtPassword.getText().toString().equals("")) {
            edtPassword.setError("Password belum di isi");
            edtPassword.requestFocus();
            return;
        }
        if (password.length()<=min_length){
            edtPassword.setError("Password Harus Lebih Dari 6 Karakter");
            edtPassword.requestFocus();
            return;
        }

        if (imgKebijakanChecked.getVisibility() == View.GONE){
            Toast.makeText(activity, "Anda belum menyetujui kebijakan dan privasi", Toast.LENGTH_SHORT).show();
            return;
        }

        signUp();
//        signUpRetrofit();
//        Daftar();

//        } else if (ulangpass.getText().toString().equals("")) {
//            ulangpass.setError("Password belum di isi");
//        } else if (!ulangpass.getText().toString().equals(password.getText().toString())) {
//            ulangpass.setError("Password salah");

//        } else if (jenis_kelamin.getCheckedRadioButtonId() == -1) {
//            Toast.makeText(this, "Anda Belum Memilih Cara Memperoleh Informasi", Toast.LENGTH_LONG).show();
//        }  else
        //Daftar
//        if (v == btn_daftar) {
//            if(mediaPath != null) {
//                Daftar();
//            }else{
//                Toast.makeText(activity,"Foto belum di upload",Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            Uri uri = data.getData();
            uploadPicture.setImageURI(uri);
            mediaPath = uri.getPath();
        }
    }

    public void KosongField(){
        edtNama.setText("");
        edtEmail.setText("");
        edtPassword.setText("");
        //ulangpass.setText("");
        //jenis_kelamin.check(0);

    }

//    public void UlangPass(){
//
//        ulangpass.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String Password = password.getText().toString();
//                if (!ulangpass.getText().toString().equals(Password)){
//                    ulangpass.setError("Password Salah");
//                }
//            }
//        });
//    }


    public void signUpRetrofit(){

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        progressDialog.setTitle("Daftar");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ConfigRetrofit.service.register(email, password).enqueue(new Callback<ResponseRegister>() {
            @Override
            public void onResponse(Call<ResponseRegister> call, retrofit2.Response<ResponseRegister> response) {
                if (response.isSuccessful()){

                    String pesan = response.body().getMessage();

                    if (pesan.equals("Pendaftaran berhasil")){

                        finish();
                        KosongField();
                        Toast.makeText(activity, "Berhasil Registrasi", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }else{
                        Toast.makeText(activity, "Gagal", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }else{
                    Toast.makeText(activity, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseRegister> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }


    public void signUp(){
        String host = "https://jualanpraktis.net/android/signup.php";

        ProgressDialog progressDialog = new ProgressDialog(daftar.this);
        progressDialog.setTitle("Sign Up...");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Map<String, String> params = new HashMap<String, String>();
        params.put("email", edtEmail.getText().toString());
        params.put("password", edtPassword.getText().toString());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post(host)
                .addBodyParameter(params)
                .setTag(daftar.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Toast.makeText(daftar.this, "Berhasil Daftar Akun", Toast.LENGTH_SHORT).show();
                        tampilDialogCheckEmail();


                        try {
                            Toast.makeText(getApplicationContext(), response.getString("response"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Email Sudah Digunakan", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void tampilDialogCheckEmail() {

        Dialog alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_check_email);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        Button btnDone = alertDialog.findViewById(R.id.btn_done_dialog_check_email);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });

        alertDialog.show();

    }

    public void Daftar(){
//        if (jenis_kelamin.getCheckedRadioButtonId()!= 0 ) {
//            if (jenis_kelamin.getCheckedRadioButtonId() == laki.getId()) {
//                jk = "Laki-Laki";
//            } else if (jenis_kelamin.getCheckedRadioButtonId() == perempuan.getId()) {
//                jk = "Perempuan";
//            }
//        }
        progressDialog.setTitle("Daftar");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        final HashMap<String,String> postData = new HashMap<>();
        postData.put("email",edtEmail.getText().toString());
        postData.put("password",edtPassword.getText().toString());
//        postData.put("nama_provinsi",nama_provinsi);
//        postData.put("nama_kota",nama_kota);
//        postData.put("nama_kecamatan",nama_wilayah);

//        postData.put("provinsi",id_provinsi);
//        postData.put("kota",id_kota);
//        postData.put("kecamatan",id_wilayah);
        //postData.put("files", body.toString());

        AndroidNetworking.post("https://jualanpraktis.net/android/signup.php")
                .addBodyParameter(postData)
                .setTag(activity)
                .setPriority(Priority.HIGH)
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
                            // progressDialog.dismiss();
//                            Intent intent = new Intent(activity, MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();
                            Toast.makeText(activity, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(activity,anError.getErrorDetail(),Toast.LENGTH_LONG).show();
                    }
                });

    }
//
//        private void getProvinsi(){
//        AndroidNetworking.get(BASE_URL + "province")
//                .addHeaders("key", KEY)
//                .setTag("test")
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//                        provinsiList.clear();
//                        try {
//                            JSONObject object = response.getJSONObject("rajaongkir");
//                            JSONArray array = object.getJSONArray("results");
//
//                            for (int i = 0; i < array.length(); i++){
//                                JSONObject obj = array.getJSONObject(i);
//                                Provinsi item = new Provinsi();
//                                item.setProvince_id(obj.getString("province_id"));
//                                item.setProvince(obj.getString("province"));
//                                provinsiList.add(item);
//                            }
//
//
//                            //   List<JenisPerangkat> list =respone.getJenisPerangkatArrayList();
//                            spin_provinsi.setVisibility(View.VISIBLE);
//                            ArrayAdapter<Provinsi> adapter = new ArrayAdapter<>(daftar.this, R.layout.support_simple_spinner_dropdown_item, provinsiList);
//                            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//                            //  spinner.setPrompt("Jenis Perangkat : ");
//                            spin_provinsi.setAdapter(adapter);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                    }
//                });
//    }
//    private void getKotaKabupaten(String id_provinsi){
//        AndroidNetworking.get(BASE_URL + "city?province="+id_provinsi)
//                .addHeaders("key", KEY)
//                .setTag("test")
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//                        kotaKabupatenList.clear();
//                        try {
//                            JSONObject object = response.getJSONObject("rajaongkir");
//                            JSONArray array = object.getJSONArray("results");
//
//                            for (int i = 0; i < array.length(); i++){
//                                JSONObject obj = array.getJSONObject(i);
//                                KotaKabupaten item = new KotaKabupaten();
//                                item.setCity_id(obj.getString("city_id"));
//                                item.setCity_name(obj.getString("city_name"));
//                                kotaKabupatenList.add(item);
//                            }
//
//
//                            //   List<JenisPerangkat> list =respone.getJenisPerangkatArrayList();
//                            spinner1.setVisibility(View.VISIBLE);
//                            ArrayAdapter<KotaKabupaten> adapter = new ArrayAdapter<KotaKabupaten>(daftar.this, R.layout.support_simple_spinner_dropdown_item, kotaKabupatenList);
//                            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//                            //  spinner.setPrompt("Jenis Perangkat : ");
//                            spinner1.setAdapter(adapter);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                    }
//                });
//    }
//    private void getKecamatan(String idkota){
//        AndroidNetworking.get(BASE_URL + "subdistrict?city="+idkota)
//                .addHeaders("key", KEY)
//                .setTag("test")
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//                        kecamatanList.clear();
//                        try {
//                            JSONObject object = response.getJSONObject("rajaongkir");
//                            JSONArray array = object.getJSONArray("results");
//
//                            for (int i = 0; i < array.length(); i++){
//                                JSONObject obj = array.getJSONObject(i);
//                                Kecamatan item = new Kecamatan();
//                                item.setSubdistrict_id(obj.getString("subdistrict_id"));
//                                item.setSubdistrict_name(obj.getString("subdistrict_name"));
//                                kecamatanList.add(item);
//                            }
//
//
//                            //   List<JenisPerangkat> list =respone.getJenisPerangkatArrayList();
//                            spinner2.setVisibility(View.VISIBLE);
//                            ArrayAdapter<Kecamatan> adapter = new ArrayAdapter<Kecamatan>(daftar.this, R.layout.support_simple_spinner_dropdown_item, kecamatanList);
//                            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//                            //  spinner.setPrompt("Jenis Perangkat : ");
//                            spinner2.setAdapter(adapter);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                    }
//                });
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
