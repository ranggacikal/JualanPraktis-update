package www.starcom.com.jualanpraktis.adapter;

import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
import www.starcom.com.jualanpraktis.model.ResultsProduk;

/**
 * Created by Suleiman on 19/10/16.
 */

public class ProdukPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<ResultsProduk> data;
    private Context context;

    private boolean isLoadingAdded = false;

    public ProdukPaginationAdapter(Context context) {
        this.context = context;

        data = new ArrayList<>();
    }

    public List<ResultsProduk> getMovies() {
        return data;
    }

    public void setMovies(List<ResultsProduk> data) {
        this.data = data;
    }

    ShareDialog shareDialog;
    CallbackManager callbackManager;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.cardview_sub_kategori, parent, false);
        viewHolder = new ViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        callbackManager = CallbackManager.Factory.create();

        shareDialog = new ShareDialog((Activity) context);
        shareDialog.registerCallback(callbackManager, callback);

        ResultsProduk item = data.get(position);


        switch (getItemViewType(position)) {
            case ITEM:
                ViewHolder viewHolder = (ViewHolder) holder;

                final String UrlImage = "https://jualanpraktis.net/img/";
                final String Image = item.gambar;
                final Uri uri = Uri.parse(UrlImage+Image);
                final String harga = item.harga_asli ;
                final String diskom;
                final int harga_disc;

            /**    if (item.end_disc!=null){
                    String valid_until = item.end_disc;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date strDate = null;
                    Date date=java.util.Calendar.getInstance().getTime();
                    try {

                        strDate = sdf.parse(valid_until);
                      //  Date now = formatter.format(date);
                        if (date.after(strDate)) {
                           item.diskon = "0";
                        }else if (date.equals(strDate)){
                            item.diskon = "0";
                        }else {
                            item.diskon = item.diskon;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } **/

                if ( item.diskon.equals("0")||item.diskon==null||item.diskon.equals("")){
                    diskom = "1";
                    harga_disc = Integer.parseInt(harga)*Integer.parseInt(diskom);
                    viewHolder.harga_asli.setVisibility(View.GONE);
                    viewHolder.diskon.setVisibility(View.GONE);
                }else {
                    diskom  = item.diskon;
                    int total_disc_harga = Integer.parseInt(harga)*Integer.parseInt(diskom)/100;
                    harga_disc = Integer.parseInt(harga)-total_disc_harga;
                }
                //  final int harga_disc = Integer.parseInt(harga)*Integer.parseInt(diskom)/100;
                NumberFormat nf = new DecimalFormat("#,###");
                final String hrg = nf.format(Integer.parseInt(harga));
                viewHolder.nama_produk.setText(item.nama_produk);
                //   holder.harga_jual.setText(String.format("%s%s", RP, hrg));
                viewHolder.harga_asli.setText(FormatText.rupiahFormat(Double.parseDouble(item.harga_asli)));
                viewHolder.diskon.setText("("+item.diskon +"%)");
                viewHolder.harga_asli.setPaintFlags(viewHolder.harga_asli.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.harga_jual.setText(FormatText.rupiahFormat(harga_disc));
                viewHolder.txtStok.setText(item.total_stok);
                viewHolder.txtKota.setText(item.kota);
                viewHolder.txtTerjual.setText(item.terjual+" Terjual");

                //  Picasso.get().load(UrlImage+Image).into(holder.gambar);

                viewHolder.shareWa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) viewHolder.gambar.getDrawable();
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

                viewHolder.shareSalin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Spanned detailProduk = Html.fromHtml(data.get(position).keterangan);
                        String nama = data.get(position).nama_produk;

                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText(nama, detailProduk);
                        clipboard.setPrimaryClip(clip);

                        Toast.makeText(context,"Berhasil menyalin deskripsi",Toast.LENGTH_SHORT).show();
                    }
                });

                viewHolder.shareFb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        BitmapDrawable bitmapDrawable = (BitmapDrawable) viewHolder.gambar.getDrawable();
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


                Glide.with(context)
                        .load(Image)
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(200,200).skipMemoryCache(false))
                        .into(viewHolder.gambar);
                Log.d("gambarKategori", "onBindViewHolder: "+UrlImage+Image);


                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ProdukDetailActivity.class);
                        intent.putExtra("id_sub_kategori_produk",data.get(position).id_sub_kategori_produk);
                        intent.putExtra("id_produk",data.get(position).id_produk);
                        intent.putExtra("id_member",data.get(position).id_member);
                        intent.putExtra("kode",data.get(position).kode);
                        intent.putExtra("nama_produk",data.get(position).nama_produk);
                        intent.putExtra("harga_asli",data.get(position).harga_asli );
                        intent.putExtra("keterangan_produk", data.get(position).keterangan);
                        intent.putExtra("image_o",data.get(position).gambar);
                        intent.putExtra("berat",data.get(position).berat);
                        intent.putExtra("harga_jual",Integer.toString(harga_disc));
                        intent.putExtra("stok",data.get(position).total_stok);
                        intent.putExtra("diskon",data.get(position).diskon);
                        v.getContext().startActivity(intent);
                    }
                });


                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == data.size()  && isLoadingAdded) ? LOADING : ITEM;
    }

    //SHARE FACEBOOK
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

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(ResultsProduk mc) {
        data.add(mc);
        notifyItemInserted(data.size());
    }

    public void addAll(List<ResultsProduk> mcList) {
        for (ResultsProduk mc : mcList) {
            add(mc);
        }
    }

    public void remove(ResultsProduk city) {
        int position = data.indexOf(city);
        if (position > -1) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
       // add(new ResultsProduk());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        /*
           int position = data.size() - 1;
        ResultsProduk item = getItem(position);

        if (item != null) {
            data.remove(position);
            notifyItemRemoved(position);
        } */
    }

    public void stop(){
        isLoadingAdded = false;
    }

    public ResultsProduk getItem(int position) {
        return data.get(position);
    }

   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView gambar ;
        public TextView nama_produk,harga_jual,harga_asli,diskon,txtStok, txtKota, txtTerjual ;
        public CardView cardView;
        public LinearLayout shareWa, shareFb, shareSalin;

        public ViewHolder(View view) {
            super(view);
            gambar = itemView.findViewById(R.id.gambar_kategori);
            nama_produk = itemView.findViewById(R.id.nama_produk);
            harga_jual = itemView.findViewById(R.id.harga_jual);
            harga_asli = itemView.findViewById(R.id.harga_asli);
            diskon = itemView.findViewById(R.id.diskon);
            cardView = itemView.findViewById(R.id.cardview);
            shareWa = itemView.findViewById(R.id.share_whatsapp_home);
            shareSalin = itemView.findViewById(R.id.share_salin_home);
            shareFb = itemView.findViewById(R.id.share_fb_home);
            txtStok = itemView.findViewById(R.id.item_id_stock_produk);
            txtKota = itemView.findViewById(R.id.kota_produk);
            txtTerjual = itemView.findViewById(R.id.text_id_qty_produk_dibeli);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
