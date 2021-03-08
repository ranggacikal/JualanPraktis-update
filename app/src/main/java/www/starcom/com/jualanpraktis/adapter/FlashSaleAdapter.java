package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.SubKategori.objectsub;

public class FlashSaleAdapter extends RecyclerView.Adapter<FlashSaleAdapter.FlashSaleViewHolder> {

    public Context context;
    private List<objectsub.ObjectSub.Results> results;
    private List<objectsub.ObjectSub.Results> dataFilter;

    ShareDialog shareDialog;
    CallbackManager callbackManager;

    public FlashSaleAdapter(Context context, List<objectsub.ObjectSub.Results> results) {
        this.context = context;
        this.results = results;
        this.dataFilter = results;
    }

    @NonNull
    @Override
    public FlashSaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flash_sale, parent, false);
        return new FlashSaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashSaleViewHolder holder, int position) {

        final String UrlImage = "https://jualanpraktis.net/img/";
        final String Image = results.get(position).gambar;
        final Uri uri = Uri.parse(UrlImage + Image);
        final String harga = results.get(position).harga_asli;
        final String diskom;
        final int harga_disc;

        callbackManager = CallbackManager.Factory.create();

        if (results.get(position).diskon.equals("0") || results.get(position).diskon == null || results.get(position).diskon.equals("")) {
            diskom = "1";
            harga_disc = Integer.parseInt(harga) * Integer.parseInt(diskom);
            holder.txtHargaJual.setVisibility(View.GONE);
            holder.txtDiskon.setVisibility(View.GONE);
        } else {
            diskom = results.get(position).diskon;
            int total_disc_harga = Integer.parseInt(harga) * Integer.parseInt(diskom) / 100;
            harga_disc = Integer.parseInt(harga) - total_disc_harga;
        }

        NumberFormat nf = new DecimalFormat("#,###");
        final String hrg = nf.format(Integer.parseInt(harga));
        holder.txtNamaProduk.setText(results.get(position).nama_produk);
        //   holder.harga_jual.setText(String.format("%s%s", RP, hrg));
        holder.txtHargaJual.setText("Rp"+NumberFormat.getInstance().format(Double.parseDouble(results.get(position).harga_asli)));
        holder.txtDiskon.setText(results.get(position).diskon + "%");
        holder.txtHargaJual.setPaintFlags(holder.txtHargaJual.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtHargaDiskon.setText("Rp"+NumberFormat.getInstance().format(harga_disc));
        holder.txtDibeli.setText(results.get(position).terjual+" Dibeli");

        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(200, 200).skipMemoryCache(false))
                .into(holder.imgFlashSale);

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class FlashSaleViewHolder extends RecyclerView.ViewHolder {

        ImageView imgFlashSale;
        TextView txtDiskon, txtNamaProduk, txtHargaJual, txtHargaDiskon, txtDibeli;
        Button btnBeli;

        public FlashSaleViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFlashSale = itemView.findViewById(R.id.img_item_flash_sale);
            txtDiskon = itemView.findViewById(R.id.text_diskon_flashsale);
            txtNamaProduk = itemView.findViewById(R.id.text_nama_produk_flash_sale);
            txtHargaJual = itemView.findViewById(R.id.text_harga_asli_flash_sale);
            txtHargaDiskon = itemView.findViewById(R.id.text_harga_diskon_flash_sale);
            txtDibeli = itemView.findViewById(R.id.text_dibeli_flashsale);
            btnBeli = itemView.findViewById(R.id.btn_beli_flashsale);
        }
    }
}
