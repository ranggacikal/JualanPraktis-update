package www.starcom.com.jualanpraktis.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.SubKategori.objectsub;
import www.starcom.com.jualanpraktis.feature.produk.ProdukDetailActivity;

public class DashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    ArrayList<HashMap<String, String>> produkList = new ArrayList<>();
    private List<objectsub.ObjectSub.Results> results;
    private List<objectsub.ObjectSub.Results> dataFilter;

    private Pref pref;


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private static final String RP = "Rp.";

    ShareDialog shareDialog;
    CallbackManager callbackManager;



    public DashboardAdapter(Context context, ArrayList<HashMap<String, String>> produkList) {
        this.context = context;
        this.produkList = produkList;
        this.dataFilter = results;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_sub_kategori, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return (null != produkList ? produkList.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return produkList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView gambar ;
        public LinearLayout linearWa, linearFb, linearSalin;
        public Button btnWa;
        public TextView nama_produk,harga_jual,harga_asli,diskon,txtTerjual ;
        public CardView cardView;
        public TextView txtStok, txtKota;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            gambar = itemView.findViewById(R.id.gambar_kategori);
            nama_produk = itemView.findViewById(R.id.nama_produk);
            harga_jual = itemView.findViewById(R.id.harga_jual);
            harga_asli = itemView.findViewById(R.id.harga_asli);
            diskon = itemView.findViewById(R.id.diskon);
            cardView = itemView.findViewById(R.id.cardview);
            linearFb = itemView.findViewById(R.id.share_fb_home);
            linearWa = itemView.findViewById(R.id.share_whatsapp_home);
            linearSalin = itemView.findViewById(R.id.share_salin_home);
            txtStok = itemView.findViewById(R.id.item_id_stock_produk);
            txtTerjual = itemView.findViewById(R.id.text_id_qty_produk_dibeli);
            txtKota = itemView.findViewById(R.id.kota_produk);

        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        viewHolder.progressBar.showContextMenu();

    }

    private void populateItemRows(ItemViewHolder holder, int position) {
        HashMap<String, String> item = new HashMap<>();
        item = this.produkList.get(position);
        pref = new Pref(context.getApplicationContext());

        final String UrlImage = "https://jualanpraktis.net/img/";
        final String Image = item.get("image_o");
        final Uri uri = Uri.parse(UrlImage + Image);
        final String harga = item.get("harga");
        final String diskom;
        final int harga_disc;

        callbackManager = CallbackManager.Factory.create();


        if (item.get("disc").equals("0") || item.get("disc") == null || item.get("disc").equals("")) {
            diskom = "1";
            harga_disc = Integer.parseInt(harga) * Integer.parseInt(diskom);
            holder.harga_asli.setVisibility(View.GONE);
            holder.diskon.setVisibility(View.GONE);
        } else {
            diskom = item.get("disc");
            int total_disc_harga = Integer.parseInt(harga) * Integer.parseInt(diskom) / 100;
            harga_disc = Integer.parseInt(harga) - total_disc_harga;
        }
        //  final int harga_disc = Integer.parseInt(harga)*Integer.parseInt(diskom)/100;
        NumberFormat nf = new DecimalFormat("#,###");
        final String hrg = nf.format(Integer.parseInt(harga));
        holder.nama_produk.setText(item.get("nama_produk"));
        //   holder.harga_jual.setText(String.format("%s%s", RP, hrg));
        holder.harga_asli.setText("Rp" + NumberFormat.getInstance().format(Double.parseDouble(item.get("harga"))));
        holder.diskon.setText("(" + item.get("disc") + "%)");
        holder.harga_asli.setPaintFlags(holder.harga_asli.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.harga_jual.setText("Rp" + NumberFormat.getInstance().format(harga_disc));
        holder.txtStok.setText(item.get("total_stock"));
        holder.txtTerjual.setText(item.get("terjual") + " Produk Terjual");
        holder.txtKota.setText(item.get("city_name"));

        final String gambar = item.get("image_o");

        Glide.with(context)
                .load(gambar)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(200, 200).skipMemoryCache(false))
                .into(holder.gambar);

        String id_sub_kategori_produk = item.get("id_sub_kategori_produk");
        String id_produk = item.get("id_produk");
        String id_member = item.get("id_member");
        String kode = item.get("kode");
        String nama_produk = item.get("nama_produk");
        String harga_jual = item.get("harga");
        String keterangan_produk = item.get("keterangan_produk");
        String image_o = item.get("image_o");
        String berat = item.get("berat");
        String harga_asli = item.get("harga_asli");
        String stok = item.get("total_stok");
        String diskon = item.get("disc");
        String produk_terjual = item.get("terjual");
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
                intent.putExtra("image_o", image_o);
                intent.putExtra("berat", berat);
                intent.putExtra("harga_asli", harga_asli);
                intent.putExtra("stok", stok);
                intent.putExtra("diskon", diskon);
                intent.putExtra("produkTerjual", produk_terjual);
                v.getContext().startActivity(intent);
            }
        });

        holder.linearFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.gambar.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                SharePhoto sharePhoto = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();

                SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build();

                shareDialog.show(sharePhotoContent);
            }
        });

        holder.linearWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.gambar.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                String bitmpath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "shareWhatsapp", null);


                Uri uri = Uri.parse(bitmpath);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                shareIntent.setPackage("com.whatsapp");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                context.startActivity(Intent.createChooser(shareIntent, "Bagikan Dengan"));
            }
        });

        holder.linearSalin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spanned detailProduk = Html.fromHtml(results.get(position).keterangan);
                String nama = results.get(position).nama_produk;

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(nama, detailProduk);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Berhasil menyalin deskripsi", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
