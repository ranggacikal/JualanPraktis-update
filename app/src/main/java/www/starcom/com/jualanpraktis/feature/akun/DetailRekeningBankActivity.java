package www.starcom.com.jualanpraktis.feature.akun;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.starcom.com.jualanpraktis.EditAkunActivity;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.VolleySingleton;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.FavoritAdapter;
import www.starcom.com.jualanpraktis.api.ConfigRetrofit;
import www.starcom.com.jualanpraktis.feature.produk.ProdukDetailActivity;
import www.starcom.com.jualanpraktis.model_retrofit.ResponseUpdateRekening;

public class DetailRekeningBankActivity extends AppCompatActivity {

    @BindView(R.id.edt_nama_buku_tabungan)
    EditText edtNamaBukuTabungan;
    @BindView(R.id.edt_no_rekening)
    EditText edtNoRekening;
    @BindView(R.id.txt_nama_bank)
    TextView txtNamaBank;
    @BindView(R.id.relative_nama_bank)
    RelativeLayout relativeNamaBank;
    @BindView(R.id.btn_simpan_detail_rekening_bank)
    Button btnSimpanDetailRekeningBank;

    Button btnEditDetailRekeningBank;

    public DetailRekeningBankActivity() {

    }

    String nama, get_nama_bank, rekening;

    public static final String ExtraNamaBank = "extraNamaBank";

    loginuser user;

    ArrayList<HashMap<String, String>> responseKataSandi = new ArrayList<>();
    ArrayList<HashMap<String, String>> dataRekening = new ArrayList<>();

    Dialog dialog;
    RelativeLayout relativeDetailRekeningBank;
    FrameLayout frameDetailRekening;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rekening_bank);
        ButterKnife.bind(this);

        btnEditDetailRekeningBank = findViewById(R.id.btn_edit_detail_rekening_bank);
        relativeDetailRekeningBank = findViewById(R.id.relative_detail_rekening);
        frameDetailRekening = findViewById(R.id.frame_detail_rekening);
        imgBack = findViewById(R.id.imgBackInputRekening);

        user = SharedPrefManager.getInstance(DetailRekeningBankActivity.this).getUser();

        dialog = new Dialog(DetailRekeningBankActivity.this);
        dialog.setContentView(R.layout.dialog_konfirmasi_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        get_nama_bank = getIntent().getStringExtra(ExtraNamaBank);
        nama = getIntent().getStringExtra("nama");
        rekening = getIntent().getStringExtra("rekening");

        validasiForm();

        if (get_nama_bank == null && nama == null && rekening == null){

            getDataRekeningDetail();

        }

        if (get_nama_bank != null) {
            txtNamaBank.setText(get_nama_bank);
        }

        if (nama != null) {
            edtNamaBukuTabungan.setText(nama);
        }

        if (rekening != null) {
            edtNoRekening.setText(rekening);
        }

        relativeNamaBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihBank();
            }
        });

        btnSimpanDetailRekeningBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validasiData();
            }
        });

        btnEditDetailRekeningBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEditDetailRekeningBank.setVisibility(View.GONE);
                btnSimpanDetailRekeningBank.setVisibility(View.VISIBLE);
            }
        });


    }

    private void validasiForm() {

        String namarekening = edtNamaBukuTabungan.getText().toString();
        String norekening = edtNoRekening.getText().toString();

        if (namarekening == null && norekening == null) {

            edtNamaBukuTabungan.setText("");
            edtNoRekening.setText("");
            txtNamaBank.setText("---");

        }
    }

    private void getDataRekeningDetail() {

        HashMap<String, String> param = new HashMap<>();
        param.put("id_member", user.getId());

        AndroidNetworking.post("https://jualanpraktis.net/android/detail_rekening.php")
                .addBodyParameter(param)
                .setTag(DetailRekeningBankActivity.this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(DetailRekeningBankActivity.this, "Password Sesuai", Toast.LENGTH_SHORT).show();
                        //  progressDialog.dismiss();

                        try {
                            responseKataSandi.clear();
                            JSONArray array = response.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                HashMap<String, String> item = new HashMap<>();
                                item.put("atas_nama", object.getString("atas_nama"));
                                item.put("no_rek", object.getString("no_rek"));
                                item.put("nama_bank", object.getString("nama_bank"));
                                edtNamaBukuTabungan.setText(object.getString("atas_nama"));
                                edtNoRekening.setText(object.getString("no_rek"));
                                txtNamaBank.setText(object.getString("nama_bank"));
                                btnEditDetailRekeningBank.setVisibility(View.VISIBLE);
                                btnSimpanDetailRekeningBank.setVisibility(View.GONE);
                                dataRekening.add(item);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        if (anError.getErrorCode() != 0) {
                            Toast.makeText(DetailRekeningBankActivity.this, "Anda belum mengisi detail rekening bank", Toast.LENGTH_SHORT).show();
                            edtNamaBukuTabungan.setText("");
                            edtNoRekening.setText("");
                            txtNamaBank.setText("---");
                            btnSimpanDetailRekeningBank.setVisibility(View.VISIBLE);
                            btnEditDetailRekeningBank.setVisibility(View.GONE);
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(DetailRekeningBankActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DetailRekeningBankActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

    }

    private void validasiData() {

        if (edtNamaBukuTabungan.getText().toString().equals("")) {
            edtNamaBukuTabungan.setError("Tidak Boleh Kosong");
            edtNamaBukuTabungan.requestFocus();
            return;
        }

        if (edtNoRekening.getText().toString().equals("")) {
            edtNoRekening.setError("Tidak Boleh Kosong");
            edtNoRekening.requestFocus();
            return;
        }

        if (get_nama_bank == null) {
            Toast.makeText(this, "Siahkan Pilih Bank Terlebih Dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        tampilDialogKataSandi();

    }

    private void tampilDialogKataSandi() {

        EditText edtMasukanSandi = dialog.findViewById(R.id.edt_dialog_kata_sandi);
        TextView txtBatal = dialog.findViewById(R.id.text_dialog_batal);
        TextView txtOke = dialog.findViewById(R.id.text_dialog_ok);
        ImageView imgHide = dialog.findViewById(R.id.img_hide_password_rekening);

        View view = imgHide;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(view.getId()==R.id.img_hide_password_rekening){

                    if(edtMasukanSandi.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        ((ImageView)(view)).setImageResource(R.drawable.icon_awesome_eye);
                        //Show Password
                        edtMasukanSandi.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        ((ImageView)(view)).setImageResource(R.drawable.icon_awesome_eye_slash);

                        //Hide Password
                        edtMasukanSandi.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }

            }
        });


        dialog.show();

        txtBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kata_sandi = edtMasukanSandi.getText().toString();
                HashMap<String, String> param = new HashMap<>();
                param.put("id_member", user.getId());
                param.put("password1", kata_sandi);

                AndroidNetworking.post("https://jualanpraktis.net/android/update_password.php ")
                        .addBodyParameter(param)
                        .setTag(DetailRekeningBankActivity.this)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(DetailRekeningBankActivity.this, "Password Sesuai", Toast.LENGTH_SHORT).show();
                                //  progressDialog.dismiss();
                                try {
                                    responseKataSandi.clear();
                                    JSONArray array = response.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        HashMap<String, String> item = new HashMap<>();
                                        item.put("response", object.getString("response"));
                                        responseKataSandi.add(item);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                kirimData();

                            }

                            @Override
                            public void onError(ANError anError) {

                                if (anError.getErrorCode() != 0) {
                                    Toast.makeText(DetailRekeningBankActivity.this, "Kata Sandi Salah.", Toast.LENGTH_SHORT).show();
                                    Log.d("CekData", "onError: " + user.getId());
                                    Log.d("CekData", "onError: " + kata_sandi);
                                } else {
                                    // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                    if (anError.getErrorDetail().equals("connectionError")) {
                                        Toast.makeText(DetailRekeningBankActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(DetailRekeningBankActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        });

            }
        });
    }

    public void showHidePass(View view){



    }

    private void kirimData() {

        String id_member = user.getId();
        String atas_nama = edtNamaBukuTabungan.getText().toString();
        String no_rek = edtNoRekening.getText().toString();
        String nama_bank = getIntent().getStringExtra(ExtraNamaBank);

        ProgressDialog progressDialog = new ProgressDialog(DetailRekeningBankActivity.this);
        progressDialog.setTitle("Mengupdate Data");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ConfigRetrofit.service.updateRekening(id_member, atas_nama, no_rek, nama_bank).enqueue(new Callback<ResponseUpdateRekening>() {
            @Override
            public void onResponse(Call<ResponseUpdateRekening> call, Response<ResponseUpdateRekening> response) {

                if (response.isSuccessful()) {

                    String pesan = response.body().getMessage();
                    progressDialog.dismiss();

                    if (pesan.equals("Perubahan profile berhasil disimpan")) {

                        Toast.makeText(DetailRekeningBankActivity.this, "Berhasil Update", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        progressDialog.dismiss();
                        //TO DO SAVE KE SHARED PREFERENCES MANAGER
                        loginuser userUpdateBank = new loginuser(
                                user.getId(),
                                user.getKode(),
                                user.getNama(),
                                user.getNama_toko(),
                                user.getProvinsi(),
                                user.getKota(),
                                user.getKecamatan(),
                                user.getAlamat(),
                                user.getNo_ktp(),
                                user.getNo_npwp(),
                                user.getNo_hp(),
                                user.getEmail(),
                                atas_nama,
                                no_rek,
                                nama_bank,
                                user.getFoto()
                        );

                        SharedPrefManager.getInstance(DetailRekeningBankActivity.this).userLogin(userUpdateBank);

                    } else {
                        Toast.makeText(DetailRekeningBankActivity.this, "Gagal Update", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } else {
                    Toast.makeText(DetailRekeningBankActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseUpdateRekening> call, Throwable t) {
                Toast.makeText(DetailRekeningBankActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    private void validasiKataSandi() {


    }

    private void pilihBank() {

        String nama = edtNamaBukuTabungan.getText().toString();
        String rekening = edtNoRekening.getText().toString();

        if (nama.isEmpty()){
            edtNamaBukuTabungan.setError("Field Tidak Boleh Kosong");
            edtNamaBukuTabungan.requestFocus();
            return;
        }

        if (rekening.isEmpty()){
            edtNoRekening.setError("Field No. Rekening Tidak Boleh Kosong");
            edtNoRekening.requestFocus();
            return;
        }

        Intent intent = new Intent(DetailRekeningBankActivity.this, PilihBankDetailRekeningActivity.class);
        intent.putExtra(PilihBankDetailRekeningActivity.ExtraNama, nama);
        intent.putExtra(PilihBankDetailRekeningActivity.ExtraRekening, rekening);
        startActivity(intent);
        finish();

    }
}