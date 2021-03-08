package www.starcom.com.jualanpraktis.feature.form_pemesanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
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
import www.starcom.com.jualanpraktis.MainActivity;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.SubKategori.order;
import www.starcom.com.jualanpraktis.adapter.CartAdapter;
import www.starcom.com.jualanpraktis.databinding.ActivityFormPilihEkspedisiBinding;
import www.starcom.com.jualanpraktis.databinding.ActivityFormTransaksiBinding;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.feature.pembayaran.PembayaranActivity;

public class FormPilihEkspedisiActivity extends AppCompatActivity {

    public static final String EXTRA_ID_TRANSAKSI = "extraIdTransaksi";
    public static final String EXTRA_ID_CUSTOMER = "extraIdCustomer";
    public static final String EXTRA_TANGGAL_TRANSAKSI = "extraTanggalTransaksi";
    public static final String EXTRA_NAMA_PENERIMA = "extraNamaPenerima";
    public static final String EXTRA_AlAMAT_PENERIMA = "extraAlamatPenerima";
    public static final String EXTRA_CITY_DESTINATION = "extraKota";
    public static final String EXTRA_PROVINCE_DESTINATION = "extraProvinsi";
    public static final String EXTRA_KECAMATAN = "extraKecamatan";
    public static final String EXTRA_NO_HP = "extraNoHp";
    public static final String EXTRA_HP_PEMESAN = "extraHpPemesan";
    public static final String EXTRA_TOTAL = "total";
    public static final String EXTRA_BERAT = "berat";

    Activity activity = FormPilihEkspedisiActivity.this;
    ActivityFormPilihEkspedisiBinding binding;

    String id_provinsi,id_kota,id_kecamatan;
    String nama_provinsi,nama_kota,nama_kecamatan;
    loginuser user ;
    String def;
    String total_berat,onngkir_ro;
    ArrayList<HashMap<String,String>> listPesanan = new ArrayList<>();
    String idVendor;
    String idTransaksi,total_belanja;
    String kode_ekspedisi,harga_layanan;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    List<order> list = new ArrayList<>();
    int grandTotal;

    String opsi_pembayaran;
    String coorperate = "0";
    Pref pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pilih_ekspedisi);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_form_pilih_ekspedisi);
        user = SharedPrefManager.getInstance(this).getUser();
        AndroidNetworking.initialize(getApplicationContext());
        pref = new Pref(getApplicationContext());
        progressDialog = new ProgressDialog(FormPilihEkspedisiActivity.this);
        def = "Klik untuk memilih";

        total_berat = getIntent().getExtras().getString(EXTRA_BERAT);
        total_belanja = getIntent().getExtras().getString(EXTRA_TOTAL);
        idTransaksi = getIntent().getExtras().getString(EXTRA_ID_TRANSAKSI);
        listPesanan.clear();
        listPesanan = (ArrayList<HashMap<String, String>>) getIntent().getExtras().getSerializable("listPesanan");

        binding.idTransaksi.setText(idTransaksi);
//         binding.lblNamaPenerima.setText(user.getNama());
//         binding.lblNoTelpon.setText(user.getNo_hp());
//         binding.lblAlamat.setText(user.getAlamat());
        binding.idTransaksi.setText(idTransaksi);

        cardClickEkspedisi(binding.cvEkspedisi,0);
        cardClickEkspedisi(binding.cvLayanan,1);
        cardClickEkspedisi(binding.cvJenisPembayaran,2 );


        binding.btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validasiEkspedisi();
            }
        });

        //list pesanan saya
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(linearLayoutManager);


        CartAdapter adapter = new CartAdapter(activity,listPesanan,null);
        binding.recyclerView.setAdapter(adapter);

        binding.lblNominalBelanja.setText(FormatText.rupiahFormat(Double.parseDouble(total_belanja)));
        binding.lblNominalTotal.setText(FormatText.rupiahFormat(Double.parseDouble(total_belanja)));

        //coorperate
        if (pref.getLoginMethod().equals("coorperate")){
            kode_ekspedisi = "antarbatam";
            opsi_pembayaran = "corporate";
            coorperate = "1";
            id_provinsi = "17";
            id_kota = "48";
            id_kecamatan = "673";
            nama_provinsi = "";
            nama_kota = "";
            nama_kecamatan = "";

            binding.cvEkspedisi.setClickable(false);
            binding.lblEkspedisi.setText("ANTAR BATAM");
            binding.imgEkspedisi.setVisibility(View.GONE);
            binding.cvJenisPembayaran.setVisibility(View.GONE);


            int harga = 0;
            if (Integer.parseInt(total_belanja)>=200000){
                harga = 0;
            }else{
                if (Integer.parseInt(total_berat)<3001){
                    harga = 10000;
                }else if (Integer.parseInt(total_berat)<6001){
                    harga = 20000;
                }else if (Integer.parseInt(total_berat)<10001){
                    harga = 25000;
                }else if (Integer.parseInt(total_berat)<20001){
                    harga = 40000;
                }else if (Integer.parseInt(total_berat)>20001){
                    harga = 0;
                }
            }


            harga_layanan = Integer.toString(harga);
            binding.lblNominalOngkir.setText(FormatText.rupiahFormat(Double.parseDouble(harga_layanan)));
            grandTotal = Integer.parseInt(harga_layanan)+Integer.parseInt(total_belanja);
            binding.lblNominalTotal.setText(FormatText.rupiahFormat(grandTotal));
        }

    }

    private void validasiEkspedisi(){
        if (kode_ekspedisi!=null){
            if (!kode_ekspedisi.equals("antarbatam")){
                if (kode_ekspedisi==null){
                    Toast.makeText(activity,"Pilih Ekspedisi",Toast.LENGTH_SHORT).show();
                }else if (harga_layanan==null){
                    Toast.makeText(activity,"Pilih Layanan",Toast.LENGTH_SHORT).show();
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
            }else {
                if (kode_ekspedisi==null){
                    Toast.makeText(activity,"Pilih Ekspedisi",Toast.LENGTH_SHORT).show();
                }else if (harga_layanan==null){
                    Toast.makeText(activity,"Pilih Layanan",Toast.LENGTH_SHORT).show();
                }else if (opsi_pembayaran==null){
                    Toast.makeText(activity,"Pilih Jenis Pembayaran",Toast.LENGTH_SHORT).show();
                }else {
                    new AlertDialog.Builder(activity)
                            .setTitle("Konfirmasi Pemesanan")
                            .setMessage("Anda yakin ingin melakukan pemesanan ini?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    processTransaksi();


                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_menu_info_details)
                            .show();
                }
            }
        }else {
            Toast.makeText(activity,"Pilih Ekspedisi",Toast.LENGTH_SHORT).show();
        }


    }

    private void cardClickEkspedisi(CardView cardView, final int reqCode){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 0 = dialog ekspedisi, selain itu ke activity pilih layanan
                if (reqCode==0){
                    binding.cvLayanan.setVisibility(View.GONE);
                    binding.cvJenisPembayaran.setVisibility(View.GONE);
                    dialogEkspedisi();
                }else if (reqCode==1){

                    HashMap<String,String> param = new HashMap<>();

                    param.put("origin","48");
                    param.put("originType","city");
                    param.put("destination",id_kecamatan);
                    param.put("destinationType","subdistrict");
                    param.put("weight",total_berat);
                    param.put("courier",kode_ekspedisi);
                    Intent intent = new Intent(activity,PilihLayananEkspedisiActivity.class);
                    intent.putExtra("param",param);
                    startActivityForResult(intent,4);
                }else if (reqCode==2){
                    dialogJenisPembayaran();
                }
            }
        });
    }

    private void dialogJenisPembayaran(){
        opsi_pembayaran=null;
        binding.lblJenisPembayaran.setText("Pilih Jenis Pembayaran");
        AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        View layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_jenis_pembayaran, null);

        final ConstraintLayout pembayaran_cod = layoutView.findViewById(R.id.pembayaran_cod);
        final ConstraintLayout pembayaran_lainnya = layoutView.findViewById(R.id.pembayaran_lainnya);
        final ConstraintLayout pembayaran_banksumut = layoutView.findViewById(R.id.pembayaran_banksumut);

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
                binding.lblJenisPembayaran.setText("Transfer Bank SUMUT");
                alertDialog.dismiss();
            }
        });

        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    private void dialogEkspedisi(){
        kode_ekspedisi = null;
        harga_layanan = null;
        opsi_pembayaran = null;
        AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        View layoutView = activity.getLayoutInflater().inflate(R.layout.dialog_ekspedisi, null);

        final ConstraintLayout jne = layoutView.findViewById(R.id.jne);
        final ConstraintLayout jnt = layoutView.findViewById(R.id.jnt);
        final ConstraintLayout tiki = layoutView.findViewById(R.id.tiki);
        final ConstraintLayout sicepat = layoutView.findViewById(R.id.sicepat);
        final ConstraintLayout antarbatam = layoutView.findViewById(R.id.kurirjualanpraktis);

        if (id_kota.equals("48")){
            antarbatam.setVisibility(View.VISIBLE);
        }else {
            antarbatam.setVisibility(View.GONE);
        }

        pickEkspedisi(jne,0,"jne");
        pickEkspedisi(jnt,1,"jnt");
        pickEkspedisi(tiki,2,"tiki");
        pickEkspedisi(sicepat,3,"sicepat");
        pickEkspedisi(antarbatam,4,"antarbatam");

        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }

    private void pickEkspedisi(ConstraintLayout constraintLayout, final int reqCode,final String kode){

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 0 = jne,1=jnt,2=tiki,3=sicepat
                kode_ekspedisi = kode;
                if (!kode.equals("antarbatam")){
                    opsi_pembayaran = "transfer";
                    binding.imgEkspedisi.setVisibility(View.VISIBLE);
                    binding.cvLayanan.setVisibility(View.VISIBLE);
                    binding.lblLayanan.setText("Pilih Layanan");
                    binding.lblHargaLayanaan.setVisibility(View.GONE);
                    binding.lblPengirimanLayanaan.setVisibility(View.GONE);
                    binding.lblNominalOngkir.setText("0");
                    binding.lblNominalBelanja.setText(FormatText.rupiahFormat(Double.parseDouble(total_belanja)));
                    binding.lblNominalTotal.setText(FormatText.rupiahFormat(Double.parseDouble(total_belanja)));


                    if (reqCode==0){
                        binding.lblEkspedisi.setText("JNE");
                        binding.imgEkspedisi.setImageDrawable(getResources().getDrawable(R.drawable.jne));
                    }else if (reqCode==1){
                        binding.lblEkspedisi.setText("JNT");
                        binding.imgEkspedisi.setImageDrawable(getResources().getDrawable(R.drawable.jnt));
                    }else if (reqCode==2){
                        binding.lblEkspedisi.setText("TIKI");
                        binding.imgEkspedisi.setImageDrawable(getResources().getDrawable(R.drawable.tiki));
                    }else if (reqCode==3){
                        binding.lblEkspedisi.setText("SI CEPAT");
                        binding.imgEkspedisi.setImageDrawable(getResources().getDrawable(R.drawable.sicepat));

                    }

                }else{
                    if (reqCode==4){
                        binding.lblEkspedisi.setText("ANTAR BATAM");
                        binding.imgEkspedisi.setVisibility(View.GONE);
                        binding.cvJenisPembayaran.setVisibility(View.VISIBLE);

                        int harga = 0;
                        if (Integer.parseInt(total_belanja)>=200000){
                            harga = 0;
                        }else{
                            if (Integer.parseInt(total_berat)<3001){
                                harga = 10000;
                            }else if (Integer.parseInt(total_berat)<6001){
                                harga = 20000;
                            }else if (Integer.parseInt(total_berat)<10001){
                                harga = 25000;
                            }else if (Integer.parseInt(total_berat)<20001){
                                harga = 40000;
                            }else if (Integer.parseInt(total_berat)>20001){
                                harga = 0;
                            }
                        }


                        harga_layanan = Integer.toString(harga);
                        binding.lblNominalOngkir.setText(FormatText.rupiahFormat(Double.parseDouble(harga_layanan)));
                        grandTotal = Integer.parseInt(harga_layanan)+Integer.parseInt(total_belanja);
                        binding.lblNominalTotal.setText(FormatText.rupiahFormat(grandTotal));
                    }
                }


                alertDialog.dismiss();
            }
        });

    }

    private void processTransaksi(){
        String host = "https://jualanpraktis.net/android/transaksi.php";
        Date date;
        Calendar calendar = Calendar.getInstance();
        if (opsi_pembayaran.equals("cod")){
            calendar.add(Calendar.DAY_OF_YEAR, 2);
            date = calendar.getTime();

        }else{
            //get 2 jam setelahnya
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)+2);
            date = calendar.getTime();
        }

        // Calendar calendar=Calendar.getInstance();

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String currentDateTime = currentDate +" "+ currentTime;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String tomorrowAsString = dateFormat.format(date);
        String tomorrowAsString2 = timeFormat.format(date);

        progressDialog.setTitle("Memproses Pemesanan");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Map<String,String> params = new HashMap<String,String>();
        params.put("id_customer",user.getId());
        params.put("id_transaksi",idTransaksi);
        params.put("tgl_transaksi",getIntent().getExtras().getString(EXTRA_TANGGAL_TRANSAKSI));
        params.put("nama_penerima",getIntent().getExtras().getString(EXTRA_NAMA_PENERIMA));
        params.put("alamat",getIntent().getExtras().getString(EXTRA_AlAMAT_PENERIMA));
        params.put("city_destination",getIntent().getExtras().getString(EXTRA_CITY_DESTINATION));
        params.put("province_destination",getIntent().getExtras().getString(EXTRA_PROVINCE_DESTINATION));
        params.put("subdistrict_destination",getIntent().getExtras().getString(EXTRA_KECAMATAN));
        params.put("no_hp",getIntent().getExtras().getString(EXTRA_NO_HP));
        params.put("hp_pemesan",getIntent().getExtras().getString(EXTRA_HP_PEMESAN));
        for(int i = 0; i<params.size(); i++){
            params.put("id_vendor["+i+"]", idVendor);
            params.put("total_belanja["+i+"]", total_belanja);
            params.put("weight["+i+"]",total_berat);
            params.put("courier["+i+"]",kode_ekspedisi);
            params.put("ongkos_kirim["+i+"]",harga_layanan);
            params.put("total_bayar["+i+"]",Integer.toString(grandTotal));
        }
        params.put("opsi",opsi_pembayaran);
//        params.put("time_limit_order",tomorrowAsString);
//        params.put("time_limit",tomorrowAsString2);
//        params.put("time_limit_order2",tomorrowAsString);
//        params.put("time_limit2",tomorrowAsString2);
//        params.put("total_belanja",total_belanja);
//        params.put("weight",total_berat);
//        params.put("courier",kode_ekspedisi);
//        params.put("ongkos_kirim",harga_layanan);
//        params.put("tot_ongkos_kirim",harga_layanan);
//        params.put("total_bayar",Integer.toString(grandTotal));


        Log.d("HasilKirim", params.toString());

//        if (pref.getLoginMethod().equals("coorperate")){
//
//            params.put("corporate",coorperate);
//        }

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
                        if (opsi_pembayaran.equals("cod")){

                            String alamatinput = getIntent().getExtras().getString(EXTRA_AlAMAT_PENERIMA);
                            String kecamatanInput = getIntent().getExtras().getString(EXTRA_KECAMATAN);
                            String kota = getIntent().getExtras().getString(EXTRA_CITY_DESTINATION);
                            String provinsi = getIntent().getExtras().getString(EXTRA_PROVINCE_DESTINATION);

                            String alamat =  alamatinput + ", Kecamatan "+kecamatanInput
                                    +", Kota/Kabupaten "+kota+", Provinsi "+provinsi;

                            Intent intent = new Intent(activity, ResultCodActivity.class);
                            intent.putExtra("id_transaksi",idTransaksi);
                            intent.putExtra("nama",getIntent().getExtras().getString(EXTRA_NAMA_PENERIMA));
                            intent.putExtra("alamat",alamat);
                            intent.putExtra("nominal_belanja",total_belanja);
                            intent.putExtra("ongkos_kirim",harga_layanan);
                            intent.putExtra("berat",total_berat);
                            intent.putExtra("total",Integer.toString(grandTotal));
                            intent.putExtra("no_hp",getIntent().getExtras().getString(EXTRA_NO_HP));
                            intent.putExtra("dataList",listPesanan);
                            startActivity(intent);
                            finish();
                            //    Toast.makeText(getApplicationContext(), "Berhasil Melakukan Pemesanan", Toast.LENGTH_SHORT).show();
                        }else if (opsi_pembayaran.equals("transfer")){

                            Intent intent = new Intent(activity, PembayaranActivity.class);
                            intent.putExtra("id_transaksi",idTransaksi);
                            intent.putExtra("total",Integer.toString(grandTotal));
                            intent.putExtra("no_hp",getIntent().getExtras().getString(EXTRA_NO_HP));
                            intent.putExtra("dataList",listPesanan);
                            startActivity(intent);
                            finish();
                            //       Toast.makeText(getApplicationContext(), "Berhasil Melakukan Pemesanan", Toast.LENGTH_SHORT).show();
                        }else if (opsi_pembayaran.equals("transfer manual")){
                            startActivity(new Intent(activity,ResultTransferActivity.class)
                                    .putExtra("id_transaksi",idTransaksi)
                                    .putExtra("status","result")
                                    .putExtra("total",Integer.toString(grandTotal)));
                        } else {
                            long limitTersisa = Integer.parseInt(pref.getLimitBelanja()) - grandTotal;
                            pref.setLimitBelanja(String.valueOf(limitTersisa));
                            startActivity(new Intent(activity, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
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
                        Toast.makeText(getApplicationContext(), "Gagal Melakukan Pemesanan", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}