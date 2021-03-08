package www.starcom.com.jualanpraktis.feature.akun;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.BantuanAdapter;
import www.starcom.com.jualanpraktis.api.ConfigRetrofit;
import www.starcom.com.jualanpraktis.model_retrofit.model_bantuan.DataItem;
import www.starcom.com.jualanpraktis.model_retrofit.model_bantuan.ResponseDataBantuan;

public class BantuanActivity extends AppCompatActivity {

    RecyclerView rv_bantuan;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan);
        ButterKnife.bind(this);

        rv_bantuan = findViewById(R.id.rv_bantuan);
        imgBack = findViewById(R.id.imgBackBantuan);

        rv_bantuan.setHasFixedSize(true);
        rv_bantuan.setLayoutManager(new LinearLayoutManager(BantuanActivity.this));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getDataBantuan();
    }

    private void getDataBantuan() {

        ProgressDialog progressDialog = new ProgressDialog(BantuanActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Memuat Data");
        progressDialog.show();

        ConfigRetrofit.service.dataBantuan().enqueue(new Callback<ResponseDataBantuan>() {
            @Override
            public void onResponse(Call<ResponseDataBantuan> call, Response<ResponseDataBantuan> response) {
                if (response.isSuccessful()){

                    progressDialog.dismiss();

                    List<DataItem> dataBantuan = response.body().getData();

                    if (!dataBantuan.isEmpty()){

                        BantuanAdapter adapter = new BantuanAdapter(BantuanActivity.this, dataBantuan);
                        rv_bantuan.setAdapter(adapter);

                    }else{
                        Toast.makeText(BantuanActivity.this, "Data Bantuan Kosong", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(BantuanActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataBantuan> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(BantuanActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}