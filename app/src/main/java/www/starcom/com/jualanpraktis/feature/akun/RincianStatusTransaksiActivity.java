package www.starcom.com.jualanpraktis.feature.akun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.adapter.StatusTransaksiAdapter;
import www.starcom.com.jualanpraktis.model.ListStatusTransaksi;

public class RincianStatusTransaksiActivity extends AppCompatActivity {

    @BindView(R.id.imgBackRincianTransaksi)
    ImageView imgBackRincianTransaksi;
    @BindView(R.id.text_id_rincian_status_transaksi)
    TextView textIdRincianStatusTransaksi;
    @BindView(R.id.text_tanggal_rincian_status_transaksi)
    TextView textTanggalRincianStatusTransaksi;
    @BindView(R.id.text_status_rincian_status_transaksi)
    TextView textStatusRincianStatusTransaksi;
    @BindView(R.id.recycler_rincian_transaksi)
    RecyclerView recyclerRincianTransaksi;
    @BindView(R.id.text_hargaproduk_rincian)
    TextView textHargaprodukRincian;
    @BindView(R.id.text_keuntungan_rincian)
    TextView textKeuntunganRincian;
    @BindView(R.id.linear_batalkan_pesanan)
    LinearLayout linearBatalkanPesanan;

    public static final String ExtraId = "extraId";
    public static final String ExtraTanggal = "extraTanggal";
    public static final String ExtraStatus = "extraStatus";
    public static final String ExtraImage = "extraImage";
    public static final String ExtraNama = "extraNama";

    String urlimage1 = "https://www.static-src.com/wcsstore/Indraprastha/images/catalog/full//83/MTA-4603141/dettol_dettol_hand_sanitizer_50ml_full02_rae7qksc.jpg";
    String urlimage2 = "https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2020/10/6/162986da-65e8-4f00-afbf-ad8812e5a2eb.jpg";

    ListStatusTransaksi[] listStatusTransaksi = new ListStatusTransaksi[]{

            new ListStatusTransaksi("#ID99887732", "28 Desember 2020", "Hand Sanitizer", "Kesehatan/Medis", "Rp. 15.000", "Rp. 25.000",
                    "Rp. 10. 000", "Dipesan", urlimage1),
            new ListStatusTransaksi("#ID55533627", "29 Desember 2020", "Crewneck", "Pakaian", "Rp. 150.000", "Rp. 250.000",
                    "Rp. 100. 000", "Dipesan", urlimage2)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian_status_transaksi);
        ButterKnife.bind(this);

        textIdRincianStatusTransaksi.setText(getIntent().getStringExtra(ExtraId));
        textTanggalRincianStatusTransaksi.setText(getIntent().getStringExtra(ExtraTanggal));
        textStatusRincianStatusTransaksi.setText(getIntent().getStringExtra(ExtraStatus));

//        loadRecycler();

        linearBatalkanPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RincianStatusTransaksiActivity.this, AlasanPembatalanActivity.class);
                intent.putExtra(AlasanPembatalanActivity.ExtraId, getIntent().getStringExtra(ExtraId));
                intent.putExtra(AlasanPembatalanActivity.ExtraTanggal, getIntent().getStringExtra(ExtraTanggal));
                intent.putExtra(AlasanPembatalanActivity.ExtraStatus, getIntent().getStringExtra(ExtraStatus));
                intent.putExtra(AlasanPembatalanActivity.ExtraImage, getIntent().getStringExtra(ExtraImage));
                intent.putExtra(AlasanPembatalanActivity.ExtraNama, getIntent().getStringExtra(ExtraNama));
                startActivity(intent);
            }
        });
    }

    private void loadRecycler() {

//        StatusTransaksiAdapter adapter = new StatusTransaksiAdapter(RincianStatusTransaksiActivity.this, listStatusTransaksi);
//        recyclerRincianTransaksi.setHasFixedSize(true);
//        recyclerRincianTransaksi.setAdapter(adapter);
//        recyclerRincianTransaksi.setLayoutManager(new LinearLayoutManager(RincianStatusTransaksiActivity.this));

    }
}