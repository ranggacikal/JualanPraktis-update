package www.starcom.com.jualanpraktis.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.akun.ProdukFavoritActivity;
import www.starcom.com.jualanpraktis.feature.produk.ProdukDetailActivity;
import www.starcom.com.jualanpraktis.model.ListFavorit;
import www.starcom.com.jualanpraktis.model_retrofit.DataFavoriteItem;

public class FavoritAdapter extends RecyclerView.Adapter<FavoritAdapter.FavoritViewHolder> {

    Context context;
    private ArrayList<HashMap<String, String>> data;

    public FavoritAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        this.data = data;
    }

    int harga_disc;

    @NonNull
    @Override
    public FavoritViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorit, parent, false);
        return new FavoritViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritViewHolder holder, int position) {
        HashMap<String, String> item = new HashMap<>();
        item = this.data.get(position);
        final String harga = item.get("harga");
        final String diskom;


        if (item.get("diskon").equals("0") || item.get("diskon") == null || item.get("diskon").equals("")) {
            diskom = "1";
            harga_disc = Integer.parseInt(harga) * Integer.parseInt(diskom);
            holder.harga_asli.setVisibility(View.GONE);
            holder.diskon.setVisibility(View.GONE);
        } else {
            if (item.get("diskon")!=null && harga!=null) {
                diskom = item.get("diskon");
                int total_disc_harga = Integer.parseInt(harga) * Integer.parseInt(diskom) / 100;
                harga_disc = Integer.parseInt(harga) - total_disc_harga;
            }
        }

        final String imagename = item.get("gambar");
        final String imageUrl = "https://jualanpraktis.net/img/" + imagename;

        holder.txtNamaBarang.setText(item.get("nama_produk"));
        //   holder.harga_jual.setText(String.format("%s%s", RP, hrg));
        holder.harga_asli.setText("Rp" + NumberFormat.getInstance().format(Double.parseDouble(item.get("harga"))));
        holder.diskon.setText("(" + item.get("diskon") + "%)");
        holder.harga_asli.setPaintFlags(holder.harga_asli.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtHargaBarang.setText("Rp" + NumberFormat.getInstance().format(harga_disc));
        holder.txtStok.setText(item.get("total_stok"));
        holder.txtTerjual.setText(item.get("terjual") + " Produk Terjual");

        Glide.with(context)
                .load(imagename)
                .into(holder.imgFavorit);

        String id_sub_kategori_produk = item.get("id_sub_kategori_produk");
        String id_produk = item.get("id_produk");
        String id_member = item.get("id_member");
        String kode = item.get("kode");
        String nama_produk = item.get("nama_produk");
        String keterangan_produk = item.get("keterangan_produk");
        String gambar = item.get("gambar");
        String berat = item.get("berat");
        String harga_asli2 = item.get("harga");
        String stok = item.get("total_stok");
        String diskon = item.get("diskon");
        String terjual = item.get("terjual");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProdukDetailActivity.class);
                intent.putExtra("id_sub_kategori_produk", id_sub_kategori_produk);
                intent.putExtra("id_produk", id_produk);
                intent.putExtra("id_member", id_member);
                intent.putExtra("kode", kode);
                intent.putExtra("nama_produk", nama_produk);
                intent.putExtra("harga_jual", Integer.toString(harga_disc));
                intent.putExtra("keterangan_produk", keterangan_produk);
                intent.putExtra("image_o", gambar);
                intent.putExtra("berat", berat);
                intent.putExtra("harga_asli", harga_asli2);
                intent.putExtra("stok", stok);
                intent.putExtra("diskon", diskon);
                intent.putExtra("produkTerjual", terjual);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public class FavoritViewHolder extends RecyclerView.ViewHolder {

        ImageView imgFavorit;
        TextView txtNamaBarang, txtHargaBarang, harga_asli, diskon, txtTerjual, txtStok;
        public CardView cardView;

        public FavoritViewHolder(@NonNull View itemView) {
            super(itemView);

            imgFavorit = itemView.findViewById(R.id.img_item_favorit);
            txtNamaBarang = itemView.findViewById(R.id.text_nama_barang_favorit);
            txtHargaBarang = itemView.findViewById(R.id.text_id_harga_favorit);
            harga_asli = itemView.findViewById(R.id.harga_asli_favorit);
            diskon = itemView.findViewById(R.id.diskon_favorit);
            txtTerjual = itemView.findViewById(R.id.text_produk_terjual_favorit);
            txtStok = itemView.findViewById(R.id.text_stock_favorit);
            cardView = itemView.findViewById(R.id.cardview_favorit);
        }
    }
}
