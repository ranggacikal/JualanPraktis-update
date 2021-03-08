package www.starcom.com.jualanpraktis;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Kategori.SliderUtils;
import www.starcom.com.jualanpraktis.Kategori.ViewPagerAdapter;
import www.starcom.com.jualanpraktis.Kategori.adapterkategori;
import www.starcom.com.jualanpraktis.Kategori.objectkategori;
import www.starcom.com.jualanpraktis.SubKategori.adaptersub;
import www.starcom.com.jualanpraktis.SubKategori.objectsub;
import www.starcom.com.jualanpraktis.adapter.DashboardAdapter;
import www.starcom.com.jualanpraktis.adapter.KategoriAdapter;
import www.starcom.com.jualanpraktis.adapter.ProdukAdapter;
import www.starcom.com.jualanpraktis.feature.akun.NotifikasiActivity;
import www.starcom.com.jualanpraktis.feature.akun.ProdukFavoritActivity;
import www.starcom.com.jualanpraktis.feature.chat.ChatActivity;
import www.starcom.com.jualanpraktis.feature.produk.ListProdukActivity;
import www.starcom.com.jualanpraktis.feature.produk.ListProdukDiskonActivity;
import www.starcom.com.jualanpraktis.model.ListProduk;
import www.starcom.com.jualanpraktis.utils.EndlessRecyclerOnScrollListener;

import static com.android.volley.VolleyLog.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ADMIN on 06/02/2018.
 */

public class home_dashboard extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private objectkategori.ObjectKategori objectKategori;
    private adapterkategori adapterkategori ;
    private RecyclerView recyclerView1,recyclerView2,recyclerView3,recyclerView4,recyclerView5,
    recyclerView6,recyclerView7,recyclerView8,recyclerView9,recyclerView10
    ,recyclerView11,recyclerView12,recyclerView13,recyclerView14,recyclerView15;

    View rootView ;

    LinearLayout sliderDotspanel;
    private int dotscount ;
    private ImageView[] dots ;
    ViewPager viewPager;
    RequestQueue requestQueue ;
    List<SliderUtils> sliderimg ;
    String request_url = "https://jualanpraktis.net/android/banner.php";
    ViewPagerAdapter viewPagerAdapter ;

    private SwipeRefreshLayout swipeRefreshLayout;

    //produk
    private RecyclerView recyclerView,rv_produk1,rv_produk2,rv_produk3,rv_produk4, recyclerTerlaris, recyclerTerfavorit ;
    private objectsub.ObjectSub objectSub;
    private www.starcom.com.jualanpraktis.SubKategori.adaptersub adaptersub ;
    private www.starcom.com.jualanpraktis.SubKategori.adaptersubTerlaris adaptersubTerlaris;
    private www.starcom.com.jualanpraktis.adapter.ProdukAdapter produk1,produk2,produk3,produk4,allproduk ;
    GridLayoutManager gridLayoutManager;
    private ShimmerFrameLayout shimmer,shimmerAllProduk,shimmer_kategori;

    //iklan
    ImageView imgIklan1,imgIklan2,imgIklan3,imgIklan4, imgFavorite, imgNotif, imgChat;
    LinearLayout lllistproudk,ll_all_produk,ll_kategori;
    Button btn_see_all;

    //kategori
    private RecyclerView rv_kategori;
    ArrayList<HashMap<String,String>> kategoriList = new ArrayList<>();
    ArrayList<HashMap<String,String>> produkList2 = new ArrayList<>();
    ArrayList<List<objectsub.ObjectSub.Results>> produkList = new ArrayList<List<objectsub.ObjectSub.Results>>();

    //diskon
    private ImageView imgDiskon;
    //contact
    ImageView img_contact, img_flash_sale;
    Context context;
    Spinner spinnerFilter;
    String image_flash_sale, status_flash_sale;
    TextView txtKosong;
    NestedScrollView scroll_data;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    boolean isLoading = false;

    int page = 0;
    int limit = 100;
    ProgressBar progressBar;

    DashboardAdapter dashboardAdapter;
    NestedScrollView nestedDashboard;
    ProgressBar progressBarDashboard;

    int currentPage = 0;
    private Timer timer ;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;

    private EndlessRecyclerOnScrollListener scrollListener;

    //Cart
    private ImageView imgKeranjang;
    public home_dashboard() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dashboard,container,false);
        context=getActivity().getApplicationContext();
        AndroidNetworking.initialize(context);


        CardView cardView = rootView.findViewById(R.id.cari);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SearchResultsActivity.class);
                getActivity().startActivity(intent);
            }
        });

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);

        requestQueue = Volley.newRequestQueue(getActivity());
        sliderimg = new ArrayList<>();

        viewPager = rootView.findViewById(R.id.pagerslide);
        sliderDotspanel = (LinearLayout) rootView.findViewById(R.id.SliderDots);

        sendRequest();

        spinnerFilter = rootView.findViewById(R.id.spinner_filter_produk);
        imgChat = rootView.findViewById(R.id.img_chat_dashboard);
        imgNotif = rootView.findViewById(R.id.img_notif_dashboard);
        imgFavorite = rootView.findViewById(R.id.img_favorite_dashboard);
        lllistproudk = rootView.findViewById(R.id.lllistproudk);
        ll_all_produk = rootView.findViewById(R.id.ll_all_produk);
        ll_kategori = rootView.findViewById(R.id.ll_kategori);
        //image iklan
        imgIklan1 = rootView.findViewById(R.id.imgIklan1);
        imgIklan2 = rootView.findViewById(R.id.imgIklan2);
        imgIklan3 = rootView.findViewById(R.id.imgIklan3);
        imgIklan4 = rootView.findViewById(R.id.imgIklan4);
        //button
        btn_see_all = rootView.findViewById(R.id.btn_see_all);
        //list produk
        recyclerView = rootView.findViewById(R.id.rv_produk);
        rv_produk1 = rootView.findViewById(R.id.rv_produk1);
        rv_produk2 = rootView.findViewById(R.id.rv_produk2);
        rv_produk3 = rootView.findViewById(R.id.rv_produk3);
        rv_produk4 = rootView.findViewById(R.id.rv_produk4);
        rv_kategori = rootView.findViewById(R.id.rv_kategori);
        img_flash_sale = rootView.findViewById(R.id.img_flash_sale);
        recyclerView.setHasFixedSize(true);
        rv_produk1.setHasFixedSize(true);
        rv_produk2.setHasFixedSize(true);
        rv_produk3.setHasFixedSize(true);
        rv_produk4.setHasFixedSize(true);
        rv_kategori.setHasFixedSize(true);

       // gridLayoutManager = new GridLayoutManager(getContext(),3);
        int gridNumber = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        rv_produk1.setLayoutManager(new GridLayoutManager(getContext(),gridNumber));
        rv_produk2.setLayoutManager(new GridLayoutManager(getContext(),gridNumber));
        rv_produk3.setLayoutManager(new GridLayoutManager(getContext(),gridNumber));
        rv_produk4.setLayoutManager(new GridLayoutManager(getContext(),gridNumber));
        rv_kategori.setLayoutManager(  new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        shimmer = rootView.findViewById(R.id.shimmer);
        shimmerAllProduk = rootView.findViewById(R.id.shimmerAllProduk);
        shimmer_kategori = rootView.findViewById(R.id.shimmer_kategori);
        lllistproudk.setVisibility(View.GONE);
        txtKosong = rootView.findViewById(R.id.text_data_kosong_home);
        scroll_data = rootView.findViewById(R.id.scroll_view);
        nestedDashboard = rootView.findViewById(R.id.nested_home_dashboard);
        progressBarDashboard = rootView.findViewById(R.id.progressBar_dashboard);

        imgDiskon = rootView.findViewById(R.id.imgDiskon);
        imgDiskon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ListProdukDiskonActivity.class));
            }
        });

        imgKeranjang = rootView.findViewById(R.id.imgKeranjang);
        imgKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), katalog.class);
//                startActivity(intent);
//                startActivity(new Intent(getActivity(), katalog.class));
            }
        });

        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProdukFavoritActivity.class));
            }
        });

        imgNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotifikasiActivity.class));
            }
        });

        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChatActivity.class));
            }
        });

        img_flash_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FlashSaleActivity.class));
            }
        });

//        getSemuaProduk();
//        getAllProdukWithLimit(1);
//        initScrollListener();
        getAllProduk();

//        nestedDashboard.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged()
//            {
//                View view = (View)nestedDashboard.getChildAt(nestedDashboard.getChildCount() - 1);
//
//                int diff = (view.getBottom() - (nestedDashboard.getHeight() + nestedDashboard
//                        .getScrollY()));
//
//                if (diff == 0) {
//                    Log.d("test", "onScrollChanged: "+"pagination added");
//                    page = 2;
//                    getAllProdukWithLimit(page);
//                }
//            }
//        });

//        getSemuaProduk();
//
//        scrollListener = new EndlessRecyclerOnScrollListener(gridLayoutManager) {
//            @Override
//            public void onLoadMore(int current_page) {
//                page = 2;
//                getAllProdukWithLimit(page);
//            }
//        };
//
//        recyclerView.addOnScrollListener(scrollListener);



        recyclerTerlaris = rootView.findViewById(R.id.rv_produk_terlaris);
        recyclerTerfavorit = rootView.findViewById(R.id.rv_produk_terfavorit);

        ArrayAdapter adapterSpinner = ArrayAdapter.createFromResource(getActivity(), R.array.filterProduk
                , R.layout.list_spinner_filter);

        adapterSpinner.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapterSpinner);
        clickedSpinner();

        btn_see_all.setVisibility(View.GONE);
       // getProdykPerBaris();
        getKategori();
        loadImageFlashSale();
        getJumlahProduk();
        return rootView;
    }

    private void getSemuaProduk() {

        recyclerView.setVisibility(View.GONE);
        // shimmerAllProduk.setVisibility(View.VISIBLE);
        // shimmerAllProduk.startShimmerAnimation();
        String url = "https://jualanpraktis.net/android/produk.php";
        Log.d("checkUrl", "getAllProdukWithLimit: "+url);
        AndroidNetworking.get(url)
                .setTag(getActivity())
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);

                        try{
                            JSONArray array = response.getJSONArray("sub1_kategori1");
                            for (int i = 0;i<array.length();i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String, String> data = new HashMap<>();
                                data.put("id_produk", jsonObject.getString("id_produk"));
                                data.put("harga", jsonObject.getString("harga"));
                                data.put("kode", jsonObject.getString("kode"));
                                data.put("id_kategori_produk", jsonObject.getString("id_kategori_produk"));
                                data.put("id_sub_kategori_produk", jsonObject.getString("id_sub_kategori_produk"));
                                data.put("id_brand", jsonObject.getString("id_brand"));
                                data.put("id_jenis", jsonObject.getString("id_jenis"));
                                data.put("status_produk1", jsonObject.getString("status_produk1"));
                                data.put("status_produk2", jsonObject.getString("status_produk2"));
                                data.put("id_supplier", jsonObject.getString("id_supplier"));
                                data.put("nama_produk", jsonObject.getString("nama_produk"));
                                data.put("size", jsonObject.getString("size"));
                                data.put("warna", jsonObject.getString("warna"));
                                data.put("berat", jsonObject.getString("berat"));
                                data.put("keterangan_produk", jsonObject.getString("keterangan_produk"));
                                data.put("image_o", jsonObject.getString("image_o"));
                                data.put("image2_o", jsonObject.getString("image2_o"));
                                data.put("disc", jsonObject.getString("disc"));
                                data.put("harga_disc", jsonObject.getString("harga_disc"));
                                data.put("stok", jsonObject.getString("stok"));
                                data.put("status_disc", jsonObject.getString("status_disc"));
                                data.put("image_disc", jsonObject.getString("image_disc"));
                                data.put("created_by", jsonObject.getString("created_by"));
                                data.put("id_member", jsonObject.getString("id_member"));
                                data.put("sku", jsonObject.getString("sku"));
                                data.put("date", jsonObject.getString("date"));
                                data.put("start_disc", jsonObject.getString("start_disc"));
                                data.put("end_disc", jsonObject.getString("end_disc"));
                                data.put("total_stok", jsonObject.getString("total_stok"));
                                data.put("terjual", jsonObject.getString("terjual"));
                                data.put("city_name", jsonObject.getString("city_name"));

                                produkList2.add(data);
                            }

                            swipeRefreshLayout.setRefreshing(false);
                            shimmer.stopShimmerAnimation();
                            shimmer.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            dashboardAdapter = new DashboardAdapter(getActivity(), produkList2);
                            recyclerView.setAdapter(dashboardAdapter);




                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    private void getAllProdukWithLimit(int page) {

        recyclerView.setVisibility(View.GONE);
        // shimmerAllProduk.setVisibility(View.VISIBLE);
        // shimmerAllProduk.startShimmerAnimation();
        String url = "https://jualanpraktis.net/android/produk.php?page="+page;
        Log.d("checkUrl", "getAllProdukWithLimit: "+url);
        AndroidNetworking.get(url)
                .setTag(getActivity())
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);

                        try{
                            JSONArray array = response.getJSONArray("sub1_kategori1");
                            for (int i = 0;i<array.length();i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String, String> data = new HashMap<>();
                                data.put("id_produk", jsonObject.getString("id_produk"));
                                data.put("harga", jsonObject.getString("harga"));
                                data.put("kode", jsonObject.getString("kode"));
                                data.put("id_kategori_produk", jsonObject.getString("id_kategori_produk"));
                                data.put("id_sub_kategori_produk", jsonObject.getString("id_sub_kategori_produk"));
                                data.put("id_brand", jsonObject.getString("id_brand"));
                                data.put("id_jenis", jsonObject.getString("id_jenis"));
                                data.put("status_produk1", jsonObject.getString("status_produk1"));
                                data.put("status_produk2", jsonObject.getString("status_produk2"));
                                data.put("id_supplier", jsonObject.getString("id_supplier"));
                                data.put("nama_produk", jsonObject.getString("nama_produk"));
                                data.put("size", jsonObject.getString("size"));
                                data.put("warna", jsonObject.getString("warna"));
                                data.put("berat", jsonObject.getString("berat"));
                                data.put("keterangan_produk", jsonObject.getString("keterangan_produk"));
                                data.put("image_o", jsonObject.getString("image_o"));
                                data.put("image2_o", jsonObject.getString("image2_o"));
                                data.put("disc", jsonObject.getString("disc"));
                                data.put("harga_disc", jsonObject.getString("harga_disc"));
                                data.put("stok", jsonObject.getString("stok"));
                                data.put("status_disc", jsonObject.getString("status_disc"));
                                data.put("image_disc", jsonObject.getString("image_disc"));
                                data.put("created_by", jsonObject.getString("created_by"));
                                data.put("id_member", jsonObject.getString("id_member"));
                                data.put("sku", jsonObject.getString("sku"));
                                data.put("date", jsonObject.getString("date"));
                                data.put("start_disc", jsonObject.getString("start_disc"));
                                data.put("end_disc", jsonObject.getString("end_disc"));
                                data.put("total_stok", jsonObject.getString("total_stok"));
                                data.put("terjual", jsonObject.getString("terjual"));
                                data.put("city_name", jsonObject.getString("city_name"));

                                produkList2.add(data);
                            }

                            swipeRefreshLayout.setRefreshing(false);
                            shimmer.stopShimmerAnimation();
                            shimmer.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            dashboardAdapter = new DashboardAdapter(getActivity(), produkList2);
                            recyclerView.setAdapter(dashboardAdapter);
                            Log.d("jumlahData", "onResponse: "+produkList2.size());


                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }


    private void loadImageFlashSale() {

        String url = "https://jualanpraktis.net/android/fs-icon.php";
        String status;

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.get(url)
                .setTag(getActivity())
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                image_flash_sale = jsonObject.getString("file");
                                status_flash_sale = jsonObject.getString("status");
                            }

                            swipeRefreshLayout.setRefreshing(false);

                            String urlImage = "https://jualanpraktis.net/file-uploads/flashsale/"+image_flash_sale;

                            if (status_flash_sale!=null||!status_flash_sale.equals("")) {
                                if (status_flash_sale.equals("0")) {
                                    img_flash_sale.setVisibility(View.GONE);
                                }
                            }

                            Glide.with(getApplicationContext())
                                    .load(urlImage)
                                    .into(img_flash_sale);

                            Log.d("imageFlashSale", "onResponse: "+urlImage);

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
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getActivity(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(getActivity(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(getActivity(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    private void clickedSpinner() {

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Produk Terbaru")){

                    getAllProduk();

                }else if (selectedItem.equals("Produk Terlaris")){
                    getProdukTerlaris();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        if (timer !=null){
            timer.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       // loadData();
      //  btn_see_all.setVisibility(View.GONE);
      //  getProdykPerBaris();
        sendRequest();

        timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),4000,4000);

    }

    @Override
    public void onRefresh() {
       // loadData();
        //getAllProduk();
     //   getProdykPerBaris();
        getKategori();
    }

    public class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            if (getActivity()!=null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() == 0) {
                            viewPager.setCurrentItem(1);
                        } else if (viewPager.getCurrentItem() == 1) {
                            viewPager.setCurrentItem(2);
                        } else if (viewPager.getCurrentItem() == 2) {
                            viewPager.setCurrentItem(3);
                        }else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }
    }

    public void sendRequest() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, (JSONArray) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    SliderUtils sliderUtils = new SliderUtils();
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        final String UrlImage = "https://jualanpraktis.net/img/";
                        final String Image = jsonObject.getString("image");
                        final Uri uri = Uri.parse(UrlImage + Image);
                        sliderUtils.setSliderImageUrl(uri.toString());
                        Log.d(TAG, response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sliderimg.add(sliderUtils);
                }

                swipeRefreshLayout.setRefreshing(false);

                viewPagerAdapter = new ViewPagerAdapter(sliderimg, getActivity());
                viewPager.setAdapter(viewPagerAdapter);

//                dotscount = sliderimg.size();
//                dots = new ImageView[dotscount];
//
//                for(int i = 0; i < dotscount; i++){
//
//                    dots[i] = new ImageView(getContext());
//                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
//
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                    params.setMargins(8, 0, 8, 0);
//
//                    sliderDotspanel.addView(dots[i], params);
//
//                }
//
//                dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
//
//                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                    @Override
//                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                    }
//
//                    @Override
//                    public void onPageSelected(int position) {
//
//                        for(int i = 0; i< dotscount; i++){
//                            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
//                        }
//
//                        dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
//
//                    }
//
//                    @Override
//                    public void onPageScrollStateChanged(int state) {
//
//                    }
//                });
                /*
                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];

                for (int i = 0; i < dotscount; i++) {

                    dots[i] = new ImageView(get Activity());
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.notactive_dots));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    sliderDotspanel.addView(dots[i], params);
                }
                dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dots));
                */
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                swipeRefreshLayout.setRefreshing(false);

            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    //Menampilkan kategori
    private void getKategori(){

        ll_kategori.setVisibility(View.GONE);
        lllistproudk.setVisibility(View.GONE);
        imgDiskon.setVisibility(View.GONE);
        shimmer_kategori.setVisibility(View.VISIBLE);
        shimmer_kategori.startShimmerAnimation();
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmerAnimation();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();

        final String url = "https://jualanpraktis.net/android/kategori.php";
        AndroidNetworking.get(url)
                .setTag(getActivity())
                .setPriority(Priority.LOW)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            kategoriList.clear();
                            JSONArray array = response.getJSONArray("kategori");
                            for (int i = 0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                data.put("id",jsonObject.getString("id_kategori_produk"));
                                data.put("kategori",jsonObject.getString("kategori"));
                                data.put("gambar", jsonObject.getString("img"));
                                data.put("color", jsonObject.getString("hex_code"));
                                data.put("jumlah", jsonObject.getString("jumlah"));
                           //     data.put("created_at",jsonObject.getString("created_at"));
                                kategoriList.add(data);
                            }
                            shimmer_kategori.stopShimmerAnimation();
                            shimmer_kategori.setVisibility(View.GONE);
                            shimmer.stopShimmerAnimation();
                            shimmer.setVisibility(View.GONE);
                            ll_kategori.setVisibility(View.VISIBLE);

                            swipeRefreshLayout.setRefreshing(false);


                            KategoriAdapter adapter = new KategoriAdapter(getActivity(),kategoriList);
                            rv_kategori.setAdapter(adapter);

                                getGambarIklan();
//                                getAllProduk();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefreshLayout.setRefreshing(false);
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);
                        shimmer_kategori.stopShimmerAnimation();
                        shimmer_kategori.setVisibility(View.GONE);
                        ll_kategori.setVisibility(View.VISIBLE);
                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(getContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(getContext(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                                Log.e(getTag(),anError.getErrorDetail());
                            }else {
                                Toast.makeText(getContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    //Menampilkan gambar iklan
    private void getGambarIklan(){
        AndroidNetworking.get("https://jualanpraktis.net/android/iklan.php").setTag(getActivity())
                .setPriority(Priority.MEDIUM).build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("data");
                            JSONObject obj = array.getJSONObject(0);

                            String urlImage = "https://jualanpraktis.net/iklan/"+obj.getString("img");

                            Glide.with(context).load(urlImage).into(imgDiskon);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
    // Menampilkan Data Produk
    private void getProdykPerBaris(){


        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();

        final String url = "https://jualanpraktis.net/android/produk2.php";
        AndroidNetworking.get(url)
                .setTag(getActivity())
                .setPriority(Priority.LOW)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsObject(ListProduk.ObjectSub.class, new ParsedRequestListener<ListProduk.ObjectSub>() {
                    @Override
                    public void onResponse(final ListProduk.ObjectSub response) {
                        swipeRefreshLayout.setRefreshing(false);
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);
                        ll_all_produk.setVisibility(View.VISIBLE);
                        btn_see_all.setVisibility(View.VISIBLE);
                        lllistproudk.setVisibility(View.VISIBLE);

                        String urlImage = "https://jualanpraktis.net/iklan/";
                        //Picasso.get().load(urlImage+response.iklan1.get(0).img).into(imgIklan1);
                        //Picasso.get().load(urlImage+response.iklan2.get(0).img).into(imgIklan2);
                        //Picasso.get().load(urlImage+response.iklan3.get(0).img).into(imgIklan3);
                        //Picasso.get().load(urlImage+response.iklan4.get(0).img).into(imgIklan4);

                        klikIklan(imgIklan1,response.iklan1.get(0).id_member);
                        klikIklan(imgIklan2,response.iklan2.get(0).id_member);
                        klikIklan(imgIklan3,response.iklan3.get(0).id_member);
                        klikIklan(imgIklan4,response.iklan4.get(0).id_member);

                        final String statusList = "home";
                        produk1 = new ProdukAdapter(getActivity(), response.produk1,statusList);
                        produk2 = new ProdukAdapter(getActivity(), response.produk2,statusList);
                        produk3 = new ProdukAdapter(getActivity(), response.produk3,statusList);
                        produk4 = new ProdukAdapter(getActivity(), response.produk4,statusList);
                        rv_produk1.setAdapter(produk1);
                        rv_produk2.setAdapter(produk2);
                        rv_produk3.setAdapter(produk3);
                        rv_produk4.setAdapter(produk4);

                        btn_see_all.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                              //  allproduk = new ProdukAdapter(getContext(),response.allproduct,statusList);
                              //  recyclerView.setAdapter(allproduk);
                               // btn_see_all.setVisibility(View.GONE);
                               // recyclerView.setVisibility(View.VISIBLE);
                               // getAllProduk();

                                List<ListProduk.ObjectSub.Results> semuaproduk = response.allproduct;
                                Intent intent = new Intent(getActivity(),ListProdukActivity.class);
                                intent.putExtra("status","allproduct");
                                intent.putExtra("id","0");
                                intent.putExtra("kategori","Semua Produk");
                              //  intent.putExtra("dataList", (Serializable) semuaproduk);
                                startActivity(intent);
                            }
                        });


                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefreshLayout.setRefreshing(false);
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);
                        lllistproudk.setVisibility(View.VISIBLE);
                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(getContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(getContext(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }

                       // Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getJumlahProduk() {

        String url = "https://jualanpraktis.net/android/produk.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.get(url)
                .setTag(context)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {


                            JSONArray array = response.getJSONArray("sub1_kategori1");


                            String jumlah_data2 = String.valueOf(array.length());
                            int jumlah_data = Integer.parseInt(jumlah_data2);

                            Log.d("jumlahData", "onResponse: "+jumlah_data2);

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
                            Toast.makeText(getContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(getContext(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    private void getAllProduk(){

        recyclerView.setVisibility(View.GONE);
       // shimmerAllProduk.setVisibility(View.VISIBLE);
       // shimmerAllProduk.startShimmerAnimation();
        String url = "https://jualanpraktis.net/android/produk.php";
        AndroidNetworking.get(url)
                .setTag(getActivity())
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(objectsub.ObjectSub.class, new ParsedRequestListener<objectsub.ObjectSub>() {
                    @Override
                    public void onResponse(objectsub.ObjectSub response) {
                       // swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setRefreshing(false);
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        imgDiskon.setVisibility(View.GONE);
                        produkList.add(response.sub1_kategori1);
                        adaptersub = new adaptersub(getActivity(), response.sub1_kategori1);
                        recyclerView.setAdapter(adaptersub);


                        Log.d("dataObjectSub", "onResponse: "+response.sub1_kategori1);
                        Log.d("dataObjectSub", "sub: "+produkList);
//                        Log.d("dataObjectSub", "sub1: "+objectSub.sub1_kategori1);

//                        initScrollListener();

                        if (response.sub1_kategori1.isEmpty()){
                            txtKosong.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            shimmer.setVisibility(View.GONE);
                            shimmer.stopShimmerAnimation();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                      //  swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setRefreshing(false);
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void initScrollListener() {

        if (nestedDashboard != null) {

            nestedDashboard.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    String TAG = "nested_sync";
                    if (scrollY > oldScrollY) {
                        Log.i(TAG, "Scroll DOWN");
                    }
                    if (scrollY < oldScrollY) {
                        Log.i(TAG, "Scroll UP");
                    }

                    if (scrollY == 0) {
                        Log.i(TAG, "TOP SCROLL");
                    }

                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        Log.i("nestedScrollListener", "BOTTOM SCROLL");

                        progressBarDashboard.setVisibility(View.VISIBLE);
                        loadMore();

                    }
                }
            });
        }

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled( RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
//
//                if (!isLoading) {
//                    if (gridLayoutManager != null && gridLayoutManager.findLastCompletelyVisibleItemPosition() == produkList.size() - 1) {
//                        //bottom of list!
//                        loadMore();
//                        isLoading = true;
//                    }
//                }
//            }
//        });

    }

    private void loadMore() {

        getAllProdukWithLimit(2);
        dashboardAdapter.notifyDataSetChanged();
        Log.d("jumlahData", "loadMore: "+produkList2.size());

//        produkList2.add(null);
//        dashboardAdapter.notifyItemInserted(produkList2.size() - 1);
//
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getProdukTerlaris();
//                produkList2.remove(produkList2.size() - 1);
//                int scrollPosition = produkList2.size();
//                dashboardAdapter.notifyItemRemoved(scrollPosition);
//                int currentSize = scrollPosition;
//                int nextLimit = currentSize + 80;
////                int page = 0;
////
////                for (int i = 0; i<800; i ++){
////
////                    if (currentSize < currentSize * i){
////                        page = i;
////                    }
////
////                }
////
////                Log.d("page", "run: "+page);
////
//                while (currentSize - 1 < nextLimit) {
//                    getAllProdukWithLimit(2);
//                    currentSize++;
//                }
//
//                dashboardAdapter.notifyDataSetChanged();
//                isLoading = false;
//            }
//        }, 2000);

    }

    private void getProdukTerlaris(){

        recyclerView.setVisibility(View.GONE);
        // shimmerAllProduk.setVisibility(View.VISIBLE);
        // shimmerAllProduk.startShimmerAnimation();
        String url = "https://jualanpraktis.net/android/produk_terlaris.php";
        AndroidNetworking.get(url)
                .setTag(getActivity())
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(objectsub.ObjectSub.class, new ParsedRequestListener<objectsub.ObjectSub>() {
                    @Override
                    public void onResponse(objectsub.ObjectSub response) {
                        // swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setRefreshing(false);
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        imgDiskon.setVisibility(View.GONE);
                        adaptersub = new adaptersub(getActivity(), response.sub1_kategori1);
                        recyclerView.setAdapter(adaptersub);

                        if (response.sub1_kategori1.isEmpty()){
                            txtKosong.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            shimmer.stopShimmerAnimation();
                            shimmer.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        //  swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setRefreshing(false);
                        shimmer.stopShimmerAnimation();
                        shimmer.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void klikIklan(ImageView imageView, final String id_member){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListProdukActivity.class);
                intent.putExtra("status","produkIklan");
                intent.putExtra("id",id_member);
                intent.putExtra("kategori","Produk Iklan");
                startActivity(intent);
            }
        });
    }

    /**
    private void produkvolley(){
        recyclerView.setVisibility(View.GONE);
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmerAnimation();

        String url = "https://batammall.co.id/ANDROID/produk.php";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    swipeRefreshLayout.setRefreshing(false);
                    shimmer.stopShimmerAnimation();
                    shimmer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectSub = mGson.fromJson(response, objectsub.ObjectSub.class);
                    adaptersub = new adaptersub(getContext(), objectSub.sub1_kategori1);
                    recyclerView.setAdapter(adaptersub);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                shimmer.stopShimmerAnimation();
                shimmer.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data Kategori dari Database
    public void GetData(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView1.setAdapter(adapterkategori);
                    if (swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.d(TAG,error.toString());
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData2(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView2.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData3(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView3.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData4(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView4.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData5(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView5.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData6(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView6.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData7(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView7.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData8(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView8.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData9(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView9.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData10(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView10.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData11(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView11.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData12(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView12.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData13(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView13.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData14(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView14.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    // Menampilkan Data dari Database
    public void GetData15(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {;
            @Override
            public void onResponse(String response) {
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    objectKategori = mGson.fromJson(response, objectkategori.ObjectKategori.class);
                    adapterkategori = new adapterkategori(getActivity(), objectKategori.kategori);
                    recyclerView15.setAdapter(adapterkategori);
                    Log.d(TAG,response);
                }catch (Exception e){
                    Log.d(TAG, "onResponse: ",e);
                    Log.d(TAG,response);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    } **/

}
