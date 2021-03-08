package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.R;

public class UploadBuktiAdapter extends RecyclerView.Adapter<UploadBuktiAdapter.UploadBuktiViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> listProdukUpload = new ArrayList<>();
    private Pref pref;

    public UploadBuktiAdapter(Context context, ArrayList<HashMap<String, String>> listProdukUpload) {
        this.context = context;
        this.listProdukUpload = listProdukUpload;
    }

    @NonNull
    @Override
    public UploadBuktiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk_rincian_transaksi, parent, false);
        return new UploadBuktiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadBuktiViewHolder holder, int position) {

        HashMap<String, String> item = new HashMap<>();
        item = this.listProdukUpload.get(position);
        pref = new Pref(context.getApplicationContext());

        String gambar = item.get("gambar");
        String url = "https://trading.my.id/img/"+gambar;

        Glide.with(context)
                .load(url)
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

    }

    @Override
    public int getItemCount() {
        return (null != listProdukUpload ? listProdukUpload.size() : 0);
    }

    public class UploadBuktiViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProdukRincian;
        TextView txtNamaBarang, txtVariasi, txtHargaJual, txtJumlahJual, txtTotalJual
                , txtHargaProduk, txtJumlahProduk, txtTotalProduk, txtKeuntungan, txtJumlahKeuntungan, txtTotalKeuntungan;

        public UploadBuktiViewHolder(@NonNull View itemView) {
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
        }
    }
}
