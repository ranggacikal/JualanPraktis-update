package www.starcom.com.jualanpraktis.feature.akun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.NotifikasiAdapter;
import www.starcom.com.jualanpraktis.adapter.StatusTransaksiAdapter;

public class NotifikasiActivity extends AppCompatActivity {

    ImageView imgBack;
    ShimmerFrameLayout shimmerNotifikasi;
    RecyclerView rv_notifikasi;
    TextView txtKosong;

    loginuser user ;
    ArrayList<HashMap<String, String>> dataNotifikasi = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        imgBack = findViewById(R.id.imgBackNotifikasi);
        shimmerNotifikasi = findViewById(R.id.shimmerNotifikasi);
        rv_notifikasi = findViewById(R.id.recycler_notifikasi);
        txtKosong = findViewById(R.id.text_kosong_notifikasi);

        AndroidNetworking.initialize(getApplicationContext());

        user = SharedPrefManager.getInstance(NotifikasiActivity.this).getUser();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv_notifikasi.setHasFixedSize(true);
        rv_notifikasi.setLayoutManager(new LinearLayoutManager(NotifikasiActivity.this));

        loadDataNotifikasi();
    }

    private void loadDataNotifikasi() {

        rv_notifikasi.setVisibility(View.GONE);
        shimmerNotifikasi.setVisibility(View.VISIBLE);
        shimmerNotifikasi.startShimmerAnimation();

        String url = "https://jualanpraktis.net/android/message.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("email", user.getEmail())
                .setTag(NotifikasiActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        shimmerNotifikasi.stopShimmerAnimation();
                        shimmerNotifikasi.setVisibility(View.GONE);

                        dataNotifikasi.clear();
                        try {
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                data.put("id_broadcast",jsonObject.getString("id_broadcast"));
                                data.put("subject",jsonObject.getString("subject"));
                                data.put("deskripsi",jsonObject.getString("description"));
                                data.put("email",jsonObject.getString("email"));

                                dataNotifikasi.add(data);
                            }

                            Log.d("dataSemuaPesanan", "onResponse: "+dataNotifikasi);
                            rv_notifikasi.setVisibility(View.VISIBLE);
                            NotifikasiAdapter adapter = new NotifikasiAdapter(NotifikasiActivity.this, dataNotifikasi);
                            rv_notifikasi.setAdapter(adapter);
                            if (dataNotifikasi.isEmpty()){
                                txtKosong.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerNotifikasi.stopShimmerAnimation();
                        shimmerNotifikasi.setVisibility(View.GONE);
                        dataNotifikasi.clear();

                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(NotifikasiActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(NotifikasiActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(NotifikasiActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }
}