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

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> listProduk = new ArrayList<>();
    private Pref pref;

    public CheckoutAdapter(Context context, ArrayList<HashMap<String, String>> listProduk) {
        this.context = context;
        this.listProduk = listProduk;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk_checkout, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {

        HashMap<String, String> item = new HashMap<>();
        item = this.listProduk.get(position);
        pref = new Pref(context.getApplicationContext());

        String gambar = item.get("gambar");
        String url = "https://jualanpraktis.net/img/"+gambar;

        Glide.with(context)
                .load(gambar)
                .error(R.drawable.logo_jualan_merah)
                .into(holder.imgProdukCheckout);

        int harga_produk = Integer.parseInt(item.get("harga_item"));
        int total_produk = Integer.parseInt(item.get("harga_jual"));
        int harga_jual = Integer.parseInt(item.get("harga_item2"));
        int total_jual = Integer.parseInt(item.get("harga_jual2"));
        int untung1 = Integer.parseInt(item.get("untung"));
        int untung2 = Integer.parseInt(item.get("subtotal_untung"));

        Locale localID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localID);

        holder.txtNamaBarangCheckout.setText(item.get("nama_produk"));
        holder.txtVariasiCheckout.setText(item.get("variasi"));
        holder.txtHargaProdukCheckout.setText("Rp."+NumberFormat.getInstance().format(harga_produk));
        holder.txtJumlahProdukCheckout.setText("x"+item.get("jumlah"));
        holder.txtTotalProdukCheckout.setText("Rp."+NumberFormat.getInstance().format(total_produk));
        holder.txtHargaJualCheckout.setText("Rp."+NumberFormat.getInstance().format(harga_jual));
        holder.txtJumlahJualCheckout.setText("x"+item.get("jumlah"));
        holder.txtTotalJualCheckout.setText("Rp."+NumberFormat.getInstance().format(total_jual));
        holder.txtKeuntunganCheckout.setText("Rp."+NumberFormat.getInstance().format(untung1));
        holder.txtJumlahKeuntunganCheckout.setText("x"+item.get("jumlah"));
        holder.txtTotalKeuntunganCheckout.setText("Rp."+NumberFormat.getInstance().format(untung2));

    }

    @Override
    public int getItemCount() {
        return (null != listProduk ? listProduk.size() : 0);
    }

    public class CheckoutViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProdukCheckout;
        TextView txtNamaBarangCheckout, txtVariasiCheckout, txtHargaJualCheckout, txtJumlahJualCheckout, txtTotalJualCheckout
                , txtHargaProdukCheckout, txtJumlahProdukCheckout, txtTotalProdukCheckout
                , txtKeuntunganCheckout, txtJumlahKeuntunganCheckout, txtTotalKeuntunganCheckout;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProdukCheckout = itemView.findViewById(R.id.img_produk_checkout);
            txtNamaBarangCheckout = itemView.findViewById(R.id.text_nama_barang_checkout);
            txtVariasiCheckout = itemView.findViewById(R.id.text_variasi_checkout);
            txtHargaProdukCheckout = itemView.findViewById(R.id.text_harga_produk_checkout);
            txtJumlahProdukCheckout = itemView.findViewById(R.id.text_jumlah_produk_checkout);
            txtTotalProdukCheckout = itemView.findViewById(R.id.text_total_produk_checkout);
            txtHargaJualCheckout = itemView.findViewById(R.id.text_harga_jual_checkout);
            txtJumlahJualCheckout = itemView.findViewById(R.id.text_jumlah_harga_jual_checkout);
            txtTotalJualCheckout = itemView.findViewById(R.id.text_total_harga_jual_checkout);
            txtKeuntunganCheckout = itemView.findViewById(R.id.text_keuntungan_checkout);
            txtJumlahKeuntunganCheckout = itemView.findViewById(R.id.text_jumlah_keuntungan_checkout);
            txtTotalKeuntunganCheckout = itemView.findViewById(R.id.text_total_keuntungan_checkout);


        }
    }
}
