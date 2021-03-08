package www.starcom.com.jualanpraktis.feature.produk;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.denzcoskun.imageslider.ImageSlider;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.IDTansaksi.Shared;
import www.starcom.com.jualanpraktis.IDTansaksi.idtransaksi;
import www.starcom.com.jualanpraktis.Kategori.SliderUtils;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.VolleySingleton;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.MainActivity;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.Spinner.Variasi;
import www.starcom.com.jualanpraktis.adapter.ImageSliderAdapter;
import www.starcom.com.jualanpraktis.adapter.PenghasilanSelesaiAdapter;
import www.starcom.com.jualanpraktis.adapter.ProdukGambarAdapter;
import www.starcom.com.jualanpraktis.adapter.ProdukSejenisAdapter;
import www.starcom.com.jualanpraktis.adapter.VariasiAdapter;
import www.starcom.com.jualanpraktis.adapter.ViewPagerProdukAdapter;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.keranjang;
import www.starcom.com.jualanpraktis.login;
import www.starcom.com.jualanpraktis.model.ListProduk;

/**
 * Created by ADMIN on 08/02/2018.
 */

public class ProdukDetailActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();

    public static final String EXTRA_ID = "id_produk";
    public static final String EXTRA_NAMA = "nama_produk";
    public static final String EXTRA_HARGA = "harga_jual";
    public static final String EXTRA_BERAT = "berat";
    public static final String EXTRA_KET = "keterangan_produk";
    public static final String EXTRA_GAMBAR = "image_o";
    public static final String EXTRA_STOK = "stok";
    public static final String EXTRA_HARGA_ASLI = "harga_asli";
    public static final String UrlImage = "https://jualanpraktis.net/img/";

    idtransaksi idtransaksi ;
    loginuser user ;

    public static final int PERMISSION_WRITE = 0;


    List<SliderUtils> sliderimg ;

    ArrayList<HashMap<String, String>> dataVariasi = new ArrayList<>();

    private ImageView gambar,main_gambar_list ;
    private TextView nama,harga,ket,harga_asli,txt_stok,txt_diskon, berat ;
    CollapsingToolbarLayout collapsingToolbarLayout ;
    ElegantNumberButton numberButton;
    LinearLayout btn_keranjang ;
    ViewPager pagerSlide;
    ImageSlider imageSlider;
    ArrayList<Uri> listGambar = new ArrayList<Uri>();
    ArrayList<String> listGambar2 = new ArrayList<>();
    ArrayList<String> listGambar3 = new ArrayList<>();

    String extra_harga,stok,diskon;
    String urlbase_api = "https://jualanpraktis.net/android/";
    private RecyclerView recyclerViewImage,rv_produk_sejenis, rvSlider;
    private Spinner spn_variasi;
    List<Variasi> variasiList = new ArrayList<>();
    String id_variasi,nama_variasi,stok_variasi,id_sub_kategori_produk;
    LinearLayout gambar_main_ll, shareFb, shareWa, salinDeskripsi, linearFavorit, linearTambahKeranjang, shareIg, linearDifavoritkan;
    CardView cvVariasi;
    ShimmerFrameLayout shimmerProdukLainnya;

    RadioGroup radio_group_jenisbelanja;
    String valueJenisBelanja,kode,id_member;

    ShareDialog shareDialog;
    CallbackManager callbackManager;

    ArrayList<HashMap<String,String>> dataGambar = new ArrayList<>();
    ArrayList<HashMap<String,String>> dataGambar2 = new ArrayList<>();
    String[] dataArrayGambar;
    ViewPager carouselView;
    ViewPagerProdukAdapter pagerProduk;
    RecyclerView rvVariasi;
    public TextView txtStock, txtProdukTerjual, txtVariasiDetailProduk;
    int data_stock;
    ImageView imgFavorit, imgBack;
    String status_favorit;

    LinearLayout linearUnduhGambar, linearKeranjang, layout, linearShare;

    public String getVariasi;
    int aa;
    private int screenWidth, c;
    View myView;
    ArrayList<Uri> imageUriArray = new ArrayList<Uri>();
    String url;
    ImageView[] imageView;
    ImageView[] imageViewWa;
    ProgressDialog progressDialog;
    public Dialog dialogVariasi;
    public String get_nama_variasi, get_id_variasi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_detail);
        user = SharedPrefManager.getInstance(ProdukDetailActivity.this).getUser();
        AndroidNetworking.initialize(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sliderimg = new ArrayList<>();

        callbackManager = CallbackManager.Factory.create();

        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, callback);

        gambar = findViewById(R.id.img_item_view_pager);
        final Uri uri = Uri.parse(UrlImage+getIntent().getExtras().getString(EXTRA_GAMBAR));

        nama = findViewById(R.id.nama_produk);
        harga = findViewById(R.id.harga_produk);
        ket = findViewById(R.id.keterangan);
        berat = findViewById(R.id.berat_detail_produk);
        harga_asli = findViewById(R.id.harga_asli);
        txt_stok = findViewById(R.id.stok);
        txt_diskon = findViewById(R.id.txt_diskon);
        numberButton = findViewById(R.id.number_button);
        recyclerViewImage = findViewById(R.id.recyclerViewImage);
//        rv_produk_sejenis = findViewById(R.id.rv_produk_sejenis);
        shimmerProdukLainnya = findViewById(R.id.shimmerProdukLainnya);
        main_gambar_list = findViewById(R.id.main_gambar_list);
        gambar_main_ll = findViewById(R.id.gambar_main_ll);
        spn_variasi = findViewById(R.id.spn_variasi);
        cvVariasi = findViewById(R.id.cvVariasi);
         radio_group_jenisbelanja = findViewById(R.id.radio_group_jenisbelanja);
        btn_keranjang = findViewById(R.id.btn_keranjang);
        shareFb = findViewById(R.id.linear_facebook_detail_produk);
        shareWa = findViewById(R.id.linear_whatsapp_detail_produk);
        shareIg = findViewById(R.id.linear_instagram_detail_produk);
        salinDeskripsi = findViewById(R.id.linear_salin_detail_produk);
        rvSlider = findViewById(R.id.rv_slider);
        linearFavorit = findViewById(R.id.linear_favorit_detail_produk);
        linearTambahKeranjang = findViewById(R.id.linear_tambah_keranjang);
        carouselView = findViewById(R.id.carouselView);
        txtStock = findViewById(R.id.text_stock_detail_produk);
        imgFavorit = findViewById(R.id.img_favorit_produk_detail);
        linearDifavoritkan = findViewById(R.id.linear_difavoritkan_detail_produk);
        txtProdukTerjual = findViewById(R.id.text_id_detail_produk_dibeli);
        linearUnduhGambar = findViewById(R.id.linear_download_image);
        linearKeranjang = findViewById(R.id.linear_keranjang);
        imgBack = findViewById(R.id.imgBackDetailProduk);
        txtVariasiDetailProduk = findViewById(R.id.txt_variasi_detail_produk);
        linearShare = findViewById(R.id.linear_sharing_detail_produk);

        txtStock.setText(getIntent().getStringExtra("stok"));

        if (getIntent() != null && getIntent().getExtras() != null) {
//            if (getIntent().getExtras().containsKey(EXTRA_GAMBAR)) {
//
//
//                Glide.with(getApplicationContext())
//                        .load(UrlImage+getIntent().getExtras().getString(EXTRA_GAMBAR))
//                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false))
//                        .into(gambar);
//                Glide.with(getApplicationContext())
//                        .load(UrlImage+getIntent().getExtras().getString(EXTRA_GAMBAR))
//                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false))
//                        .into(main_gambar_list);
//                main_gambar_list.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Glide.with(getApplicationContext())
//                                .load(UrlImage+getIntent().getExtras().getString(EXTRA_GAMBAR))
//                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false))
//                                .into(gambar);
//                    }
//                });
//
////                Log.d(TAG, uri.toString());
//
//
//            }

            if (getIntent().getExtras().containsKey(EXTRA_BERAT)){
                berat.setText(getIntent().getExtras().getString(EXTRA_BERAT)+" gram");
            }

            if (getIntent().getExtras().containsKey(EXTRA_KET)) {
                ket.setText(Html.fromHtml(getIntent().getExtras().getString(EXTRA_KET)));
            }

            if (getIntent().getExtras().containsKey(EXTRA_NAMA)) {

                nama.setText(getIntent().getExtras().getString(EXTRA_NAMA));
            }

            if (getIntent().getExtras().containsKey("diskon")) {
                diskon = getIntent().getExtras().getString("diskon");
                if (diskon.equals("0")){
                    txt_diskon.setVisibility(View.GONE);
                    harga_asli.setVisibility(View.GONE);
                }
                txt_diskon.setText("Hemat "+diskon+"%");
            }
            if (getIntent().getExtras().containsKey(EXTRA_HARGA_ASLI)) {

                 harga_asli.setText(FormatText.rupiahFormat(Double.parseDouble(getIntent().getExtras().getString(EXTRA_HARGA_ASLI))));
                 harga_asli.setPaintFlags(harga_asli.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (getIntent().getExtras().containsKey(EXTRA_HARGA)) {
             //   extra_harga = getIntent().getExtras().getString(EXTRA_HARGA);
             //   String hargadiskon = getIntent().getExtras().getString(EXTRA_HARGA);
             //   int harga_disc = Integer.parseInt(hargadiskon)*Integer.parseInt(diskon)/100;
                extra_harga = getIntent().getExtras().getString(EXTRA_HARGA);
                harga.setText(FormatText.rupiahFormat(Double.parseDouble(extra_harga)));
            }

            if (getIntent().getExtras().containsKey(EXTRA_STOK)) {
                stok = getIntent().getExtras().getString(EXTRA_STOK);

                //if (stok==null||stok.equals("")||stok.equals("null")){
                  //  stok = "0";
                  //  getDataVariasi();
              //  }


            }

            txtProdukTerjual.setText(getIntent().getStringExtra("produkTerjual"+" Dibeli"));

            id_sub_kategori_produk = getIntent().getExtras().getString("id_sub_kategori_produk");
            kode = getIntent().getExtras().getString("kode");
            Log.d("kode", "onCreate: "+kode);
            id_member = getIntent().getExtras().getString("id_member");

        }

        if (Shared.getInstance(this).isIdIn()) {

        }else {
            idTransaksi();
        }


        linearFavorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(ProdukDetailActivity.this).isLoggedIn()){
                    tambahFavorit();
                }else{
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);
                    Toast.makeText(ProdukDetailActivity.this,"Harap masuk terlebih dahulu",Toast.LENGTH_SHORT).show();
                }
            }
        });

        linearDifavoritkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProdukDetailActivity.this, "Anda Sudah Mem-Favoritkan Produk Ini", Toast.LENGTH_SHORT).show();
            }
        });

        btn_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(ProdukDetailActivity.this).isLoggedIn()){

                    if (stok.equals("0")){
                        Toast.makeText(ProdukDetailActivity.this,"Stok barang kosong",Toast.LENGTH_SHORT).show();
                    }
                    else if (Integer.parseInt(stok)<Integer.parseInt(numberButton.getNumber())){
                        Toast.makeText(ProdukDetailActivity.this,"Pemesanan melebihi dari stok yang ada",Toast.LENGTH_SHORT).show();
                    }else if(getVariasi == null){
                        Toast.makeText(ProdukDetailActivity.this, "Silahkan Pilih Variasi Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                    }else if (txtVariasiDetailProduk.getText().toString().equals("")) {
                        Toast.makeText(ProdukDetailActivity.this, "Silahkan Pilih Variasi Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                    }else {
                        //validasiRadio();
                        valueJenisBelanja ="0";
                        if (valueJenisBelanja==null){
                            Toast.makeText(ProdukDetailActivity.this,"Pilih Jenis Belanja",Toast.LENGTH_SHORT).show();
                        }else {
                            pesan();
                        }

                    }

                }else{
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);
                    Toast.makeText(ProdukDetailActivity.this,"Harap masuk terlebih dahulu",Toast.LENGTH_SHORT).show();
                }

            }
        });

        linearTambahKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(ProdukDetailActivity.this).isLoggedIn()){
                    if (stok.equals("0")){
                        Toast.makeText(ProdukDetailActivity.this,"Stok barang kosong",Toast.LENGTH_SHORT).show();
                    }else if (Integer.parseInt(stok)<Integer.parseInt(numberButton.getNumber())){
                        Toast.makeText(ProdukDetailActivity.this,"Pemesanan melebihi dari stok yang ada",Toast.LENGTH_SHORT).show();
                    }else if(getVariasi == null && txtVariasiDetailProduk.getText().toString().isEmpty()){
                        Toast.makeText(ProdukDetailActivity.this, "Silahkan Pilih Variasi Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                    }else if (txtVariasiDetailProduk.getText().toString().equals("")) {
                        Toast.makeText(ProdukDetailActivity.this, "Silahkan Pilih Variasi Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                    } else {
                        //validasiRadio();
                        valueJenisBelanja ="0";
                        if (valueJenisBelanja==null){
                            Toast.makeText(ProdukDetailActivity.this,"Pilih Jenis Belanja",Toast.LENGTH_SHORT).show();
                        }else {
                            masukanKeranjang();
                        }

                    }

                }else{
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);
                    Toast.makeText(ProdukDetailActivity.this,"Harap masuk terlebih dahulu",Toast.LENGTH_SHORT).show();
                }
            }
        });
        collapsingToolbarLayout = findViewById(R.id.collap);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsingToolbarLayoutExpandedTextStyle);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        recyclerViewImage.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
      //  rv_produk_sejenis.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
      //  rv_produk_sejenis.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

//        rv_produk_sejenis.setLayoutManager(  new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.HORIZONTAL, false));
        spn_variasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Variasi variasi = variasiList.get(spn_variasi.getSelectedItemPosition());
                id_variasi = variasi.getId();
                nama_variasi = variasi.getVariasi();
                stok = variasi.getStok();

                txt_stok.setText(stok);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        shareFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                shareFacebook();
                shareImageFacebook();
            }
        });

        shareWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                shareWhatsapp();
                shareImageWhatsapp();
//                shareMultipleWhasapp();
//                for (int b = 0; b<listGambar2.size(); b++){
//                    shareMultipleWhasapp(listGambar2.get(b));
//
//                }
//                tampilDialogWa();
            }
        });

        shareIg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImageInstagram();
            }
        });

        linearShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        salinDeskripsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salinDeskripsi();
            }
        });

        linearUnduhGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilDialogDownload();
            }
        });

        linearKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ProdukDetailActivity.this, MainActivity.class);
                intent.putExtra("extraKeranjang", "produkDetail");
                startActivity(intent);
                finish();
            }
        });

        cvVariasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilDialogPilihVariasi();
            }
        });

        getDataGambar();
        getDataGambar2();
        getVariasi();
//        getDataProdukSejenis();

        getStatus();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    private void getDataGambar2() {

        String url = "https://jualanpraktis.net/android/tes1.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("kode", kode)
                .setTag(ProdukDetailActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        dataGambar2.clear();
                        try {
                            JSONArray array = response.getJSONArray("produk");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String, String> data = new HashMap<>();
                                data.put("gambar_produk", jsonObject.getString("image_o"));
                                JSONArray detail = jsonObject.getJSONArray("detail");
                                for (int j = 0; j < detail.length(); j++) {
                                    JSONObject jsonObject1 = detail.getJSONObject(j);
                                    data.put("gambar_detail", jsonObject1.getString("image"));
                                }

                                dataGambar2.add(data);
                                Log.d("dataGambar", "onResponse: " + data.toString());
                                Log.d("dataGambar2", "onResponse: "+dataGambar2);
                                Toast.makeText(ProdukDetailActivity.this, "Berhasil Load Data 2", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dataGambar2.clear();
                        Toast.makeText(ProdukDetailActivity.this, "Error get data 2", Toast.LENGTH_SHORT).show();

                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(ProdukDetailActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(ProdukDetailActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProdukDetailActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    private void share() {

        ImageView imgViewPagerShare = carouselView.findViewWithTag(carouselView.getCurrentItem());
        imgViewPagerShare.setDrawingCacheEnabled(true);

        BitmapDrawable bitmapDrawableShare = (BitmapDrawable) imgViewPagerShare.getDrawable();
        Bitmap bitmapShare = bitmapDrawableShare.getBitmap();
        String mediaPathShare = MediaStore.Images.Media.insertImage(getContentResolver(), bitmapShare, "shareProduct", null);


        String typeShare = "image/*";
        String filename = "/produk.jpg";

        createShareIntent(typeShare, mediaPathShare);

    }

    private void createShareIntent(String typeShare, String mediaPathShare) {

        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(typeShare);

        // Create the URI from the media
        Uri uri = Uri.parse(mediaPathShare);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));

    }

    private void tampilDialogPilihVariasi() {

        dialogVariasi = new Dialog(this);
        dialogVariasi.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogVariasi.setContentView(R.layout.dialog_pilih_variasi);
        dialogVariasi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        rvVariasi = dialogVariasi.findViewById(R.id.rv_variasi_detail_produk);

        rvVariasi.setHasFixedSize(true);
        rvVariasi.setLayoutManager(new LinearLayoutManager(ProdukDetailActivity.this));

        dialogVariasi.show();

        getDataVariasi();

    }

    private void tampilDialogWa() {

        imageView = new ImageView[listGambar2.size()];

        Dialog alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_download_wa);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        layout = alertDialog.findViewById(R.id.layout_download_wa);
        Button btnDownload = alertDialog.findViewById(R.id.download_gambar_wa);

        layout.removeAllViews();

        for (int i = 0; i < listGambar2.size(); i++) {;
            imageView[i] = new ImageView(this);
            imageView[i].setMinimumWidth(200);
            imageView[i].setMinimumHeight(200);
            imageView[i].setPadding(5, 0, 5, 0);
            Glide.with(this)
                    .load(listGambar2.get(i))
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView[i]);
            layout.addView(imageView[i]);
        }

        checkPermission();

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] filePath = new String[listGambar2.size()];
                for (int i = 0; i < listGambar2.size(); i++) {
                    try {
                        File mydir = new File(Environment.getExternalStorageDirectory(), "JualanPraktis/");
                        if (!mydir.exists()) {
                            mydir.mkdirs();
                        }

                        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri downloadUri = Uri.parse(listGambar2.get(i));
                        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmmss");
                        String date = dateFormat.format(new Date());

                        request.setAllowedNetworkTypes(
                                DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(false)
                                .setTitle("Downloading")
                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, date + ".jpg");

                        manager.enqueue(request);
                        filePath[i] = mydir.getAbsolutePath() + File.separator + date + ".jpg";
                    } catch (Exception ed) {
                        ed.printStackTrace();
                    }
                }

                ArrayList<Uri> arrayUri = new ArrayList<Uri>();
                File[] imageFiles = new File[filePath.length];

                for (int x = 0; x<filePath.length; x++){

                    arrayUri.add(FileProvider.getUriForFile(ProdukDetailActivity.this,
                            ProdukDetailActivity.this.getApplicationContext().getPackageName()+".provider",
                            new File(filePath[x])));
                }

                Log.d("arrayUri", "onClick: "+filePath);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("text/plain");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayUri);
                intent.setType("image/jpeg");
                startActivity(intent);
            }
        });

        alertDialog.show();

    }

    private void cobaShareWa(String s) {



    }

    private void tampilDialogDownload() {

        imageView = new ImageView[listGambar2.size()];

        Dialog alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_download_gambar);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        layout = alertDialog.findViewById(R.id.layout_download);
        Button btnDownload = alertDialog.findViewById(R.id.download_gambar);

        layout.removeAllViews();

        for (int i = 0; i < listGambar2.size(); i++) {;
            imageView[i] = new ImageView(this);
            imageView[i].setMinimumWidth(200);
            imageView[i].setMinimumHeight(200);
            imageView[i].setPadding(5, 0, 5, 0);
            Glide.with(this)
                    .load(listGambar2.get(i))
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView[i]);
            layout.addView(imageView[i]);
        }

        checkPermission();

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermission()) {
                    new Downloading().execute(listGambar2.toString());
                }
            }
        });

        alertDialog.show();



    }

    private void shareMultipleWhasapp(){

        for (int c = 0; c < listGambar.size(); c++){

            Picasso.get().load(listGambar.get(c)).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    i.setType("image/*");
                    i.setPackage("com.whatsapp");
                    i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, listGambar);
                    startActivity(i);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

        }



//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
//        intent.setType("image/jpeg");
//        intent.setPackage("com.whatsapp");
//        intent.putExtra(Intent.EXTRA_STREAM, url);
//        startActivity(intent);
//        Log.d("shareMultipleData", "data: "+imageUriArray.toString());
    }

    public Uri getLocalBitmapUri(Bitmap bitmap) {

        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;

    }

    public static void downloadFile(String uRl, Context context) {
        File myDir = new File(Environment.getExternalStorageDirectory(), "MyApp/");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE).setAllowedOverMetered(true)
                .setAllowedOverRoaming(true).setTitle("Myapp - " + "Downloading " + uRl).
                setVisibleInDownloadsUi(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uRl);

        mgr.enqueue(request);

    }

    public class Downloading extends AsyncTask<String, Integer, String[]> {

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProdukDetailActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String[] doInBackground(String... url) {
            String[] filePath = new String[listGambar2.size()];
            for (int i = 0; i < listGambar2.size(); i++) {
                try {
                    File mydir = new File(Environment.getExternalStorageDirectory(), "JualanPraktis/");
                    if (!mydir.exists()) {
                        mydir.mkdirs();
                    }

                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri downloadUri = Uri.parse(listGambar2.get(i));
                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmmss");
                    String date = dateFormat.format(new Date());

                    request.setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false)
                            .setTitle("Downloading")
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, date + ".jpg");

                    manager.enqueue(request);
                    filePath[i] = mydir.getAbsolutePath() + File.separator + date + ".jpg";
                } catch (Exception ed) {
                    ed.printStackTrace();
                }
            }
            return filePath;
        }

        @Override
        public void onPostExecute(String[] s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Images Saved", Toast.LENGTH_SHORT).show();

        }

    }

    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PERMISSION_WRITE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //do somethings
        }
    }


        private void getStatus() {

        String url = "https://jualanpraktis.net/android/detail_produk.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("customer", user.getId())
                .addBodyParameter("id_produk", getIntent().getStringExtra(EXTRA_ID))
                .setTag(ProdukDetailActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            status_favorit = response.getString("status");
                            Log.d("dataStatusFavorit", "onCreate: "+status_favorit);
                            Log.d("dataIdProduk", "onResponse: "+getIntent().getStringExtra(EXTRA_ID));

                            if (status_favorit.equals("1")){
                                linearFavorit.setVisibility(View.GONE);
                                linearDifavoritkan.setVisibility(View.VISIBLE);
                            }else{
                                linearDifavoritkan.setVisibility(View.GONE);
                                linearFavorit.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(ProdukDetailActivity.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(ProdukDetailActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ProdukDetailActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    private void shareImageInstagram() {

        ImageView imgViewPagerInstagram = carouselView.findViewWithTag(carouselView.getCurrentItem());
        imgViewPagerInstagram.setDrawingCacheEnabled(true);

        BitmapDrawable bitmapDrawableInstagram = (BitmapDrawable) imgViewPagerInstagram.getDrawable();
        Bitmap bitmapInstagram = bitmapDrawableInstagram.getBitmap();
        String mediaPathInstagram = MediaStore.Images.Media.insertImage(getContentResolver(), bitmapInstagram, "share",null);


        String typeInstagram = "image/*";
        String filename = "/produk.jpg";

        createInstagramIntent(typeInstagram, mediaPathInstagram);
    }

    private void createInstagramIntent(String typeInstagram, String mediaPathInstagram) {


        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(typeInstagram);
        share.setPackage("com.instagram.android");

        // Create the URI from the media
        Uri uri = Uri.parse(mediaPathInstagram);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    

    }

    private void shareImageFacebook() {

        ImageView imgViewPager = carouselView.findViewWithTag(carouselView.getCurrentItem());
        imgViewPager.setDrawingCacheEnabled(true);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgViewPager.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        String mediaPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "shareFacebook", null);


        String type = "image/*";
        String filename = "/produk.jpg";

        createFacebookIntent(type, mediaPath);
    }

    private void createFacebookIntent(String type, String mediaPath) {


        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);
        share.setPackage("com.facebook.katana");

        // Create the URI from the media
        Uri uri = Uri.parse(mediaPath);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));


    }

    public void tambahFavorit(){
        StringRequest request = new StringRequest(Request.Method.POST, "https://jualanpraktis.net/android/favorit.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Data Berhasil Di Kirim")){
                    /**   int total_berat = Integer.parseInt(numberButton.getNumber())*Integer.parseInt(getIntent().getExtras().getString(EXTRA_BERAT));
                     new Database(getBaseContext()).addToChart(new order(
                     "",
                     getIntent().getExtras().getString(EXTRA_ID),
                     getIntent().getExtras().getString(EXTRA_NAMA)+" - "+nama_variasi,
                     extra_harga,
                     numberButton.getNumber(),
                     String.valueOf(total_berat)
                     ));
                     Log.d(TAG,getIntent().getExtras().getString(EXTRA_BERAT)); **/

                    // finish();
                    // Intent intent = new Intent(ProdukDetailActivity.this, MainActivity.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // startActivity(intent);
                    linearFavorit.setVisibility(View.GONE);
                    linearDifavoritkan.setVisibility(View.VISIBLE);
                    Toast.makeText(ProdukDetailActivity.this, "Berhasil Ditambahkan Ke Favorit ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ProdukDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProdukDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();

                params.put("id_produk",getIntent().getExtras().getString(EXTRA_ID));
                params.put("customer",user.getId());
                Log.d("paramFavorit", "getParams: "+params);
                Log.d("paramFavorit", "getIduser: "+user.getId());

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void shareFacebook() {

        ImageView imgViewPager = carouselView.findViewWithTag(carouselView.getCurrentItem());
        imgViewPager.setDrawingCacheEnabled(true);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgViewPager.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();

        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .build();

        shareDialog.show(sharePhotoContent);

    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    private void salinDeskripsi() {

        Spanned detailProduk = Html.fromHtml(getIntent().getExtras().getString(EXTRA_KET));
        String nama = getIntent().getExtras().getString(EXTRA_NAMA);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(nama, detailProduk);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this,"Berhasil menyalin deskripsi",Toast.LENGTH_SHORT).show();

    }

    private void shareImageWhatsapp() {

        ImageView imgViewPager = carouselView.findViewWithTag(carouselView.getCurrentItem());
        imgViewPager.setDrawingCacheEnabled(true);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgViewPager.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        String bitmpath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "shareWhatsapp"+Calendar.getInstance().getTime(), null);


        Uri uri = Uri.parse(bitmpath);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "Bagikan Dengan"));

    }

    private void shareWhatsapp() {


        Spanned detailProduk = Html.fromHtml(getIntent().getExtras().getString(EXTRA_KET));


        boolean installed = appInstalledOrNot("com.whatsapp");

        if (installed){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?text="+detailProduk));
            startActivity(intent);
        }else{
            Toast.makeText(this, "Anda Belum Menginstall whatsapp", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean appInstalledOrNot(String url) {

        PackageManager packageManager = getPackageManager();
        boolean app_installed;
        try{
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }

        return app_installed;

    }

//    private void validasiRadio(){
//        int selectedId = radio_group_jenisbelanja.getCheckedRadioButtonId();
//        RadioButton radioButton = (RadioButton) findViewById(selectedId);
//
//        if (radioButton!=null) {
//            if (radioButton.getText().toString().equals("Belanja Biasa")) {
//                valueJenisBelanja = "0";
//            } else {
//                valueJenisBelanja = "1";
//            }
//        }
//    }

    private void getDataGambar(){

        dataGambar.clear();
        HashMap<String,String> item3 = new HashMap<>();
        item3.put("gambar", getIntent().getStringExtra("image_o"));
        dataGambar.add(item3);
        listGambar2.add(getIntent().getStringExtra("image_o"));

        AndroidNetworking.post(urlbase_api+"detail_gambar.php")
                .addBodyParameter("kode", kode)
                .setTag(ProdukDetailActivity.this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("data");

                            ArrayList<HashMap<String,String>> hashMaps = new ArrayList<>();
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                HashMap<String,String> item = new HashMap<>();
                                HashMap<String,String> item2 = new HashMap<>();
                                item.put("gambar",object.getString("image"));
                                listGambar.add(Uri.parse("https://jualanpraktis.net/img/"+object.getString("image")));
                                listGambar2.add(object.getString("image"));

                                hashMaps.add(item);
                                dataGambar.add(item);
                            }

                            Log.d("listGambar", "onResponse: "+listGambar2);
                            Log.d("dataLinkGambar", "onCreate: "+dataGambar);

                            //View Pager
                            ViewPagerProdukAdapter viewPagerAdapter = new ViewPagerProdukAdapter(ProdukDetailActivity.this, dataGambar);
                            carouselView.setAdapter(viewPagerAdapter);



                            //Adapter Image
                            ProdukGambarAdapter adapter = new ProdukGambarAdapter(ProdukDetailActivity.this,hashMaps,gambar);
                            recyclerViewImage.setAdapter(adapter);
                            ImageSliderAdapter adapter1 = new ImageSliderAdapter(hashMaps, ProdukDetailActivity.this);
                            rvSlider.setAdapter(adapter1);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ProdukDetailActivity.this,
                                    LinearLayoutManager.HORIZONTAL, false);
                            rvSlider.setLayoutManager(layoutManager);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void getDataVariasi(){
        AndroidNetworking.post(urlbase_api+"detail_variasi.php")
                .addBodyParameter("kode", kode)
                .setTag(ProdukDetailActivity.this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String id_variasi = "";
                        String stockTest = "";
                        dataVariasi.clear();
                        try {
                            JSONArray array = response.getJSONArray("data");

                            if (array.length()!=0){
                                for (int i = 0; i<array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);
                                    Variasi item = new Variasi();
                                    HashMap<String,String> data = new HashMap<>();
                                    data.put("id_variasi",object.getString("id"));
                                    data.put("variasi",object.getString("variasi"));
                                    data.put("stok",object.getString("stok"));
                                    item.setId(object.getString("id"));
                                    item.setVariasi(object.getString("variasi"));
                                    item.setStok(object.getString("stok"));
                                    id_variasi = object.getString("id");
                                    stockTest = object.getString("stok");
                                    data_stock = Integer.parseInt(object.getString("stok"));
                                    variasiList.add(item);
                                    dataVariasi.add(data);
                                }

                                //Adapter Recycler
                                VariasiAdapter variasiAdapter = new VariasiAdapter(ProdukDetailActivity.this, dataVariasi, ProdukDetailActivity.this);
                                rvVariasi.setAdapter(variasiAdapter);

                                txtVariasiDetailProduk.setText(get_nama_variasi);
                                txtVariasiDetailProduk.setVisibility(View.VISIBLE);

                                Log.d("dataVariasi", "id & stok: "+get_id_variasi+" dan "+stockTest);

                                if (dataVariasi.size()<=1){
                                    getVariasi = id_variasi;
                                    Log.d("id_variasi", "onResponse: "+getVariasi);
                                }


                                ArrayAdapter<Variasi> adapter = new ArrayAdapter<>(ProdukDetailActivity.this, R.layout.simple_spinner_text, variasiList);
                                adapter.setDropDownViewResource(R.layout.simple_spinner_text);
                                //  spinner.setPrompt("Jenis Perangkat : ");
                                spn_variasi.setAdapter(adapter);

                                if ( variasiList.get(0).getVariasi()==null || variasiList.get(0).getVariasi().equals("null")|| variasiList.get(0).getVariasi().equals("")){
                                    cvVariasi.setVisibility(View.GONE);
                                }else {
                                    cvVariasi.setVisibility(View.VISIBLE);
                                }

                                if (cvVariasi.getVisibility()==View.GONE){
                                    id_variasi = variasiList.get(0).getId();
                                    nama_variasi = "";
                                    stok = variasiList.get(0).getStok();

                                    txt_stok.setText(stok);
                                }
                            }else{
                                txt_stok.setText("0");
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void getVariasi(){

        AndroidNetworking.post(urlbase_api+"detail_variasi.php")
                .addBodyParameter("kode", kode)
                .setTag(ProdukDetailActivity.this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String id_variasi = "";
                        dataVariasi.clear();
                        try {
                            JSONArray array = response.getJSONArray("data");

                            if (array.length()!=0){
                                for (int i = 0; i<array.length(); i++){
                                    JSONObject object = array.getJSONObject(i);
                                    Variasi item = new Variasi();
                                    HashMap<String,String> data = new HashMap<>();
                                    data.put("id_variasi",object.getString("id"));
                                    data.put("variasi",object.getString("variasi"));
                                    item.setId(object.getString("id"));
                                    item.setVariasi(object.getString("variasi"));
                                    item.setStok(object.getString("stok"));
                                    id_variasi = object.getString("id");
                                    data_stock = Integer.parseInt(object.getString("stok"));
                                    variasiList.add(item);
                                    dataVariasi.add(data);
                                }

                                if (dataVariasi.size()<=1){
                                    getVariasi = id_variasi;
                                    Log.d("id_variasi", "onResponse: "+getVariasi);
                                }


                                ArrayAdapter<Variasi> adapter = new ArrayAdapter<>(ProdukDetailActivity.this, R.layout.simple_spinner_text, variasiList);
                                adapter.setDropDownViewResource(R.layout.simple_spinner_text);
                                //  spinner.setPrompt("Jenis Perangkat : ");
                                spn_variasi.setAdapter(adapter);

                                if ( variasiList.get(0).getVariasi()==null || variasiList.get(0).getVariasi().equals("null")|| variasiList.get(0).getVariasi().equals("")){
                                    cvVariasi.setVisibility(View.GONE);
                                }else {
                                    cvVariasi.setVisibility(View.VISIBLE);
                                }

                                if (cvVariasi.getVisibility()==View.GONE){
                                    id_variasi = variasiList.get(0).getId();
                                    nama_variasi = "";
                                    stok = variasiList.get(0).getStok();

                                    txt_stok.setText(stok);
                                }
                            }else{
                                txt_stok.setText("0");
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }


    private void getDataProdukSejenis(){
//        rv_produk_sejenis.setVisibility(View.GONE);
        shimmerProdukLainnya.setVisibility(View.VISIBLE);
        shimmerProdukLainnya.startShimmerAnimation();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        final String url = "https://jualanpraktis.net/android/produk_sejenis.php";
        AndroidNetworking.post(url)
                .addBodyParameter("id_sub_kategori_produk",id_sub_kategori_produk)
                .setTag(ProdukDetailActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsObject(ListProduk.ObjectSub.class, new ParsedRequestListener<ListProduk.ObjectSub>() {
                    @Override
                    public void onResponse(ListProduk.ObjectSub response) {
                        shimmerProdukLainnya.stopShimmerAnimation();
                        shimmerProdukLainnya.setVisibility(View.GONE);
//                        rv_produk_sejenis.setVisibility(View.VISIBLE);
                        ProdukSejenisAdapter adapter = new ProdukSejenisAdapter(getApplicationContext(), response.produksejenislist);
//                        rv_produk_sejenis.setAdapter(adapter);

                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerProdukLainnya.stopShimmerAnimation();
                        shimmerProdukLainnya.setVisibility(View.GONE);
//                        rv_produk_sejenis.setVisibility(View.VISIBLE);
                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void idTransaksi(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://jualanpraktis.net/android/id_transaksi.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    idtransaksi id = new idtransaksi(
                            response
                    );
                    Shared.getInstance(getApplicationContext()).idT(id);
                    Log.d(TAG,response);

                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "Periksa Jaringan Internet Anda", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void pesan(){
        StringRequest request = new StringRequest(Request.Method.POST, "https://jualanpraktis.net/android/pesan.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Data Berhasil Di Kirim")){
                 /**   int total_berat = Integer.parseInt(numberButton.getNumber())*Integer.parseInt(getIntent().getExtras().getString(EXTRA_BERAT));
                    new Database(getBaseContext()).addToChart(new order(
                            "",
                            getIntent().getExtras().getString(EXTRA_ID),
                            getIntent().getExtras().getString(EXTRA_NAMA)+" - "+nama_variasi,
                            extra_harga,
                            numberButton.getNumber(),
                            String.valueOf(total_berat)
                    ));
                    Log.d(TAG,getIntent().getExtras().getString(EXTRA_BERAT)); **/

                   // finish();
                   // Intent intent = new Intent(ProdukDetailActivity.this, MainActivity.class);
                   // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   // startActivity(intent);
                    ProdukDetailActivity.this.finish();
                    Intent intent = new Intent(ProdukDetailActivity.this, MainActivity.class);
                    intent.putExtra("extraKeranjang", "produkDetail");
                    startActivity(intent);
                    finish();
                    Toast.makeText(ProdukDetailActivity.this, "Berhasil Membuat Pesanan ", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("test Error", response);
                    Toast.makeText(ProdukDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProdukDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                idtransaksi = Shared.getInstance(getApplicationContext()).getIdT();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)+2);
                Date tomorrow = calendar.getTime();

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String tomorrowAsString = dateFormat.format(tomorrow);
                String tomorrowAsString2 = timeFormat.format(tomorrow);
                int total_berat = Integer.parseInt(numberButton.getNumber())*Integer.parseInt(getIntent().getExtras().getString(EXTRA_BERAT));
                Map<String,String> params = new HashMap<>();

                params.put("id_produk",getIntent().getExtras().getString(EXTRA_ID));
                params.put("customer",user.getId());
                params.put("id_member",id_member);
              //  params.put("tgl_transaksi",tomorrowAsString);
             //   params.put("id_transaksi",idtransaksi.getId_transaksi());
               // params.put("berat_barang",getIntent().getExtras().getString(EXTRA_BERAT));
                params.put("berat",String.valueOf(total_berat));
                params.put("jumlah", "1");
                params.put("harga_jual_item",extra_harga);
             //   params.put("harga_jual",extra_harga);
             //   params.put("time_limit",tomorrowAsString2);
                params.put("ket1",getVariasi);
            //  params.put("ket2",nama_variasi);
             //   params.put("bbl",valueJenisBelanja);

                Log.d("CobaKirim", params.toString());

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void masukanKeranjang(){

        StringRequest request = new StringRequest(Request.Method.POST, "https://jualanpraktis.net/android/pesan.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Data Berhasil Di Kirim")){
                    /**   int total_berat = Integer.parseInt(numberButton.getNumber())*Integer.parseInt(getIntent().getExtras().getString(EXTRA_BERAT));
                     new Database(getBaseContext()).addToChart(new order(
                     "",
                     getIntent().getExtras().getString(EXTRA_ID),
                     getIntent().getExtras().getString(EXTRA_NAMA)+" - "+nama_variasi,
                     extra_harga,
                     numberButton.getNumber(),
                     String.valueOf(total_berat)
                     ));
                     Log.d(TAG,getIntent().getExtras().getString(EXTRA_BERAT)); **/

                    // finish();
                    // Intent intent = new Intent(ProdukDetailActivity.this, MainActivity.class);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // startActivity(intent);
                    Log.d("checkVariasi", "onResponse: "+getVariasi);
                    Toast.makeText(ProdukDetailActivity.this, "Berhasil Dimasukkan Kedalam Keranjang ", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("test Error", response);
                    Toast.makeText(ProdukDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProdukDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                idtransaksi = Shared.getInstance(getApplicationContext()).getIdT();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)+2);
                Date tomorrow = calendar.getTime();

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String tomorrowAsString = dateFormat.format(tomorrow);
                String tomorrowAsString2 = timeFormat.format(tomorrow);
                int total_berat = Integer.parseInt(numberButton.getNumber())*Integer.parseInt(getIntent().getExtras().getString(EXTRA_BERAT));
                Map<String,String> params = new HashMap<>();

                params.put("id_produk",getIntent().getExtras().getString(EXTRA_ID));
                params.put("customer",user.getId());
                params.put("id_member",id_member);
                //  params.put("tgl_transaksi",tomorrowAsString);
                //   params.put("id_transaksi",idtransaksi.getId_transaksi());
                // params.put("berat_barang",getIntent().getExtras().getString(EXTRA_BERAT));
                params.put("berat",String.valueOf(total_berat));
                params.put("jumlah", "1");
                params.put("harga_jual_item",extra_harga);
                //   params.put("harga_jual",extra_harga);
                //   params.put("time_limit",tomorrowAsString2);
                params.put("ket1",getVariasi);
                //  params.put("ket2",nama_variasi);
                //   params.put("bbl",valueJenisBelanja);

                Log.d("CobaKirim", params.toString());

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);

    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            HashMap<String, String> item = new HashMap<>();
            item = dataGambar.get(position);

            final String url = "https://trading.my.id/img/" ;

            String link = item.get("gambar");

            Glide.with(ProdukDetailActivity.this)
                    .load(url+link)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        user = SharedPrefManager.getInstance(ProdukDetailActivity.this).getUser();
    }
}
