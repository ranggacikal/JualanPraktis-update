package www.starcom.com.jualanpraktis.feature.akun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.starcom.com.jualanpraktis.R;

public class AlasanPembatalanActivity extends AppCompatActivity {

    @BindView(R.id.imgBackAlasanPembatalan)
    ImageView imgBackAlasanPembatalan;
    @BindView(R.id.text_id_pembatalan_status_transaksi)
    TextView textIdPembatalanStatusTransaksi;
    @BindView(R.id.text_tanggal_pembatalan_status_transaksi)
    TextView textTanggalPembatalanStatusTransaksi;
    @BindView(R.id.text_pembatalan_rincian_status_transaksi)
    TextView textPembatalanRincianStatusTransaksi;
    @BindView(R.id.img_barang_pembatalan)
    ImageView imgBarangPembatalan;
    @BindView(R.id.text_nama_barang_pembatalan)
    TextView textNamaBarangPembatalan;
    @BindView(R.id.text_harga_produk_pembatalan)
    TextView textHargaProdukPembatalan;
    @BindView(R.id.text_harga_jual_pembatalan)
    TextView textHargaJualPembatalan;
    @BindView(R.id.text_keuntungan_pembatalan)
    TextView textKeuntunganPembatalan;
    @BindView(R.id.edt_masukan_alasan)
    EditText edtMasukanAlasan;
    @BindView(R.id.img_pembatalan_1)
    ImageView imgPembatalan1;
    @BindView(R.id.img_pembatalan_2)
    ImageView imgPembatalan2;
    @BindView(R.id.img_pembatalan_3)
    ImageView imgPembatalan3;
    @BindView(R.id.img_pembatalan_4)
    ImageView imgPembatalan4;
    @BindView(R.id.linear_ajukan_pembatalan)
    LinearLayout linearAjukanPembatalan;

    public static final String ExtraId = "extraId";
    public static final String ExtraTanggal = "extraTanggal";
    public static final String ExtraStatus = "extraStatus";
    public static final String ExtraImage = "extraImage";
    public static final String ExtraNama = "extraNama";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alasan_pembatalan);
        ButterKnife.bind(this);

        textIdPembatalanStatusTransaksi.setText(getIntent().getStringExtra(ExtraId));
        textTanggalPembatalanStatusTransaksi.setText(getIntent().getStringExtra(ExtraTanggal));
        textPembatalanRincianStatusTransaksi.setText(getIntent().getStringExtra(ExtraStatus));
        textNamaBarangPembatalan.setText(getIntent().getStringExtra(ExtraNama));

        final String img = getIntent().getStringExtra(ExtraImage);

        Glide.with(AlasanPembatalanActivity.this)
                .load(img)
                .into(imgBarangPembatalan);

        Glide.with(AlasanPembatalanActivity.this)
                .load(img)
                .into(imgPembatalan1);

        linearAjukanPembatalan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlasanPembatalanActivity.this, StatusTransaksiActivity.class);
                startActivity(intent);
            }
        });
    }
}