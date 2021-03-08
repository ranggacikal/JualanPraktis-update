package www.starcom.com.jualanpraktis.feature.akun;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.PencairanAdapter;
import www.starcom.com.jualanpraktis.adapter.TukarPesananAdapter;
import www.starcom.com.jualanpraktis.model.ListPencairan;

public class RiwayatPencairanActivity extends AppCompatActivity {

    @BindView(R.id.imgBackRiwayatPencairan)
    ImageView imgBackRiwayatPencairan;
    @BindView(R.id.recycler_riwayat_pencairan)
    RecyclerView recyclerRiwayatPencairan;

    ArrayList<HashMap<String, String>> dataPencairan = new ArrayList<>();
    loginuser user;

    TextView txtKosong;

    ListPencairan[] listPencairan = new ListPencairan[]{
            new ListPencairan("29 Des 2020", "17 : 00", "Rp. 450.000"),
            new ListPencairan("28 Des 2020", "14 : 00", "Rp.750.000")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_pencairan);
        ButterKnife.bind(this);

        AndroidNetworking.initialize(getApplicationContext());
        user = SharedPrefManager.getInstance(RiwayatPencairanActivity.this).getUser();

        txtKosong = findViewById(R.id.text_kosong_pencairan);

        imgBackRiwayatPencairan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerRiwayatPencairan.setHasFixedSize(true);
        recyclerRiwayatPencairan.setLayoutManager(new LinearLayoutManager(RiwayatPencairanActivity.this));

        loadData();
    }

    private void loadData() {

        ProgressDialog progressDialog = new ProgressDialog(RiwayatPencairanActivity.this);
        progressDialog.setMessage("Memuat Data");
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.show();

        String url = "https://jualanpraktis.net/android/riwayat_pencairan.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("customer", user.getId())
                .setTag(RiwayatPencairanActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();
                        dataPencairan.clear();
                        try {

                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String, String> data = new HashMap<>();
                                data.put("id", jsonObject.getString("id"));
                                data.put("id_member", jsonObject.getString("id_member"));
                                data.put("no_pembayaran", jsonObject.getString("no_pembayaran"));
                                data.put("tgl", jsonObject.getString("tgl"));
                                data.put("PeriodeAwal", jsonObject.getString("PeriodeAwal"));
                                data.put("PeriodeAkhir", jsonObject.getString("PeriodeAkhir"));
                                data.put("komisi", jsonObject.getString("komisi"));
                                data.put("denda", jsonObject.getString("denda"));

                                dataPencairan.add(data);
                                Log.d("checkDataPencairan", "onResponse: /"+dataPencairan.size());
                                PencairanAdapter pencairanAdapter = new PencairanAdapter(RiwayatPencairanActivity.this,
                                        dataPencairan);
                                recyclerRiwayatPencairan.setAdapter(pencairanAdapter);


                                if (dataPencairan == null){
                                    txtKosong.setVisibility(View.VISIBLE);
                                    recyclerRiwayatPencairan.setVisibility(View.GONE);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        dataPencairan.clear();

                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(RiwayatPencairanActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(RiwayatPencairanActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RiwayatPencairanActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }
}