package www.starcom.com.jualanpraktis.feature.produk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.SearchResultsActivity;
import www.starcom.com.jualanpraktis.SubKategori.adaptersub;
import www.starcom.com.jualanpraktis.SubKategori.objectsub;
import www.starcom.com.jualanpraktis.adapter.ProdukAdapter;
import www.starcom.com.jualanpraktis.adapter.ProdukPaginationAdapter;
import www.starcom.com.jualanpraktis.databinding.ActivityListSubKategoriProdukBinding;
import www.starcom.com.jualanpraktis.model.ListProduk;
import www.starcom.com.jualanpraktis.model.ResultsProduk;
import www.starcom.com.jualanpraktis.utils.EndlessRecyclerOnScrollListener;

public class ListSubKategoriProdukActivity extends AppCompatActivity {
    Activity activity = ListSubKategoriProdukActivity.this;
    ActivityListSubKategoriProdukBinding binding;
    String title, id, status;

    //adapter
    ProdukAdapter adapter;
    List<ListProduk.ObjectSub.Results> allproduct;
    GridLayoutManager gridLayoutManager, gridLayoutManager2;
    ProdukPaginationAdapter produkPaginationAdapter;

    Spinner spinnerFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_list_sub_kategori_produk);
        AndroidNetworking.initialize(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("subkategori");
            id = bundle.getString("id");
            status = bundle.getString("status");

        }
        setSupportActionBar(binding.toolbar);
        binding.tvHint.setText("Cari dalam " + title);
        produkPaginationAdapter = new ProdukPaginationAdapter(ListSubKategoriProdukActivity.this);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        gridLayoutManager2 = new GridLayoutManager(getApplicationContext(), 2);
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        binding.recyclerViewTerlarisSub.setLayoutManager(gridLayoutManager2);

        spinnerFilter = findViewById(R.id.spinner_filter_produk_subkategori);

        ArrayAdapter adapterSpinner = ArrayAdapter.createFromResource(ListSubKategoriProdukActivity.this, R.array.filterProduk
                , R.layout.list_spinner_filter);

        adapterSpinner.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapterSpinner);
        clickedSpinner();


        binding.recyclerViewTerlarisSub.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(produkPaginationAdapter);

        binding.cari.setFocusable(false);
        binding.cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SearchResultsActivity.class);
                activity.startActivity(intent);
            }
        });
        binding.icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getData(1);
        binding.recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                // do something...
                getData(current_page);
            }
        });
    }

    private void clickedSpinner() {

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Produk Terbaru")){


                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.recyclerViewTerlarisSub.setVisibility(View.GONE);

                }else if (selectedItem.equals("Produk Terlaris")){
                    getProdukTerlaris();
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.recyclerViewTerlarisSub.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getProdukTerlaris() {

        binding.recyclerViewTerlarisSub.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmerAnimation();
        String url = "https://jualanpraktis.net/android/produk_terlaris.php";


        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("id_sub_kategori_produk", id)
                .setTag(ListSubKategoriProdukActivity.this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(objectsub.ObjectSub.class, new ParsedRequestListener<objectsub.ObjectSub>() {
                    @Override
                    public void onResponse(objectsub.ObjectSub response) {
                        // swipeRefreshLayout.setRefreshing(false);
                        binding.shimmer.stopShimmerAnimation();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.recyclerViewTerlarisSub.setVisibility(View.VISIBLE);

                        adaptersub adaptersub = new adaptersub(ListSubKategoriProdukActivity.this, response.sub1_kategori1);
                        binding.recyclerViewTerlarisSub.setAdapter(adaptersub);


                    }

                    @Override
                    public void onError(ANError anError) {
                        //  swipeRefreshLayout.setRefreshing(false);
                        binding.shimmer.stopShimmerAnimation();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.recyclerViewTerlarisSub.setVisibility(View.VISIBLE);

                        Toast.makeText(ListSubKategoriProdukActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();

                    }
                });

    }


    private void getData(final int currentPage){
        if (currentPage==1){

            binding.recyclerView.setVisibility(View.GONE);
            binding.shimmer.setVisibility(View.VISIBLE);
            binding.shimmer.startShimmerAnimation();
        }
        HashMap<String,String> param = new HashMap<>();
        String url = "";

            url = "https://jualanpraktis.net/android/produk_subkategori.php?page="+currentPage+"&id_sub_kategori_produk="+id;


        AndroidNetworking.get(url)
                .setTag(ListSubKategoriProdukActivity.this)
                .setPriority(Priority.LOW)
                // .setOkHttpClient(okHttpClient)
                .build()
                .getAsObject(ResultsProduk.class, new ParsedRequestListener<ResultsProduk>() {
                    @Override
                    public void onResponse(ResultsProduk response) {
                        // swipeRefreshLayout.setRefreshing(false);

                        binding.recyclerView.setVisibility(View.VISIBLE);
                        //  rowsArrayList = response.semuaproduk;
                        //  adapter = new ProdukAdapter(getApplicationContext(),response.semuaproduk, "list");
                        //   binding.recyclerView.setAdapter(adapter);

                        if (currentPage==1){
                            binding.shimmer.stopShimmerAnimation();
                            binding.shimmer.setVisibility(View.GONE);
                         //   binding.cvCari.setVisibility(View.GONE);
                            //  adapter.addAll(response.getListVideos());
                            // if (currentPage <= TOTAL_PAGES)
                            produkPaginationAdapter.addLoadingFooter();
                            // else
                            //   isLastPage = true;
                        }else {
                           // produkPaginationAdapter.removeLoadingFooter();
                           // isLoading = false;

                            //  if (currentPage != TOTAL_PAGES)
                            produkPaginationAdapter.addLoadingFooter();
                            //  else
                            //      isLastPage = true;

                        }
                        if (response.produkdata.size()!=0){
                            produkPaginationAdapter.addAll(response.produkdata);
                        }else{
                            produkPaginationAdapter.stop();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        binding.shimmer.stopShimmerAnimation();
                        binding.shimmer.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
