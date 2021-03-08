package www.starcom.com.jualanpraktis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.starcom.com.jualanpraktis.Database.Database;
import www.starcom.com.jualanpraktis.Keranjang.keranjangAdapter;
import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.SubKategori.order;
import www.starcom.com.jualanpraktis.adapter.CartAdapter;
import www.starcom.com.jualanpraktis.api.ConfigRetrofit;
import www.starcom.com.jualanpraktis.feature.akun.DetailRekeningBankActivity;
import www.starcom.com.jualanpraktis.feature.akun.NotifikasiActivity;
import www.starcom.com.jualanpraktis.feature.akun.ProdukFavoritActivity;
import www.starcom.com.jualanpraktis.feature.form_pemesanan.FormTransaksiActivity;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.feature.produk.ProdukDetailActivity;
import www.starcom.com.jualanpraktis.model_retrofit.data_check.ResponseDataCheck;

/**
 * Created by ADMIN on 05/02/2018.
 */

public class keranjang extends Fragment implements View.OnClickListener {

    keranjang activity = keranjang.this;

    private final String TAG = this.getClass().getName();

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    TextView total;
    public LinearLayout btnSubmit, btn_belanja_lagi, linear_button;
    loginuser user;

    List<order> list = new ArrayList<>();
    ArrayList<HashMap<String, String>> dataList = new ArrayList<>();
    ArrayList<HashMap<String, String>> cartList = new ArrayList<>();
    ArrayList<HashMap<String, String>> isBblList = new ArrayList<>();
    keranjangAdapter adapter;
    ProgressDialog progressDialog;

    CartAdapter adapterCart;

    ArrayList<String> listHargaItem2 = new ArrayList<>();


    //private order order ;

    String totalbelanja;

    int berat = 0;
    www.starcom.com.jualanpraktis.IDTansaksi.idtransaksi idtransaksi;

    String harga_dropshpper;
    //    ArrayList<String> harga_item2 = new ArrayList<>();
    private ShimmerFrameLayout shimmer;
    String harga_item2;
    int grandTotal = 0;
    Pref pref;

    public String status_user;

    TextView txtKosong;

    public int[] hargaItem;

    public ArrayList<String> dataHarga = new ArrayList<>();

    ImageView imgSearch, imgFav, imgNotif;

//    public ArrayList<String> dataHarga;

    public keranjang() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_keranjang, container, false);
//        View rootView2 = inflater.inflate(R.layout.item_cart_new, container, false);
        user = SharedPrefManager.getInstance(getActivity()).getUser();
        AndroidNetworking.initialize(getActivity().getApplicationContext());
        progressDialog = new ProgressDialog(getActivity());
        pref = new Pref(getContext());

        recyclerView = rootView.findViewById(R.id.list_belanja);
        shimmer = rootView.findViewById(R.id.shimmer);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        total = rootView.findViewById(R.id.total);
        btnSubmit = rootView.findViewById(R.id.submitOrder);
        imgSearch = rootView.findViewById(R.id.img_search_keranjang);
        imgFav = rootView.findViewById(R.id.img_favorit_keranjang);
        imgNotif = rootView.findViewById(R.id.img_notif_keranjang);
        txtKosong = rootView.findViewById(R.id.text_kosong_keranjang);
        linear_button = rootView.findViewById(R.id.linear_button_keranjang);

        btn_belanja_lagi = rootView.findViewById(R.id.btn_belanja_lagi);

        linear_button.setVisibility(View.GONE);

        dataHarga.clear();

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchResultsActivity.class));
            }
        });

        imgFav.setOnClickListener(new View.OnClickListener() {
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

        Bundle bundle = getArguments();
        if(bundle!=null) {
            String value = bundle.getString("key");
        }


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        //harga_dropshipper = rootView.findViewById(R.id.harga_dropshipper);

//        harga_dropshipper = (EditText) rootView.findViewById(R.id.harga_dropshipper);
//        coba = harga_dropshipper.getText().toString();

        btn_belanja_lagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().finish();
//                getActivity().overridePendingTransition(0, 0);
//                getActivity().startActivity(getActivity().getIntent().setFlags(getActivity().getIntent().FLAG_ACTIVITY_NO_ANIMATION));
//                getActivity().overridePendingTransition(0, 0);
                Fragment home= new home_dashboard();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, home, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        getDataUser();
//        getDataCheck();

        return rootView;
    }

    private void getDataUser() {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post("https://jualanpraktis.net/android/data-check.php")
                .addBodyParameter("id_member", user.getId())
                .setTag(getActivity())
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            status_user = response.getString("status");
                            Log.d("dataStatusUser", "onCreate: "+status_user);

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
                            Toast.makeText(getActivity(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                            Log.d("dataError", "onError: "+anError.getErrorDetail());
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")){
                                Toast.makeText(getActivity(), "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void getDataCheck(){


        String id_member = user.getId();

        ConfigRetrofit.service.dataCheck(id_member).enqueue(new Callback<ResponseDataCheck>() {
            @Override
            public void onResponse(Call<ResponseDataCheck> call, Response<ResponseDataCheck> response) {
                if (response.isSuccessful()){
                    String status = response.body().getStatus();
                    status_user = status;
                }else{
                    Toast.makeText(getActivity(), "Gagal Ambil Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataCheck> call, Throwable t) {
                Toast.makeText(getActivity(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            harga_item2 = intent.getStringExtra("hargaItem2");
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        dataHarga.clear();
        total.setText(FormatText.rupiahFormat(Double.parseDouble("0")));
        getCart();
        getDataUser();
        //  listItem(0,0);

        btnSubmit.setOnClickListener(this);
    }


    public void listItem(int Total, int Berat) {
        list = new Database(getContext()).getPesan();
        adapter = new keranjangAdapter(list, getActivity(), keranjang.this, null);
        recyclerView.setAdapter(adapter);

        for (order order : list)
            Total += (Integer.parseInt(order.getHarga())) * (Integer.parseInt(order.getJumlah()));
        NumberFormat nf = new DecimalFormat("#,###");
        //total.setText(nf.format(Total));
        total.setText(FormatText.rupiahFormat(Total));
        totalbelanja = String.valueOf(Total);
        Log.d(TAG, Integer.toString(Total));

        for (order order : list)
            Berat += (Integer.parseInt(order.getBerat()));
        berat = Berat;
        Log.d(TAG, Integer.toString(Berat));

        for (order item : list) {
            HashMap<String, String> data = new HashMap<>();
            data.put("id", item.getID());
            data.put("product", item.getNamaProduk());
            data.put("quantity", item.getJumlah());
            data.put("price", item.getHarga());
            dataList.add(data);
        }


    }

    public void onChangeData() {
        int Total = 0;
        int Berat = 0;
        for (HashMap<String, String> item : cartList) {
            Total += (Integer.parseInt(item.get("harga"))) * (Integer.parseInt(item.get("jumlah")));
            //  NumberFormat nf = new DecimalFormat("#,###");
            //total.setText(nf.format(Total));
            total.setText(FormatText.rupiahFormat(Total));
            totalbelanja = String.valueOf(Total);

            Berat += (Integer.parseInt(item.get("berat_item"))) * (Integer.parseInt(item.get("jumlah")));
            berat = Berat;
        }
    }

    @Override
    public void onClick(View v) {

        Log.d("statusUser", "onClick: "+status_user);

        if (status_user.equals("0")){
            Toast.makeText(getActivity(), "Anda Belum Melengkapi Data Diri", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), EditAkunActivity.class));
        }else {

            if (SharedPrefManager.getInstance(getActivity()).isLoggedIn() &&
                    !Objects.equals(total.getText().toString(), "Rp. 0")
                    && status_user.equals("1")) {
                if (Integer.parseInt(totalbelanja) < 5000) {
                    new AlertDialog.Builder(getActivity())
                            // .setTitle("Tidak bisa melanjutkan pemesanan")
                            .setMessage("Minimal pemesanan sejumlah Rp. 5000")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                } else {
                    if (pref.getLoginMethod().equals("coorperate")) {
                        if (Integer.parseInt(totalbelanja) > Integer.parseInt(pref.getLimitBelanja())) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Perhatian")
                                    .setMessage("Limit Belanja anda tidak cukup")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).show();
                        } else if (dataHarga.size() < 1) {
                            Toast.makeText(getActivity(), "Harga Jual Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();

                        } else if (status_user.equals("0")) {
                            Toast.makeText(getActivity(), "Anda Belum Melengkapi Data Diri, Silahkan lengkapi data diri anda", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), EditAkunActivity.class));
                        }else if (status_user.equals("2")) {
                            Toast.makeText(getActivity(), "Anda belum melengkapi data bank anda, silahkan lengkapi data bank anda", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), DetailRekeningBankActivity.class));
                        }else {
                            Log.d("statusUser", "onClick: " + status_user);
                            proccess();
                        }
                    } else if (dataHarga.size() < 1) {
                        Toast.makeText(getActivity(), "Harga Jual Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();

                    } else if (status_user.equals("0")) {
                        Toast.makeText(getActivity(), "Anda Belum Melengkapi Data Diri, Silahkan lengkapi data diri anda", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), EditAkunActivity.class));
                    }else if (status_user.equals("2")) {
                        Toast.makeText(getActivity(), "Anda belum melengkapi data bank anda, silahkan lengkapi data bank anda", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), DetailRekeningBankActivity.class));
                    } else {
                        Log.d("statusUser", "onClick: " + status_user);
                        proccess();
                    }
                }

            } else if (Objects.equals(total.getText().toString(), "Rp. 0")) {
                Toast.makeText(getActivity(), "Anda Belum Belanja", Toast.LENGTH_SHORT).show();
            } else if (status_user.equals("0")) {
                Toast.makeText(getActivity(), "Anda Belum Melengkapi Data Diri, Silahkan lengkapi data diri anda", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), EditAkunActivity.class));
            }else if (status_user.equals("2")) {
                Toast.makeText(getActivity(), "Anda belum melengkapi data bank anda, silahkan lengkapi data bank anda", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), DetailRekeningBankActivity.class));
            } else if (dataHarga.size() < 1) {
                Toast.makeText(getActivity(), "Harga Jual Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();

            } else {
                startActivity(new Intent(getActivity(), login.class));
                Toast.makeText(getActivity(), "Harap masuk terlebih dahulu", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void getHargaDropshipper(String test) {
        harga_dropshpper = test;
    }

    public void getArrayHarga(ArrayList<String> hargaItem) {
        listHargaItem2 = hargaItem;
    }


    public void getCart() {

        recyclerView.setVisibility(View.GONE);
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmerAnimation();
        HashMap<String, String> param = new HashMap<>();
        param.put("customer", user.getId());

        AndroidNetworking.post("https://jualanpraktis.net/android/cart.php")
                .addBodyParameter(param)
                .setTag(getActivity())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //  progressDialog.dismiss();
                        try {
                            cartList.clear();

                            shimmer.setVisibility(View.GONE);
                            shimmer.stopShimmerAnimation();
                            int jumlah, hasil, harga;
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                HashMap<String, String> item = new HashMap<>();
                                harga = Integer.parseInt(object.getString("harga_item"));
                                jumlah = Integer.parseInt(object.getString("jumlah"));
                                hasil = harga * jumlah;
                                item.put("nomor", object.getString("nomor"));
                                item.put("nama", object.getString("nama_produk"));
                                item.put("id_variasi", object.getString("ket1"));
                                item.put("variasi", object.getString("ket2"));
                                item.put("gambar", object.getString("image_o"));
                                item.put("harga", object.getString("harga_item"));
                                item.put("jumlah", object.getString("jumlah"));
                                item.put("berat", object.getString("berat"));
                                item.put("berat_item", object.getString("berat_item"));
                                item.put("stok", object.getString("stok"));
                                item.put("fee", object.getString("fee"));
                                item.put("id_vendor", object.getString("id_member"));
                                item.put("total_belanja", String.valueOf(hasil));
//                                item.put("bbl",object.getString("bbl"));
                                cartList.add(item);
                                Log.d("dataItem", "item: ");
                            }

                            recyclerView.setVisibility(View.VISIBLE);
                            linear_button.setVisibility(View.VISIBLE);
                            adapterCart = new CartAdapter(getActivity(), cartList, keranjang.this);
                            recyclerView.setAdapter(adapterCart);
                            recyclerView.setItemViewCacheSize(cartList.size());
                            Log.d("dataItem", "cartList: "+cartList);

                            if (cartList.size()<1){
                                txtKosong.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                linear_button.setVisibility(View.GONE);
                            }

//                            dataHarga = new ArrayList<>(cartList.size());

                            int Total = 0;
                            int Berat = 0;
                            for (HashMap<String, String> item : cartList) {
                                Total += (Integer.parseInt(item.get("harga"))) * (Integer.parseInt(item.get("jumlah")));
                                //  NumberFormat nf = new DecimalFormat("#,###");
                                //total.setText(nf.format(Total));
                                total.setText(FormatText.rupiahFormat(Total));
                                totalbelanja = String.valueOf(Total);

                                Berat += (Integer.parseInt(item.get("berat_item")));
                                berat = Berat;

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
//                        progressDialog.dismiss();
                        shimmer.setVisibility(View.GONE);
                        shimmer.stopShimmerAnimation();

                        if (anError.getErrorCode() != 0) {

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

    public void proccess() {
        progressDialog.setTitle("Melanjutkan Pemesanan");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String hargaDropshipper = "";
        final String[] hargaDrop = {""};
        //  idtransaksi = Shared.getInstance(getApplicationContext()).getIdT();


//        if (recyclerView.getChildCount() > 0) {
//
//            for (int b = 0; b < recyclerView.getChildCount(); b++) {
//
//                if (recyclerView.findViewHolderForLayoutPosition(b) instanceof CartAdapter.ViewHolder) {
//
//                    View holder = recyclerView.getChildAt(b);
//                    EditText edtHargaDropship = holder.findViewById(R.id.harga_dropshipper);
//
//                    getHargaDropshipper(edtHargaDropship.getText().toString());
//
//                }
//
//            }
//
//        }

//        Log.d(TAG, "proccess: "+harga_dropshpper);



        for (int a = 0; a < adapterCart.getItemCount(); a++) {

            Log.d(TAG, "proccess: "+dataHarga.get(a));

        }
//
//        for (String harga : dataHarga){
//            Log.d(TAG, "getHarga : "+harga);
//            Toast.makeText(getActivity(), harga, Toast.LENGTH_SHORT).show();
//        }

        ArrayList<String> listHargaJual = new ArrayList<>();

        HashMap<String, String> param = new HashMap<>();
        param.put("customer", user.getId());

        int i = 0;
        for (HashMap<String, String> data : cartList) {
            param.put("nomor[" + i + "]", data.get("nomor"));
            param.put("jumlah[" + i + "]", data.get("jumlah"));
            param.put("berat[" + i + "]", data.get("berat_item"));
            param.put("fee[" + i + "]", data.get("fee"));
            param.put("harga_dropshipper[" + i + "]", dataHarga.get(i));
            listHargaJual.add(dataHarga.get(i));
            param.put("harga_jual_item[" + i + "]", data.get("harga"));
            param.put("ket1[" + i + "]", data.get("id_variasi"));
            i++;
        }



        AndroidNetworking.post("https://jualanpraktis.net/android/pesan2.php")
                .addBodyParameter(param)
                .setTag(getActivity())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        String id_transaksi = "";



                        try {
                            id_transaksi = response.getString("id_transaksi");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Belanja", param.toString());

                        Intent intent = new Intent(getActivity(), FormTransaksiActivity.class);
                        intent.putExtra("total", totalbelanja);
                        intent.putExtra("berat", Integer.toString(berat));
                        intent.putExtra("id_transaksi", id_transaksi);
                        intent.putExtra("dataList", cartList);
                        intent.putExtra("hargaJualList", listHargaJual);
                        startActivity(intent);
                        Log.d(TAG, "Total = " + totalbelanja);
                        Log.d(TAG, "Berat = " + Integer.toString(berat));
                        Log.d("paramTransaksi", "onResponse: "+param.toString());
                        Log.d("checkListHargaJual", "proccess: "+cartList);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Gagal, silahkan coba beberapa saat lagi.", Toast.LENGTH_SHORT).show();
                        Log.d("Belanja", param.toString());
                    }
                });

    }
}
