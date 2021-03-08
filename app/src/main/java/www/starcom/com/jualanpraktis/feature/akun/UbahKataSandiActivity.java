package www.starcom.com.jualanpraktis.feature.akun;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.form_pemesanan.ResultCodActivity;
import www.starcom.com.jualanpraktis.feature.form_pemesanan.ResultTransferActivity;

public class UbahKataSandiActivity extends AppCompatActivity {

    @BindView(R.id.imgBackUbahPassword)
    ImageView imgBackUbahPassword;
    @BindView(R.id.edt_password_lama_ubah)
    EditText edtPasswordLamaUbah;
    @BindView(R.id.btn_lanjut_password)
    Button btnLanjutPassword;
    @BindView(R.id.linear_password_lama)
    LinearLayout linearPasswordLama;
    @BindView(R.id.edt_password_baru_ubah)
    EditText edtPasswordBaruUbah;
    @BindView(R.id.edt_password_konfirmasi_ubah)
    EditText edtPasswordKonfirmasiUbah;
    @BindView(R.id.btn_oke_password)
    Button btnOkePassword;
    @BindView(R.id.linear_password_baru)
    LinearLayout linearPasswordBaru;

    TextView txtTidakCocok, txtCocok;

    loginuser user;

    ImageView showPassLama, showPassBaru, showPassKonfirmasi;

    ArrayList<HashMap<String, String>> responsePassword = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_kata_sandi);
        ButterKnife.bind(this);

        txtTidakCocok = findViewById(R.id.text_ubah_kata_sandi_tidak_cocok);
        txtCocok = findViewById(R.id.text_ubah_kata_sandi_cocok);
        showPassLama = findViewById(R.id.show_pass_btn_pass_lama);
        showPassBaru = findViewById(R.id.show_pass_btn_pass_baru);
        showPassKonfirmasi = findViewById(R.id.show_pass_btn_pass_konfirmasi);

        user = SharedPrefManager.getInstance(UbahKataSandiActivity.this).getUser();

        btnLanjutPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validasiPassword();
            }
        });

        imgBackUbahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        konfirmasiPassword();

        btnOkePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validasiPasswordBaru();
            }
        });

        showHidePassLama(showPassLama);
        showHidePassBaru(showPassBaru);
        showHidePassKonfirmasi(showPassKonfirmasi);
    }

    private void showHidePassKonfirmasi(View view) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(view.getId()==R.id.show_pass_btn_pass_konfirmasi){

                    if(edtPasswordKonfirmasiUbah.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        ((ImageView)(view)).setImageResource(R.drawable.icon_awesome_eye);
                        //Show Password
                        edtPasswordKonfirmasiUbah.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        ((ImageView)(view)).setImageResource(R.drawable.icon_awesome_eye_slash);

                        //Hide Password
                        edtPasswordKonfirmasiUbah.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }

            }
        });

    }

    private void showHidePassBaru(View view) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(view.getId()==R.id.show_pass_btn_pass_baru){

                    if(edtPasswordBaruUbah.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        ((ImageView)(view)).setImageResource(R.drawable.icon_awesome_eye);
                        //Show Password
                        edtPasswordBaruUbah.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        ((ImageView)(view)).setImageResource(R.drawable.icon_awesome_eye_slash);

                        //Hide Password
                        edtPasswordBaruUbah.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }

            }
        });

    }

    private void showHidePassLama(View view) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(view.getId()==R.id.show_pass_btn_pass_lama){

                    if(edtPasswordLamaUbah.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        ((ImageView)(view)).setImageResource(R.drawable.icon_awesome_eye);
                        //Show Password
                        edtPasswordLamaUbah.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        ((ImageView)(view)).setImageResource(R.drawable.icon_awesome_eye_slash);

                        //Hide Password
                        edtPasswordLamaUbah.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }

            }
        });

    }

    private void validasiPasswordBaru() {

        String password_baru = edtPasswordBaruUbah.getText().toString();
        String konfirmasi_password_baru = edtPasswordKonfirmasiUbah.getText().toString();

        if (password_baru.isEmpty()){
            edtPasswordBaruUbah.setError("Field Tidak Boleh Kosong");
            edtPasswordBaruUbah.requestFocus();
            return;
        }

        if (konfirmasi_password_baru.isEmpty()){
            edtPasswordKonfirmasiUbah.setError("Konfirmasi Password Tidak Boleh Kosong");
            edtPasswordKonfirmasiUbah.requestFocus();
            return;
        }

        ubahPasswordBaru();

    }

    private void konfirmasiPassword() {

        edtPasswordKonfirmasiUbah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String pass1 = edtPasswordBaruUbah.getText().toString();
                String pass2 = edtPasswordKonfirmasiUbah.getText().toString();

                if (pass2.equals(pass1)){
                    txtCocok.setVisibility(View.VISIBLE);
                    txtTidakCocok.setVisibility(View.GONE);
                }else{
                    txtCocok.setVisibility(View.GONE);
                    txtTidakCocok.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void ubahPasswordBaru() {
        String host = "https://jualanpraktis.net/android/update_password_member.php";

        ProgressDialog progressDialog = new ProgressDialog(UbahKataSandiActivity.this);
        progressDialog.setTitle("Mengubah Kata Sandi");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Map<String, String> params = new HashMap<String, String>();
        params.put("id_member", user.getId());
        params.put("password1", edtPasswordBaruUbah.getText().toString());
        params.put("password2", edtPasswordKonfirmasiUbah.getText().toString());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post(host)
                .addBodyParameter(params)
                .setTag(UbahKataSandiActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Toast.makeText(UbahKataSandiActivity.this, "Berhasil Ubah Kata Sandi", Toast.LENGTH_SHORT).show();
                        finish();

                        try {
                            Toast.makeText(getApplicationContext(), response.getString("response"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Gagal Ubah Kata Sandi", Toast.LENGTH_SHORT).show();
                    }

                });

    }

    private void validasiPassword() {

        String passwordLama = edtPasswordLamaUbah.getText().toString();

        if (passwordLama.isEmpty()) {
            edtPasswordLamaUbah.setError("Tidak Boleh Kosong");
            edtPasswordLamaUbah.requestFocus();
        } else {
            cekPasswordLama();
        }

    }

    private void cekPasswordLama() {

        String kata_sandi = edtPasswordLamaUbah.getText().toString();
        HashMap<String, String> param = new HashMap<>();
        param.put("id_member", user.getId());
        param.put("password1", kata_sandi);

        AndroidNetworking.post("https://jualanpraktis.net/android/update_password.php")
                .addBodyParameter(param)
                .setTag(UbahKataSandiActivity.this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(UbahKataSandiActivity.this, "Password Sesuai", Toast.LENGTH_SHORT).show();
                        //  progressDialog.dismiss();
                        try {
                            responsePassword.clear();
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                HashMap<String, String> item = new HashMap<>();
                                item.put("response", object.getString("response"));
                                responsePassword.add(item);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        linearPasswordLama.setVisibility(View.GONE);
                        linearPasswordBaru.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onError(ANError anError) {

                        if (anError.getErrorCode() != 0) {
                            Toast.makeText(UbahKataSandiActivity.this, "Kata Sandi Salah.", Toast.LENGTH_SHORT).show();
                            Log.d("CekData", "onError: " + user.getId());
                            Log.d("CekData", "onError: " + kata_sandi);
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(UbahKataSandiActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UbahKataSandiActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

    }
}