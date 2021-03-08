package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.ChipGroup;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import butterknife.OnCheckedChanged;
import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.akun.TukarkanPesananActivity;

public class TukarPesananAdapter extends RecyclerView.Adapter<TukarPesananAdapter.TukarPesananViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> listProdukTukar = new ArrayList<>();
    private Pref pref;

    int index;
    private int selectedItem;

    TukarkanPesananActivity tukarkanPesananActivity;

    public TukarPesananAdapter(Context context, ArrayList<HashMap<String, String>> listProdukTukar, TukarkanPesananActivity tukarkanPesananActivity) {
        this.context = context;
        this.listProdukTukar = listProdukTukar;
        this.tukarkanPesananActivity = tukarkanPesananActivity;
        selectedItem = -1;
    }

    @NonNull
    @Override
    public TukarPesananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk_rincian_transaksi, parent, false);
        return new TukarPesananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TukarPesananViewHolder holder, int position) {

        HashMap<String, String> item = new HashMap<>();
        item = this.listProdukTukar.get(position);
        pref = new Pref(context.getApplicationContext());

        index = position;

        String gambar = item.get("gambar");
        String url = "https://jualanpraktis.net/img/"+gambar;

        Glide.with(context)
                .load(gambar)
                .error(R.drawable.logo_jualan_merah)
                .into(holder.imgProdukRincian);

        int harga_produk = Integer.parseInt(item.get("harga_produk"));
        int total_produk = Integer.parseInt(item.get("harga_jual"));
        int harga_jual = Integer.parseInt(item.get("harga_produk2"));
        int total_jual = Integer.parseInt(item.get("harga_jual2"));
        int untung1 = Integer.parseInt(item.get("untung1"));
        int untung2 = Integer.parseInt(item.get("untung2"));

        Locale localID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localID);

        holder.txtNamaBarang.setText(item.get("nama_produk"));
        holder.txtVariasi.setText(item.get("variasi"));
        holder.txtHargaProduk.setText(formatRupiah.format(harga_produk));
        holder.txtJumlahProduk.setText("x"+item.get("jumlah"));
        holder.txtTotalProduk.setText(formatRupiah.format(total_produk));
        holder.txtHargaJual.setText(formatRupiah.format(harga_jual));
        holder.txtJumlahJual.setText("x"+item.get("jumlah"));
        holder.txtTotalJual.setText(formatRupiah.format(total_jual));
        holder.txtKeuntungan.setText(formatRupiah.format(untung1));
        holder.txtJumlahKeuntungan.setText("x"+item.get("jumlah"));
        holder.txtTotalKeuntungan.setText(formatRupiah.format(untung2));

        String nomor = item.get("nomor");

        holder.checkBox.setVisibility(View.VISIBLE);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(holder.itemView.isSelected());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked==true) {
                    holder.itemView.setSelected(isChecked);
                    tukarkanPesananActivity.dataNomor.add(nomor);
                }else{
                    int index = tukarkanPesananActivity.dataNomor.size() - 1;
                    tukarkanPesananActivity.dataNomor.remove(index);
                }
            }
        });


//        else{
//            if (tukarkanPesananActivity.dataNomor.size()>0) {
//                int index = tukarkanPesananActivity.dataNomor.size() - 1;
//                tukarkanPesananActivity.dataNomor.remove(index);
//                Toast.makeText(context, "Batalkan dan hapus", Toast.LENGTH_SHORT).show();
//            }
//        }

    }

    @Override
    public int getItemCount() {
        return (null != listProdukTukar ? listProdukTukar.size() : 0);
    }

    public class TukarPesananViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProdukRincian, imgChecked;
        TextView txtNamaBarang, txtVariasi, txtHargaJual, txtJumlahJual, txtTotalJual
                , txtHargaProduk, txtJumlahProduk, txtTotalProduk, txtKeuntungan, txtJumlahKeuntungan, txtTotalKeuntungan;
        CheckBox checkBox;

        public TukarPesananViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProdukRincian = itemView.findViewById(R.id.img_produk_rincian);
            txtNamaBarang = itemView.findViewById(R.id.text_nama_barang_rincian);
            txtVariasi = itemView.findViewById(R.id.text_variasi_rincian);
            txtHargaProduk = itemView.findViewById(R.id.text_harga_produk_rincian);
            txtJumlahProduk = itemView.findViewById(R.id.text_jumlah_produk_rincian);
            txtTotalProduk = itemView.findViewById(R.id.text_total_produk_rincian);
            txtHargaJual = itemView.findViewById(R.id.text_harga_jual_rincian);
            txtJumlahJual = itemView.findViewById(R.id.text_jumlah_harga_jual_rincian);
            txtTotalJual = itemView.findViewById(R.id.text_total_harga_jual_rincian);
            txtKeuntungan = itemView.findViewById(R.id.text_keuntungan_rincian);
            txtJumlahKeuntungan = itemView.findViewById(R.id.text_jumlah_keuntungan_rincian);
            txtTotalKeuntungan = itemView.findViewById(R.id.text_total_keuntungan_rincian);
            imgChecked = itemView.findViewById(R.id.img_checked_tukar);
            checkBox = itemView.findViewById(R.id.checkBox_tukar);
        }
    }
}
