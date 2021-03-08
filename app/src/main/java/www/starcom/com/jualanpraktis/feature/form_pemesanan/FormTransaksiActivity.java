package www.starcom.com.jualanpraktis.feature.form_pemesanan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.SubKategori.order;
import www.starcom.com.jualanpraktis.adapter.CartAdapter;
import www.starcom.com.jualanpraktis.adapter.CheckoutAdapter;
import www.starcom.com.jualanpraktis.adapter.PilihPengirimanAdapter;
import www.starcom.com.jualanpraktis.adapter.ProdukRincianTransaksiAdapter;
import www.starcom.com.jualanpraktis.databinding.ActivityFormTransaksiBinding;
import www.starcom.com.jualanpraktis.feature.akun.RincianTransaksiActivity;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.interfaces.PilihPengirimanClickInterface;
import www.starcom.com.jualanpraktis.service.ServiceTask;

public class FormTransaksiActivity extends AppCompatActivity {
    Activity activity = FormTransaksiActivity.this;
    ActivityFormTransaksiBinding binding;

    String id_provinsi, id_kota, id_kecamatan;
    String nama_provinsi, nama_kota, nama_kecamatan;
    public static final String EXTRA_TOTAL = "total";
    public static final String EXTRA_BERAT = "berat";
    loginuser user;
    String def;
    String total_berat;
    String[] total_berat1;
    String onngkir_ro;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<>();
    ArrayList<HashMap<String, String>> listProduk = new ArrayList<>();
    //    String idVendor;
    ArrayList<String> idVendor1 = new ArrayList<>();
    ArrayList<String> beratItem = new ArrayList<>();
    ArrayList<String> jumlahItem = new ArrayList<>();
    ArrayList<String> hargaItem = new ArrayList<>();
    ArrayList<String> hargaItem2 = new ArrayList<>();
    ArrayList<String> grandTotal = new ArrayList<>();
    String idTransaksi;
    String total_belanja;
    String[] total_belanj1;
    String[] kode_ekspedisi;
    String[] kode_ekspedisi1 = {"jne"};
    String[] harga_layanan;
    String total_belanja_1 = "";
    String total_berat_1 = "";
    String[] harga_layanan1 = {"7000"};
    String total_bayar_item = "";
    String total_belanja_item = "";
    String total_berat2 = "";
    int harga_ongkir;
    String kurir;

    String totalBayar;

    int harga_ongkir2;
    int harga_item2;
    int total_harga2;

    PilihPengirimanAdapter adapterPengiriman;

    ArrayList<Integer> totalbayarArray = new ArrayList<Integer>();
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    List<order> list = new ArrayList<>();
    //    int[] grandTotal;
    int[] grandTotal1 = {107000};

//    int harga_ongkir = 0;

    String opsi_pembayaran;
    String coorperate = "0";

    String total_ongkir;

    Pref pref;
    boolean clicked = false;
    boolean clickedRodaEmpat = false;

    public ArrayList<Integer> dataOngkir = new ArrayList<>();
    public ArrayList<String> dataKurir = new ArrayList<>();

    public String isClicked;

    String validasiAdapter;

    String ongkir, kurirFix;

    String totalOngkir2, total_harga_produk, total_keuntungan;

    private static final int REQUEST_PENGIRIMAN = 4;

    LinearLayout linearRodaDua, linearRodaEmpat;
    TextView txtKendaraanRodaDua, txtHargaRodaDua, txtPerlokasiRodaDua;
    TextView txtKendaraanRodaEmpat, txtHargaRodaEmpat, txtPerlokasiRodaEmpat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_form_transaksi);
        user = SharedPrefManager.getInstance(this).getUser();
        AndroidNetworking.initialize(getApplicationContext());
        pref = new Pref(getApplicationContext());
        progressDialog = new ProgressDialog(FormTransaksiActivity.this);
        def = "Klik untuk memilih";

        total_berat = getIntent().getExtras().getString(EXTRA_BERAT);

        total_belanja = getIntent().getExtras().getString(EXTRA_TOTAL);

        linearRodaDua = findViewById(R.id.linear_ekspedisi_roda_dua);
        linearRodaEmpat = findViewById(R.id.linear_ekspedisi_roda_empat);
        txtKendaraanRodaDua = findViewById(R.id.text_kendaraan_roda_dua);
        txtHargaRodaDua = findViewById(R.id.text_harga_kendaraan_roda_dua);
        txtPerlokasiRodaDua = findViewById(R.id.text_perlokasi_kendaraan_roda_dua);
        txtKendaraanRodaEmpat = findViewById(R.id.text_kendaraan_roda_empat);
        txtHargaRodaEmpat = findViewById(R.id.text_harga_kendaraan_roda_empat);
        txtPerlokasiRodaEmpat = findViewById(R.id.text_perlokasi_kendaraan_roda_empat);

        total_berat1 = new String[]{total_berat};
        total_belanj1 = new String[]{total_belanja};
        idTransaksi = getIntent().getExtras().getString("id_transaksi");
        dataList.clear();
        dataList = (ArrayList<HashMap<String, String>>) getIntent().getExtras().getSerializable("dataList");

        hargaItem.clear();
        hargaItem = (ArrayList<String>) getIntent().getExtras().getSerializable("hargaJualList");

        Log.d("checkIntentList", "onCreate: " + hargaItem);


        Log.d("dataList", "onCreate: " + dataList);

        binding.imgBackFormTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.idTransaksi.setText(idTransaksi);
//         binding.lblNamaPenerima.setText(user.getNama());
//         binding.lblNoTelpon.setText(user.getNo_hp());
//         binding.lblAlamat.setText(user.getAlamat());
        binding.idTransaksi.setText(idTransaksi);


        cardClickAlamat(binding.cvProvinsi, 0, "province");
        cardClickAlamat(binding.cvKotakab, 1, "city");
        cardClickAlamat(binding.cvKecamatan, 2, "subdistrict");

//        cardClickEkspedisi(binding.cvEkspedisi, 0);
//        cardClickEkspedisi(binding.cvLayanan, 1);
//        cardClickEkspedisi(binding.cvJenisPembayaran, 2);

        binding.cvJenisPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogJenisPembayaran();
            }
        });


        buttonClick(binding.btnLanjut, 0);
        buttonClick(binding.btnKonfirmasi, 1);

        //list pesanan saya
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        Drawable dr = FormTransaksiActivity.this.getResources().getDrawable(R.drawable.background_pilih_pengiriman_red);

        Drawable dr2 = FormTransaksiActivity.this.getResources().getDrawable(R.drawable.background_pilih_pengiriman);

        Locale localID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localID);

        linearRodaDua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearRodaDua.setBackground(dr);
                linearRodaEmpat.setBackground(dr2);

                txtKendaraanRodaDua.setTextColor(ContextCompat.getColor(FormTransaksiActivity.this, R.color.white));
                txtHargaRodaDua.setTextColor(ContextCompat.getColor(FormTransaksiActivity.this, R.color.white));
                txtPerlokasiRodaDua.setTextColor(ContextCompat.getColor(FormTransaksiActivity.this, R.color.white));

                txtKendaraanRodaEmpat.setTextColor(ContextCompat.getColor(FormTransaksiActivity.this, R.color.colorPrimary));
                txtHargaRodaEmpat.setTextColor(ContextCompat.getColor(FormTransaksiActivity.this, R.color.colorPrimary));
                txtPerlokasiRodaEmpat.setTextColor(ContextCompat.getColor(FormTransaksiActivity.this, R.color.colorPrimary));

                ongkir = "10000";
                kurirFix = "Motor";

                int ongkirRodaDua = Integer.valueOf(ongkir);
                binding.lblTotalOngkir.setText("Rp" + NumberFormat.getInstance().format(ongkirRodaDua));

                int totalProduk = Integer.parseInt(total_harga_produk);
                int totalKeuntungan = Integer.parseInt(total_keuntungan);

                binding.lblMetodePengiriman.setText(kurirFix);


                int jumlahTotal = ongkirRodaDua + totalProduk + totalKeuntungan;
                binding.lblTotalBayarPenerima.setText("Rp" + NumberFormat.getInstance().format(jumlahTotal));
                binding.lblHargaTotal.setText("Rp" + NumberFormat.getInstance().format(jumlahTotal));


            }
        });

        linearRodaEmpat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearRodaEmpat.setBackground(dr);
                linearRodaDua.setBackground(dr2);

                //set text color kendaraan roda empat
                txtKendaraanRodaEmpat.setTextColor(ContextCompat.getColor(FormTransaksiActivity.this, R.color.white));
                txtHargaRodaEmpat.setTextColor(ContextCompat.getColor(FormTransaksiActivity.this, R.color.white));
                txtPerlokasiRodaEmpat.setTextColor(ContextCompat.getColor(FormTransaksiActivity.this, R.color.white));

                //set text color kendaraan roda dua
                txtKendaraanRodaDua.setTextColor(ContextCompat.getColor(FormTransaksiActivity.this, R.color.colorPrimary));
                txtHargaRodaDua.setTextColor(ContextCompat.getColor(FormTransaksiActivity.this, R.color.colorPrimary));
                txtPerlokasiRodaDua.setTextColor(ContextCompat.getColor(FormTransaksiActivity.this, R.color.colorPrimary));

                ongkir = "50000";
                kurirFix = "Mobil";

                int ongkirRodaEmpat = Integer.valueOf(ongkir);
                binding.lblTotalOngkir.setText("Rp" + NumberFormat.getInstance().format(ongkirRodaEmpat));

                int totalProduk = Integer.parseInt(total_harga_produk);
                int totalKeuntungan = Integer.parseInt(total_keuntungan);

                binding.lblMetodePengiriman.setText(kurirFix);


                int jumlahTotal = ongkirRodaEmpat + totalProduk + totalKeuntungan;
                binding.lblTotalBayarPenerima.setText("Rp" + NumberFormat.getInstance().format(jumlahTotal));
                binding.lblHargaTotal.setText("Rp" + NumberFormat.getInstance().format(jumlahTotal));

            }
        });


//        CartAdapter adapter = new CartAdapter(activity, dataList, null);
//        binding.recyclerView.setAdapter(adapter);


        for (HashMap<String, String> item : dataList) {

            idVendor1.add(item.get("id_vendor"));
            beratItem.add(item.get("berat"));
//            hargaItem.add(item.get("total_belanja"));

            jumlahItem.add(item.get("jumlah"));

        }

        Log.d("IdVendor", "id: " + idVendor1);
        Log.d("IdVendor", "harga: " + hargaItem);
        Log.d("IdVendor", "berat: " + beratItem);


        for (int total1 = 0; total1 < hargaItem.size(); total1++) {

            total_belanja_1 = hargaItem.get(total1);
        }

        for (int berat1 = 0; berat1 < beratItem.size(); berat1++) {

            total_berat_1 = beratItem.get(berat1);
        }

//        binding.lblNominalBelanja.setText(FormatText.rupiahFormat(Double.parseDouble(String.valueOf(total_belanj1[0]))));
//        binding.lblNominalTotal.setText(FormatText.rupiahFormat(Double.parseDouble(String.valueOf(total_belanj1[0]))));

        getDataProduk();

//        coorperate
//        if (pref.getLoginMethod().equals("coorperate")) {
//            kode_ekspedisi = new String[]{"kurirjualanpraktis"};
//            opsi_pembayaran = "corporate";
//            coorperate = "1";
//            id_provinsi = "17";
//            id_kota = "48";
//            id_kecamatan = "673";
//            nama_provinsi = "";
//            nama_kota = "";
//            nama_kecamatan = "";
//
//            binding.cvProvinsi.setVisibility(View.GONE);
//            binding.cvKecamatan.setVisibility(View.GONE);
//            binding.cvKotakab.setVisibility(View.GONE);
//
//            binding.cvEkspedisi.setClickable(false);
//            binding.lblEkspedisi.setText("Kurir Jualan Praktis");
//            binding.imgEkspedisi.setVisibility(View.GONE);
//            binding.cvJenisPembayaran.setVisibility(View.GONE);
//
//            int harga = 0;
//            if (Integer.parseInt(String.valueOf(total_belanja_1)) >= 200000) {
//                harga = 0;
//            } else {
//                if (Integer.parseInt(String.valueOf(total_belanja_1)) < 3001) {
//                    harga = 10000;
//                } else if (Integer.parseInt(String.valueOf(total_belanja_1)) < 6001) {
//                    harga = 20000;
//                } else if (Integer.parseInt(String.valueOf(total_belanja_1)) < 10001) {
//                    harga = 25000;
//                } else if (Integer.parseInt(String.valueOf(total_belanja_1)) < 20001) {
//                    harga = 40000;
//                } else if (Integer.parseInt(String.valueOf(total_belanja_1)) > 20001) {
//                    harga = 0;
//                }
//            }
//
//            String hargalayanan = String.valueOf(harga);
//            harga_layanan = new String[]{hargalayanan};
//            binding.lblNominalOngkir.setText(FormatText.rupiahFormat(Double.parseDouble(String.valueOf(harga_layanan))));
//            int totalseluruh = Integer.parseInt(String.valueOf(harga_layanan)) + Integer.parseInt(total_belanja_1);
//            grandTotal.add(String.valueOf(totalseluruh));
//
//            String totalString = "";
//            for (int aa = 0; aa < grandTotal.size(); aa++) {
//                totalString = grandTotal.get(aa);
//            }
//
//            double grandTotalDouble = Double.parseDouble(totalString);
//            binding.lblNominalTotal.setText(FormatText.rupiahFormat(grandTotalDouble));
//        } else {
//            if (user.getProvinsi() == null) {
//                cekAlamat(0);
//            } else if (user.getProvinsi().equals("")) {
//                cekAlamat(0);
//            } else if (user.getProvinsi().equals("-")) {
//                cekAlamat(0);
//            } else if (user.getProvinsi().equals("null")) {
//                cekAlamat(0);
//            } else {
//                cekAlamat(1);
//            }
//        }
    }

    private void getDataProduk() {

        String url = "https://jualanpraktis.net/android/list-produk.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("id_transaksi", binding.idTransaksi.getText().toString())
                .setTag(FormTransaksiActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        listProduk.clear();
                        try {

//                            total_harga_produk = response.getString("total_harga");
//                            total_keuntungan = response.getString("total_keuntungan");

                            String testTotal = response.getString("total_harga");
                            String testLKeuntungan = response.getString("total_untung");

                            JSONArray array = response.getJSONArray("produk");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                HashMap<String, String> data = new HashMap<>();
                                data.put("nama_produk", jsonObject.getString("nama_produk"));
                                data.put("gambar", jsonObject.getString("image_o"));
                                data.put("variasi", jsonObject.getString("ket2"));
                                data.put("harga_item", jsonObject.getString("harga_item"));
                                data.put("harga_jual", jsonObject.getString("harga_jual"));
                                data.put("jumlah", jsonObject.getString("jumlah"));
                                data.put("harga_item2", jsonObject.getString("harga_item2"));
                                data.put("harga_jual2", jsonObject.getString("harga_jual2"));
                                data.put("untung", jsonObject.getString("untung"));
                                data.put("subtotal_untung", jsonObject.getString("subtotal_untung"));

                                listProduk.add(data);

                            }

                            CheckoutAdapter adapter = new CheckoutAdapter(FormTransaksiActivity.this, listProduk);
                            binding.recyclerView.setAdapter(adapter);
                            int nominal_belanja = Integer.parseInt(testTotal);
                            int nominal_keuntungan = Integer.parseInt(testLKeuntungan);
                            total_harga_produk = testTotal;
                            total_keuntungan = testLKeuntungan;
                            binding.lblNominalBelanja.setText("Rp" + NumberFormat.getInstance().format(nominal_belanja));
                            binding.lblTotalKeuntungan.setText("Rp" + NumberFormat.getInstance().format(nominal_keuntungan));
                            int total_bayar_penerima = nominal_belanja + nominal_keuntungan;
                            binding.lblTotalBayarPenerima.setText("Rp" + NumberFormat.getInstance().format(total_bayar_penerima));
                            binding.lblHargaTotal.setText("Rp" + NumberFormat.getInstance().format(total_bayar_penerima));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        listProduk.clear();

                        if (anError.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail

                            // get parsed error object (If ApiError is your class)
                            Toast.makeText(FormTransaksiActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(FormTransaksiActivity.this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FormTransaksiActivity.this, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    public void getStatusButton(String test) {
        validasiAdapter = test;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startService(new Intent(activity, ServiceTask.class)
                .putExtra("proses", "form_transaksi_back")
                .putExtra("dataList", dataList)

        );
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (dataOngkir.size()<0){
//
//            binding.lblNominalOngkir.setText("0");
//
//        }else if (dataOngkir.size()>0){
//
//            int total_ongkir_label = 0;
//            for (int totalLabelOngkir : dataOngkir){
//                total_ongkir_label = total_ongkir_label + totalLabelOngkir;
//            }
//
//            binding.lblNominalOngkir.setText(total_ongkir_label);
//
//        }
//    }

    //on activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                id_provinsi = data.getStringExtra("id");
                nama_provinsi = data.getStringExtra("nama");

                binding.lblProvinsi.setText(nama_provinsi);
                binding.lblProvinsi.setTextColor(getResources().getColor(android.R.color.black));
            }
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                id_kota = data.getStringExtra("id");
                nama_kota = data.getStringExtra("nama");

                binding.lblKota.setText(nama_kota);
                binding.lblKota.setTextColor(getResources().getColor(android.R.color.black));

            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {

                id_kecamatan = data.getStringExtra("id");
                nama_kecamatan = data.getStringExtra("nama");

                binding.lblKecamatan.setText(nama_kecamatan);
                binding.lblKecamatan.setTextColor(getResources().getColor(android.R.color.black));
            }
        } else if (requestCode == REQUEST_PENGIRIMAN) {
            if (resultCode == RESULT_OK && data != null) {

                String totalOngkirIntent = data.getStringExtra("totalOngkir");
//                binding.lblNominalOngkir.setText(totalOngkirIntent);
//                String hargalayananIntent = data.getStringExtra("harga");
//                harga_layanan = new String[]{hargalayananIntent};
//                String layanan = data.getStringExtra("nama");
//                String pengiriman_layanan = data.getStringExtra("estimasi");
//
//                binding.lblHargaLayanaan.setVisibility(View.VISIBLE);
//                binding.lblPengirimanLayanaan.setVisibility(View.VISIBLE);
//
//                binding.lblLayanan.setText(layanan);
//                binding.lblHargaLayanaan.setText(FormatText.rupiahFormat(Double.parseDouble(harga_layanan[0])));
//                binding.lblPengirimanLayanaan.setText(pengiriman_layanan);
//
//                binding.lblNominalOngkir.setText(FormatText.rupiahFormat(Double.parseDouble(harga_layanan[0])));
//                int totalSeluruh = Integer.parseInt(harga_layanan[0]) + Integer.parseInt(total_belanja_1);
//                grandTotal.add(String.valueOf(totalSeluruh));
//
//                String totalSeluruhString = "";
//                for (int aa = 0; aa < grandTotal.size(); aa++) {
//                    totalSeluruhString = grandTotal.get(aa);
//                }
//
////                double totalSeluruhDouble = Double.parseDouble(totalSeluruhString);
//                binding.lblNominalTotal.setText(FormatText.rupiahFormat(Double.parseDouble(totalSeluruhString)));
//                binding.cvJenisPembayaran.setVisibility(View.VISIBLE);
            }
        }
    }

    //set on click
    private void cardClickAlamat(CardView cardView, final int reqCode, final String key) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reqCode == 0) {
                    Intent intent = new Intent(activity, PilihAlamatActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("code", reqCode);
                    startActivityForResult(intent, reqCode);
                } else if (reqCode == 1) {
                    if (id_provinsi != null) {
                        Intent intent = new Intent(activity, PilihAlamatActivity.class);
                        intent.putExtra("key", key);
                        intent.putExtra("code", reqCode);
                        intent.putExtra("id", id_provinsi);
                        startActivityForResult(intent, reqCode);
                    } else {
                        Toast.makeText(activity, "Pilih Provinsi", Toast.LENGTH_SHORT).show();
                    }
                } else if (reqCode == 2) {
                    if (id_kota != null) {
                        Intent intent = new Intent(activity, PilihAlamatActivity.class);
                        intent.putExtra("key", key);
                        intent.putExtra("code", reqCode);
                        intent.putExtra("id", id_kota);
                        startActivityForResult(intent, reqCode);
                    } else {
                        Toast.makeText(activity, "Pilih Kota/Kabupaten", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

//    private void cardClickEkspedisi(CardView cardView, final int reqCode) {
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 0 = dialog ekspedisi, selain itu ke activity pilih layanan
//                if (reqCode == 0) {
//                    binding.cvLayanan.setVisibility(View.GONE);
//                    binding.cvJenisPembayaran.setVisibility(View.GONE);
//                    dialogEkspedisi();
//                } else if (reqCode == 1) {
//
//                    HashMap<String, String> param = new HashMap<>();
//
//                    param.put("origin", "48");
//                    param.put("originType", "city");
//                    param.put("destination", id_kecamatan);
//                    param.put("destinationType", "subdistrict");
//                    param.put("weight", total_berat1[0]);
//                    param.put("courier", kode_ekspedisi[0]);
//                    Intent intent = new Intent(activity, PilihLayananEkspedisiActivity.class);
//                    intent.putExtra("param", param);
//                    startActivityForResult(intent, 4);
//                } else if (reqCode == 2) {
//                    dialogJenisPembayaran();
//                }
//            }
//        });
//    }

    private void buttonClick(Button button, final int reqCode) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reqCode == 0) {

                } else if (reqCode == 1) {
//                    validasiAlamat();
                    validasiEkspedisi();

                }
            }
        });
    }

    private void totalBayar() {

        int harga_ongkir = 0;
        int harga_belanja = 0;
        int totalSeluruh = 0;
        ArrayList<Integer> total = new ArrayList<>();

        for (int sum : dataOngkir) {

            harga_ongkir = harga_ongkir + sum;

        }

        harga_belanja = Integer.parseInt(getIntent().getStringExtra(EXTRA_TOTAL));

        totalSeluruh = harga_ongkir + harga_belanja;

        Log.d("indexTotal", "totalBayar: " + totalSeluruh);


    }

    //settings
//    private void cekAlamat(int status) {
//        if (status == 0) {
//            id_provinsi = null;
//            id_kota = null;
//            id_kecamatan = null;
//            nama_provinsi = null;
//            nama_kota = null;
//            nama_kecamatan = null;
//            binding.lblProvinsi.setText(def);
//            binding.lblKota.setText(def);
//            binding.lblKecamatan.setText(def);
//        } else {
//            id_provinsi = user.getProvinsi();
//            id_kota = user.getKota();
//            id_kecamatan = user.getKecamatan();
////            nama_provinsi = user.getNamaProvinsi();
////            nama_kota = user.getNamaKota();
////            nama_kecamatan = user.getNamaKecamatan();
//            binding.lblProvinsi.setText(nama_provinsi);
//            binding.lblKota.setText(nama_kota);
//            binding.lblKecamatan.setText(nama_kecamatan);
//            binding.lblProvinsi.setTextColor(getResources().getColor(android.R.color.black));
//            binding.lblKota.setTextColor(getResources().getColor(android.R.color.black));
//            binding.lblKecamatan.setTextColor(getResources().getColor(android.R.color.black));
//        }
//    }

//    private void dialogEkspedisi() {
//        kode_ekspedisi = null;
//        harga_layanan = null;
//        opsi_pembayaran = null;
//        AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
//        View layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_ekspedisi, null);
//
//        final ConstraintLayout jne = layoutView.findViewById(R.id.jne);
//        final ConstraintLayout jnt = layoutView.findViewById(R.id.jnt);
//        final ConstraintLayout tiki = layoutView.findViewById(R.id.tiki);
//        final ConstraintLayout sicepat = layoutView.findViewById(R.id.sicepat);
//        final ConstraintLayout antarbatam = layoutView.findViewById(R.id.kurirjualanpraktis);
//
//        if (id_kota.equals("48")) {
//            antarbatam.setVisibility(View.VISIBLE);
//        } else {
//            antarbatam.setVisibility(View.GONE);
//        }
//
//        pickEkspedisi(jne, 0, "jne");
//        pickEkspedisi(jnt, 1, "jnt");
//        pickEkspedisi(tiki, 2, "tiki");
//        pickEkspedisi(sicepat, 3, "sicepat");
//        pickEkspedisi(antarbatam, 4, "kurirjualanpraktis");
//
//        dialogBuilder.setView(layoutView);
//        alertDialog = dialogBuilder.create();
//        alertDialog.setCancelable(false);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.show();
//
//    }
//
//    private void pickEkspedisi(ConstraintLayout constraintLayout, final int reqCode, final String kode) {
//
//        constraintLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 0 = jne,1=jnt,2=tiki,3=sicepat
//                kode_ekspedisi = new String[]{kode};
//                if (!kode.equals("kurirjualanpraktis")) {
//                    opsi_pembayaran = "transfer";
//                    binding.imgEkspedisi.setVisibility(View.VISIBLE);
//                    binding.cvLayanan.setVisibility(View.VISIBLE);
//                    binding.lblLayanan.setText("Pilih Layanan");
//                    binding.lblHargaLayanaan.setVisibility(View.GONE);
//                    binding.lblPengirimanLayanaan.setVisibility(View.GONE);
//                    binding.lblNominalOngkir.setText("0");
//                    binding.lblNominalBelanja.setText(FormatText.rupiahFormat(Double.parseDouble(String.valueOf(total_belanja_1))));
//                    binding.lblNominalTotal.setText(FormatText.rupiahFormat(Double.parseDouble(String.valueOf(total_belanja_1))));
//
//
//                    if (reqCode == 0) {
//                        binding.lblEkspedisi.setText("JNE");
//                        binding.imgEkspedisi.setImageDrawable(getResources().getDrawable(R.drawable.jne));
//                    } else if (reqCode == 1) {
//                        binding.lblEkspedisi.setText("JNT");
//                        binding.imgEkspedisi.setImageDrawable(getResources().getDrawable(R.drawable.jnt));
//                    } else if (reqCode == 2) {
//                        binding.lblEkspedisi.setText("TIKI");
//                        binding.imgEkspedisi.setImageDrawable(getResources().getDrawable(R.drawable.tiki));
//                    } else if (reqCode == 3) {
//                        binding.lblEkspedisi.setText("SI CEPAT");
//                        binding.imgEkspedisi.setImageDrawable(getResources().getDrawable(R.drawable.sicepat));
//
//                    }
//
//                } else {
//                    if (reqCode == 4) {
//                        binding.lblEkspedisi.setText("KURIR JUALAN PRAKTIS");
//                        binding.imgEkspedisi.setVisibility(View.GONE);
//                        binding.cvJenisPembayaran.setVisibility(View.VISIBLE);
//
//                        int harga = 0;
//                        if (Integer.parseInt(String.valueOf(total_belanja_1)) >= 200000) {
//                            harga = 0;
//                        } else {
//                            if (Integer.parseInt(String.valueOf(total_berat_1)) < 3001) {
//                                harga = 10000;
//                            } else if (Integer.parseInt(String.valueOf(total_berat_1)) < 6001) {
//                                harga = 20000;
//                            } else if (Integer.parseInt(String.valueOf(total_berat_1)) < 10001) {
//                                harga = 25000;
//                            } else if (Integer.parseInt(String.valueOf(total_berat_1)) < 20001) {
//                                harga = 40000;
//                            } else if (Integer.parseInt(String.valueOf(total_berat_1)) > 20001) {
//                                harga = 0;
//                            }
//                        }
//
//
//                        String hargalayanan = String.valueOf(harga);
//                        harga_layanan = new String[]{hargalayanan};
//                        binding.lblNominalOngkir.setText(FormatText.rupiahFormat(Double.parseDouble(harga_layanan[0])));
//                        int totalSeluruhPick = Integer.parseInt(harga_layanan[0]) + Integer.parseInt(total_belanja_1);
//                        grandTotal.add(String.valueOf(totalSeluruhPick));
//
//                        String totalPick = "";
//                        for (int totalpick = 0; totalpick < grandTotal.size(); totalpick++) {
//                            totalPick = grandTotal.get(totalpick);
//                        }
//                        binding.lblNominalTotal.setText(FormatText.rupiahFormat(Double.parseDouble(totalPick)));
//                    }
//                }
//
//
//                alertDialog.dismiss();
//            }
//        });
//
//    }

    private void dialogJenisPembayaran() {

        opsi_pembayaran = null;
        binding.lblJenisPembayaran.setText("Pilih Jenis Pembayaran");
        AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        View layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_jenis_pembayaran, null);

        final ConstraintLayout pembayaran_cod = layoutView.findViewById(R.id.pembayaran_cod);
        final ConstraintLayout pembayaran_lainnya = layoutView.findViewById(R.id.pembayaran_lainnya);
        final ConstraintLayout pembayaran_banksumut = layoutView.findViewById(R.id.pembayaran_banksumut);

//        if (kode_ekspedisi[0].equals("kurirjualanpraktis")) {
//            pembayaran_cod.setVisibility(View.VISIBLE);
//            pembayaran_banksumut.setVisibility(View.VISIBLE);
//        } else {
//            pembayaran_cod.setVisibility(View.GONE);
//            pembayaran_lainnya.setVisibility(View.GONE);
//            pembayaran_banksumut.setVisibility(View.VISIBLE);
//        }

        pembayaran_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opsi_pembayaran = "cod";
                binding.lblJenisPembayaran.setText("COD/Bayar Ditempat");
                alertDialog.dismiss();

            }
        });

        pembayaran_lainnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opsi_pembayaran = "transfer";
                binding.lblJenisPembayaran.setText("Transfer Bank / QR Code");
                alertDialog.dismiss();

            }
        });
        pembayaran_banksumut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opsi_pembayaran = "transfer manual";
                binding.lblJenisPembayaran.setText("Transfer Bank");
                alertDialog.dismiss();
            }
        });

        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    //validasi
    private void validasiAlamat() {
        if (binding.lblNamaPenerima.getText().toString().isEmpty()) {
            Toast.makeText(activity, "Nama Penerima harus diisi", Toast.LENGTH_SHORT).show();
        } else if (binding.lblNoTelpon.getText().toString().isEmpty()) {
            Toast.makeText(activity, "Nomor HP tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (binding.lblAlamat.getText().toString().isEmpty()) {
            Toast.makeText(activity, "Alamat Lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (nama_provinsi == null) {
            Toast.makeText(activity, "Pilih Provinsi", Toast.LENGTH_SHORT).show();
        } else if (nama_kota == null) {
            Toast.makeText(activity, "Pilih Kota/Kabupaten", Toast.LENGTH_SHORT).show();
        } else if (nama_kecamatan == null) {
            Toast.makeText(activity, "Pilih Kecamatan", Toast.LENGTH_SHORT).show();
        }
//        else {
//            binding.llEkspedisi.setVisibility(View.VISIBLE);
//            binding.btnKonfirmasi.setVisibility(View.VISIBLE);
//            binding.llAlamat.setVisibility(View.GONE);
        //   binding.llEkspedisi.animate().alpha(1.0f).setDuration(300);
        //   binding.llEkspedisi.animate().alpha(0f).setDuration(300);
//            binding.btnLanjut.setVisibility(View.GONE);


//        }
    }

    private void validasiEkspedisi() {

        if (binding.lblNamaPenerima.getText().toString().isEmpty()) {
            Toast.makeText(activity, "Nama Penerima harus diisi", Toast.LENGTH_SHORT).show();
        } else if (binding.lblNoTelpon.getText().toString().isEmpty()) {
            Toast.makeText(activity, "Nomor HP tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (binding.lblAlamat.getText().toString().isEmpty()) {
            Toast.makeText(activity, "Alamat Lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (nama_provinsi == null) {
            Toast.makeText(activity, "Pilih Provinsi", Toast.LENGTH_SHORT).show();
        } else if (nama_kota == null) {
            Toast.makeText(activity, "Pilih Kota/Kabupaten", Toast.LENGTH_SHORT).show();
        } else if (nama_kecamatan == null) {
            Toast.makeText(activity, "Pilih Kecamatan", Toast.LENGTH_SHORT).show();
        }else if (binding.lblKodePos.getText().toString().isEmpty()) {
            Toast.makeText(activity, "Kode Pos tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }else if (kurirFix == null) {
            Toast.makeText(activity, "Silahkan Pilih Kurir", Toast.LENGTH_SHORT).show();
        } else if (ongkir == null) {
            Toast.makeText(activity, "Silahkan Pilih Kurir", Toast.LENGTH_SHORT).show();
        } else if (opsi_pembayaran == null) {
            Toast.makeText(activity, "Silahkan pilih Metode Pembayaran", Toast.LENGTH_SHORT).show();
        }else {
            new AlertDialog.Builder(activity)
                    .setTitle("Konfirmasi Pemesanan")
                    .setMessage("Anda yakin ingin melakukan pemesanan ini?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(activity,"Transaksi Biasa",Toast.LENGTH_LONG).show();
                            processTransaksi();

                        }
                    })
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_menu_info_details)
                    .show();
        }

//        if (kode_ekspedisi != null) {
//
//            if (!kode_ekspedisi.equals("kurirjualanpraktis")) {
//
//                if (kode_ekspedisi == null) {
//                    Toast.makeText(activity, "Pilih Ekspedisi", Toast.LENGTH_SHORT).show();
//                } else if (harga_layanan == null) {
//                    Toast.makeText(activity, "Pilih Layanan", Toast.LENGTH_SHORT).show();
//                } else {
//                    new AlertDialog.Builder(activity)
//                            .setTitle("Konfirmasi Pemesanan")
//                            .setMessage("Anda yakin ingin melakukan pemesanan ini?")
//
//                            // Specifying a listener allows you to take an action before dismissing the dialog.
//                            // The dialog is automatically dismissed when a dialog button is clicked.
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //Toast.makeText(activity,"Transaksi Biasa",Toast.LENGTH_LONG).show();
//                                    processTransaksi();
//
//                                }
//                            })
//                            // A null listener allows the button to dismiss the dialog and take no further action.
//                            .setNegativeButton(android.R.string.no, null)
//                            .setIcon(android.R.drawable.ic_menu_info_details)
//                            .show();
//                }
//
//            } else {
//
//                if (kode_ekspedisi == null) {
//                    Toast.makeText(activity, "Pilih Ekspedisi", Toast.LENGTH_SHORT).show();
//                } else if (harga_layanan == null) {
//                    Toast.makeText(activity, "Pilih Layanan", Toast.LENGTH_SHORT).show();
//                } else if (opsi_pembayaran == null) {
//                    Toast.makeText(activity, "Pilih Jenis Pembayaran", Toast.LENGTH_SHORT).show();
//                } else {
//                    new AlertDialog.Builder(activity)
//                            .setTitle("Konfirmasi Pemesanan")
//                            .setMessage("Anda yakin ingin melakukan pemesanan ini?")
//
//                            // Specifying a listener allows you to take an action before dismissing the dialog.
//                            // The dialog is automatically dismissed when a dialog button is clicked.
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    processTransaksi();
//
//
//                                }
//                            })
//                            // A null listener allows the button to dismiss the dialog and take no further action.
//                            .setNegativeButton(android.R.string.no, null)
//                            .setIcon(android.R.drawable.ic_menu_info_details)
//                            .show();
//                }
//            }
//
//        } else {
//            Toast.makeText(activity, "Pilih Ekspedisi", Toast.LENGTH_SHORT).show();
//        }


    }

    //back


    //kirim data ke server
    private void processTransaksi() {
        Object[] id_vendor = idVendor1.toArray();
        String host = "https://jualanpraktis.net/android/transaksi.php";
        Date date;
        Calendar calendar = Calendar.getInstance();
        String id_member = "";
        String total_bayar3 = "";

        int jumlahOngkir = 0;

        for (int a = 0; a < hargaItem.size(); a++) {

            harga_item2 = Integer.parseInt(hargaItem.get(a));

            total_harga2 = Integer.parseInt(ongkir) + harga_item2;
            totalbayarArray.add(total_harga2);

        }

//        for (int sumOngkir : dataOngkir) {
//            jumlahOngkir = jumlahOngkir + sumOngkir;
//            total_ongkir = String.valueOf(jumlahOngkir);
//        }

        Log.d("FormTransaksiTotal", "TotalOngkir: " + total_ongkir);

        int total_seluruh = Integer.parseInt(total_belanja) + Integer.parseInt(ongkir);
        total_bayar_item = String.valueOf(total_seluruh);

//        for (int a = 0; a < idVendor1.size(); a++) {
//            id_member = idVendor1.get(a);

//            total_berat2 = beratItem.get(a);
//            total_belanja_item = hargaItem.get(a);
//            total_bayar_item = grandTotal.get(a);

        if (opsi_pembayaran.equals("cod")) {
            calendar.add(Calendar.DAY_OF_YEAR, 2);
            date = calendar.getTime();

        } else {
            //get 2 jam setelahnya
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 2);
            date = calendar.getTime();
        }

        // Calendar calendar = Calendar.getInstance();

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String currentDateTime = currentDate + " " + currentTime;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String tomorrowAsString = dateFormat.format(date);
        String tomorrowAsString2 = timeFormat.format(date);


        progressDialog.setTitle("Memproses Pemesanan");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Map<String, String> params = new HashMap<String, String>();
        params.put("id_customer", user.getId());
        params.put("id_transaksi", idTransaksi);
//            params.put("tgl_transaksi", currentDateTime);
        params.put("nama_penerima", binding.lblNamaPenerima.getText().toString());
        params.put("alamat", binding.lblAlamat.getText().toString());
        params.put("city_destination", id_kota);
        params.put("subdistrict_destination", id_kecamatan);
        params.put("province_destination", id_provinsi);
        params.put("no_hp", binding.lblNoTelpon.getText().toString());
        params.put("kode_pos", binding.lblKodePos.getText().toString());
        params.put("catatan", binding.lblCatatan.getText().toString());

//        for (int a = 0; a < idVendor1.size(); a++) {

//            id_member = idVendor1.get(a);
//
//            params.put("id_vendor["+a+"]", id_member);
//
//            total_belanja_item = hargaItem.get(a);
//            params.put("total_belanja["+a+"]", total_belanja_item);
//            total_berat2 = beratItem.get(a);
//            params.put("weight["+a+"]", total_berat2);

        params.put("courier", kurirFix);

//        }

        params.put("ongkos_kirim", String.valueOf(ongkir));

//            total_bayar3 = String.valueOf(totalbayarArray.get(a));
//            params.put("total_bayar[]", total_bayar3);
        params.put("opsi", opsi_pembayaran);


        Log.d("HasilKirim", params.toString());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post(host)
                .addBodyParameter(params)
                .setTag(activity)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        // if (response.equals("Data Berhasil Di Kirim")){
                        if (opsi_pembayaran.equals("cod")) {

                            Log.d("FormTransaksi", "COD");
                            String alamat = binding.lblAlamat.getText().toString() + ", Kecamatan " + nama_kecamatan
                                    + ", Kota/Kabupaten " + nama_kota + ", Provinsi " + nama_provinsi;

                            Intent intent = new Intent(activity, ResultCodActivity.class);
                            intent.putExtra("id_transaksi", idTransaksi);
                            intent.putExtra("nama", binding.lblNamaPenerima.getText().toString());
                            intent.putExtra("alamat", alamat);
                            intent.putExtra("nominal_belanja", total_belanja);
                            intent.putExtra("ongkos_kirim", ongkir);
                            intent.putExtra("berat", total_berat2);
                            intent.putExtra("total", total_bayar_item);
                            intent.putExtra("no_hp", binding.lblNoTelpon.getText().toString());
                            intent.putExtra("dataList", dataList);
                            startActivity(intent);
                            finish();

                            //       Toast.makeText(getApplicationContext(), "Berhasil Melakukan Pemesanan", Toast.LENGTH_SHORT).show();
                        } else if (opsi_pembayaran.equals("transfer manual")) {
                            Log.d("FormTransaksi", "Transfer manual");
                            startActivity(new Intent(activity, ResultTransferActivity.class)
                                    .putExtra("id_transaksi", idTransaksi)
                                    .putExtra("status", "result")
                                    .putExtra("total", String.valueOf(total_bayar_item)));
                        } else {

//                                Log.d("FormTransaksi", "Limit");
//                                long limitTersisa = Integer.parseInt(pref.getLimitBelanja()) - grandTotal.length;
//                                pref.setLimitBelanja(String.valueOf(limitTersisa));
//                                startActivity(new Intent(activity, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                                finish();
                        }


                        try {
                            Toast.makeText(getApplicationContext(), response.getString("response"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.d("FormTransaksi", String.valueOf(anError.getErrorCode()));
                        Log.d("FormTransaksi", anError.getErrorDetail());
                        Toast.makeText(getApplicationContext(), anError.getErrorDetail() + " Code : " + anError.getErrorCode(), Toast.LENGTH_SHORT).show();
                    }

                });

//for idVendor
//        }

    }

//    @Override
//    public void onItemClick(View view, int position) {
//
//        LinearLayout linearRodaDua = view.findViewById(R.id.linear_ekspedisi_roda_dua);
//        LinearLayout linearRodaEmpat = view.findViewById(R.id.linear_ekspedisi_roda_empat);
//
//        int harga_ongkir_adapter;
//        String nama_kurir;
//
//        if (isClicked.equals("linearRodaDua")) {
//            harga_ongkir_adapter = 10000;
//            nama_kurir = "motor";
//            dataOngkir.add(harga_ongkir_adapter);
//            dataKurir.add(nama_kurir);
//        }
//
//        if (isClicked.equals("linearRodaEmpat")) {
//            harga_ongkir_adapter = 50000;
//            nama_kurir = "mobil";
//            dataOngkir.add(harga_ongkir_adapter);
//            dataKurir.add(nama_kurir);
//        }
//
//        int tambahOngkir2 = 0;
//
//        if (dataOngkir.size() > 0) {
//            for (int jumlahOngkir : dataOngkir) {
//
//                tambahOngkir2 = tambahOngkir2 + jumlahOngkir;
//
//            }
//
//            binding.lblNominalOngkir.setText(FormatText.rupiahFormat(Double.parseDouble(String.valueOf(tambahOngkir2))));
//            int total_bayar_seluruh;
//
//            total_bayar_seluruh = Integer.parseInt(total_belanja) + tambahOngkir2;
//
//            binding.lblNominalTotal.setText(FormatText.rupiahFormat(Double.parseDouble(String.valueOf(total_bayar_seluruh))));
//
//            if (dataOngkir.size()>=dataList.size()){
//
//                dataOngkir.clear();
//                dataKurir.clear();
//
//            }
//
//        }
//
//
//    }

//    @Override
//    public void onLongItemClick(int position) {
//
//    }
}
