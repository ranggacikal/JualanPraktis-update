package www.starcom.com.jualanpraktis.SubKategori;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.feature.produk.ProdukDetailActivity;

/**
 * Created by ADMIN on 06/02/2018.
 */

public class adaptersub extends RecyclerView.Adapter<holdersub> {

    private List<objectsub.ObjectSub.Results> results;
    private List<objectsub.ObjectSub.Results> dataFilter;
    ArrayList<List<objectsub.ObjectSub.Results>> produkList = new ArrayList<List<objectsub.ObjectSub.Results>>();
    public Context context;

    private static final String RP = "Rp.";

    ShareDialog shareDialog;
    CallbackManager callbackManager;

    public adaptersub(Context context, List<objectsub.ObjectSub.Results> results) {
        this.context = context;
        this.results = results;
        this.dataFilter = results;
    }

    @Override
    public holdersub onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_sub_kategori, null);
        holdersub holder = new holdersub(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(holdersub holder, final int position) {


        final String UrlImage = "https://jualanpraktis.net/img/";
        final String Image = results.get(position).gambar;
        final Uri uri = Uri.parse(UrlImage + Image);
        final String harga = results.get(position).harga_asli;
        final String diskom;
        final int harga_disc;

        /** if (results.get(position).end_disc!=nu
         String valid_until = results.get(position).end_disc;
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
         Date strDate = null;
         try {
         strDate = sdf.parse(valid_until);
         if (new Date().after(strDate)) {
         results.get(position).diskon = "0";
         }else if (new Date().equals(strDate)){
         results.get(position).diskon = "0";
         }
         } catch (ParseException e) {
         e.printStackTrace();
         }

         } **/

        callbackManager = CallbackManager.Factory.create();

//        shareDialog = new ShareDialog(context);
//        shareDialog.registerCallback(callbackManager, callback);


        if (results.get(position).diskon.equals("0") || results.get(position).diskon == null || results.get(position).diskon.equals("")) {
            diskom = "1";
            harga_disc = Integer.parseInt(harga) * Integer.parseInt(diskom);
            holder.harga_asli.setVisibility(View.GONE);
            holder.diskon.setVisibility(View.GONE);
        } else {
            diskom = results.get(position).diskon;
            int total_disc_harga = Integer.parseInt(harga) * Integer.parseInt(diskom) / 100;
            harga_disc = Integer.parseInt(harga) - total_disc_harga;
        }
        //  final int harga_disc = Integer.parseInt(harga)*Integer.parseInt(diskom)/100;
        NumberFormat nf = new DecimalFormat("#,###");
        final String hrg = nf.format(Integer.parseInt(harga));
        holder.nama_produk.setText(results.get(position).nama_produk);
        //   holder.harga_jual.setText(String.format("%s%s", RP, hrg));
        holder.harga_asli.setText("Rp" + NumberFormat.getInstance().format(Double.parseDouble(results.get(position).harga_asli)));
        holder.diskon.setText("(" + results.get(position).diskon + "%)");
        holder.harga_asli.setPaintFlags(holder.harga_asli.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.harga_jual.setText("Rp" + NumberFormat.getInstance().format(harga_disc));
        holder.txtStok.setText(results.get(position).total_stok);
        holder.txtTerjual.setText(results.get(position).terjual + " Produk Terjual");
        holder.txtKota.setText(results.get(position).kota);

        String linkGambar = results.get(position).gambar;

        Glide.with(context)
                .load(linkGambar)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(200, 200).skipMemoryCache(false))
                .into(holder.gambar);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProdukDetailActivity.class);
                intent.putExtra("id_sub_kategori_produk", results.get(position).id_sub_kategori_produk);
                intent.putExtra("id_produk", results.get(position).id_produk);
                intent.putExtra("id_member", results.get(position).id_member);
                intent.putExtra("kode", results.get(position).kode);
                intent.putExtra("nama_produk", results.get(position).nama_produk);
                intent.putExtra("harga_jual", Integer.toString(harga_disc));
                intent.putExtra("keterangan_produk", results.get(position).keterangan);
                intent.putExtra("image_o", results.get(position).gambar);
                intent.putExtra("berat", results.get(position).berat);
                intent.putExtra("harga_asli", results.get(position).harga_asli);
                intent.putExtra("stok", results.get(position).total_stok);
                intent.putExtra("diskon", results.get(position).diskon);
                intent.putExtra("produkTerjual", results.get(position).terjual);
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

    @Override
    public int getItemCount() {
        return results.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    results = dataFilter;
                } else {
                    ArrayList<objectsub.ObjectSub.Results> filteredList = new ArrayList<>();
                    for (objectsub.ObjectSub.Results row : dataFilter) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.nama_produk.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    results = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = results;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                results = (ArrayList<objectsub.ObjectSub.Results>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    private FacebookCallback<Sharer.Result> callback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {
            Log.v("UserProfile", "Successfully posted");
            // Write some code to do some operations when you shared content successfully.
        }

        @Override
        public void onCancel() {
            Log.v("UserProfile", "Sharing cancelled");
            // Write some code to do some operations when you cancel sharing content.
        }

        @Override
        public void onError(FacebookException error) {
            Log.v("UserProfile", error.getMessage());
            // Write some code to do some operations when some error occurs while sharing content.
        }
    };

}
