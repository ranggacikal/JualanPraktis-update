package www.starcom.com.jualanpraktis.feature.akun;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.EditAkunActivity;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.MainActivity;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.Spinner.Pendidikan;
import www.starcom.com.jualanpraktis.adapter.PenghasilanBatalAdapter;
import www.starcom.com.jualanpraktis.adapter.PenghasilanSayaAdapter;
import www.starcom.com.jualanpraktis.adapter.PenghasilanSelesaiAdapter;
import www.starcom.com.jualanpraktis.adapter.PeriodeAdapter;
import www.starcom.com.jualanpraktis.feature.belanja_bulanan.ListBelanjaBulananFragment;
import www.starcom.com.jualanpraktis.feature.pembayaran.ListPembayaranFragment;
import www.starcom.com.jualanpraktis.feature.pesanan_saya.ListPesananFragment;
import www.starcom.com.jualanpraktis.home_dashboard;
import www.starcom.com.jualanpraktis.interfaces.PilihPeriodeClickInterface;
import www.starcom.com.jualanpraktis.katalog;
import www.starcom.com.jualanpraktis.keranjang;
import www.starcom.com.jualanpraktis.model.ListPenghasilanSaya;
import www.starcom.com.jualanpraktis.model.Periode;

public class PenghasilanSayaActivity extends AppCompatActivity {

    @BindView(R.id.btn_riwayat_pencairan)
    LinearLayout btnRiwayatPencairan;

    public static PenghasilanSayaActivity instance;
    TabLayout tabLayout;

    private PesananSelesaiFragment pesananSelesaiFragment;
    private PesananDiprosesFragment pesananDiprosesFragment;
    private PesananDibatalkanFragment pesananDibatalkanFragment;
    ImageView imgBack;
    LinearLayout linearRiwayatPencairan;
    TextView txtTotalPenghasilan;

    loginuser user;

    ArrayList<HashMap<String, String>> dataPeriode = new ArrayList<>();

    String penghasilanSaya;

    public TextView txtButton;

    public Dialog alertDialog;

    public String tanggalAwal, tanggalAkhir, semuaPeriode;

    public String range_periode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penghasilan_saya);
        ButterKnife.bind(this);

        user = SharedPrefManager.getInstance(PenghasilanSayaActivity.this).getUser();
        AndroidNetworking.initialize(getApplicationContext());

        imgBack = findViewById(R.id.imgBackPenghasilanSaya);
        linearRiwayatPencairan = findViewById(R.id.linear_riwayat_pencairan);
        txtTotalPenghasilan = findViewById(R.id.text_total_penghasilan_anda);
        txtButton = findViewById(R.id.text_button_periode);

        Periode getPeriode = new Periode();

        String start = getPeriode.getStart_date();
        String end = getPeriode.getEnd_date();

        Log.d("testDataModel", "start: "+start);
        Log.d("testDataModel", "end: "+end);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linearRiwayatPencairan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PenghasilanSayaActivity.this, RiwayatPencairanActivity.class);
                startActivity(intent);
            }
        });

        btnRiwayatPencairan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnRiwayatPencairan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilDialog();
            }
        });

        range_periode = txtButton.getText().toString();

        Log.d("rangePeriode", "onCreate: "+range_periode);

        if (tanggalAwal != null && tanggalAkhir != null){

            Log.d("periodeCheck", "onCreate: "+tanggalAwal+" - "+tanggalAkhir);

        }

        getPenghasilanSaya();

        String id_member = user.getId();

        instance=this;
        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();

    }

    private void tampilDialog() {

        alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_periode);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        RecyclerView rv_periode = alertDialog.findViewById(R.id.rv_periode);
        TextView txtSemuaPeriode = alertDialog.findViewById(R.id.text_semua_periode);
        rv_periode.setHasFixedSize(true);
        rv_periode.setLayoutManager(new LinearLayoutManager(PenghasilanSayaActivity.this));

        loadDataPeriode(rv_periode);

        txtSemuaPeriode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtButton.setText("Semua Periode");
                semuaPeriode = "semua periode";
                tanggalAwal = "semua periode";
                tanggalAkhir = "semua periode";
                alertDialog.dismiss();


            }
        });

        if (tanggalAwal!=null && tanggalAkhir!=null){

            Log.d("cekPeriode", "tampilDialog: "+tanggalAwal +" - "+tanggalAkhir);

//            reloadFragment();

        }

    }

    public void reloadFragment() {

        Fragment currentFragment = PenghasilanSayaActivity.this.getSupportFragmentManager()
                .findFragmentById(R.id.framePenghasilanSaya);
        FragmentManager manager = PenghasilanSayaActivity.this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();

    }

    private void loadDataPeriode(RecyclerView rv_periode) {

        AndroidNetworking.get("https://jualanpraktis.net/android/date-picker.php")
                .setTag(PenghasilanSayaActivity.this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        dataPeriode.clear();
                        try {

                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                data.put("id_periode", obj.getString("id_date"));
                                data.put("tanggal_awal", obj.getString("start_date"));
                                data.put("tanggal_akhir", obj.getString("end_date"));
                                dataPeriode.add(data);
                            }

                            PeriodeAdapter adapterPeriode = new PeriodeAdapter(PenghasilanSayaActivity.this, dataPeriode,
                                    PenghasilanSayaActivity.this);
                            rv_periode.setAdapter(adapterPeriode);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(PenghasilanSayaActivity.this, error.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getPenghasilanSaya() {

        String url = "https://jualanpraktis.net/android/penghasilan.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("id_member", user.getId())
                .setTag(PenghasilanSayaActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                penghasilanSaya = jsonObject.getString("jumlah");
                            }

                            Log.d("penghasilanSayaAkun", "jumlah: "+penghasilanSaya);
                            int penghasilanSayaInt = Integer.parseInt(penghasilanSaya);
                            Locale localID = new Locale("in", "ID");
                            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localID);
                            txtTotalPenghasilan.setText("Rp" + NumberFormat.getInstance().format(penghasilanSayaInt));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(PenghasilanSayaActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(PenghasilanSayaActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(PenghasilanSayaActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    public static PenghasilanSayaActivity getInstance() {
        return instance;
    }
    private void getAllWidgets() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutPenghasilanSaya);
    }

    private void setupTabLayout() {

        String range = txtButton.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("range_periode", range);
//        tabLayout.removeAllTabs();
        pesananDiprosesFragment = new PesananDiprosesFragment();
        pesananDiprosesFragment.setArguments(bundle);
        pesananSelesaiFragment = new PesananSelesaiFragment();
        pesananDibatalkanFragment = new PesananDibatalkanFragment();
        // tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("Pesanan Sedang Diproses"));
        tabLayout.addTab(tabLayout.newTab().setText("Pesanan Selesai"));
        tabLayout.addTab(tabLayout.newTab().setText("Pesanan Dibatalkan"));

    }
    private void bindWidgetsWithAnEvent()
    {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0 :
                replaceFragment(pesananDiprosesFragment);
                break;
            case 1 :
                replaceFragment(pesananSelesaiFragment);
                break;
            case 2 :
                replaceFragment(pesananDibatalkanFragment);
                break;
        }
    }
    public void replaceFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("start_date", tanggalAwal);
        bundle.putString("end_date", tanggalAkhir);
        bundle.putString("semua_periode", semuaPeriode);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.framePenghasilanSaya, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragment.setArguments(bundle);
        ft.commit();
    }

    public interface FragmentRefreshListener{
        void onRefresh();
    }
}