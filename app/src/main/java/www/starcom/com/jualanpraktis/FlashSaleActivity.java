package www.starcom.com.jualanpraktis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;

import www.starcom.com.jualanpraktis.SubKategori.adaptersub;
import www.starcom.com.jualanpraktis.SubKategori.objectsub;
import www.starcom.com.jualanpraktis.adapter.FlashSaleAdapter;

public class FlashSaleActivity extends AppCompatActivity {

    ImageView imgBack;
    RecyclerView rv_flash_sale;
    ShimmerFrameLayout shimmerFlashSale;

    private www.starcom.com.jualanpraktis.SubKategori.adaptersub adaptersub ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_sale);

        imgBack = findViewById(R.id.imgBackFlashSale);
        rv_flash_sale = findViewById(R.id.rv_flash_sale);
        shimmerFlashSale = findViewById(R.id.shimmerFlashSale);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv_flash_sale.setHasFixedSize(true);
        rv_flash_sale.setLayoutManager(new LinearLayoutManager(FlashSaleActivity.this));
        getDataFlashSale();
    }

    private void getDataFlashSale() {

        rv_flash_sale.setVisibility(View.GONE);
        shimmerFlashSale.setVisibility(View.VISIBLE);
        shimmerFlashSale.startShimmerAnimation();
        String url = "https://jualanpraktis.net/android/flash_sale.php";
        AndroidNetworking.get(url)
                .setTag(FlashSaleActivity.this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(objectsub.ObjectSub.class, new ParsedRequestListener<objectsub.ObjectSub>() {
                    @Override
                    public void onResponse(objectsub.ObjectSub response) {
                        // swipeRefreshLayout.setRefreshing(false);
                        shimmerFlashSale.stopShimmerAnimation();
                        shimmerFlashSale.setVisibility(View.GONE);
                        rv_flash_sale.setVisibility(View.VISIBLE);
                        FlashSaleAdapter adapter = new FlashSaleAdapter(FlashSaleActivity.this, response.data);
                        rv_flash_sale.setAdapter(adapter);


                    }

                    @Override
                    public void onError(ANError anError) {
                        shimmerFlashSale.stopShimmerAnimation();
                        shimmerFlashSale.setVisibility(View.GONE);
                        rv_flash_sale.setVisibility(View.VISIBLE);

                        Toast.makeText(FlashSaleActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();

                    }
                });

    }
}