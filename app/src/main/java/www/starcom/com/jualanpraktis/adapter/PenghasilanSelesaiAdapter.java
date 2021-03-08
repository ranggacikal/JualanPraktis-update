package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.akun.RincianTransaksiActivity;
import www.starcom.com.jualanpraktis.model.ListPenghasilanSaya;

public class PenghasilanSelesaiAdapter extends RecyclerView.Adapter<PenghasilanSelesaiAdapter.PenghasilanSelesaiViewHolder> {

    ArrayList<HashMap<String, String>> listPenghasilanSelesai = new ArrayList<>();
    Context context;
    private Pref pref;
    String id_transaksi;


    public PenghasilanSelesaiAdapter(ArrayList<HashMap<String, String>> listPenghasilanSelesai, Context context) {
        this.listPenghasilanSelesai = listPenghasilanSelesai;
        this.context = context;
    }

    @NonNull
    @Override
    public PenghasilanSelesaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_status_dipesan, parent, false);
        return new PenghasilanSelesaiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenghasilanSelesaiViewHolder holder, int position) {

        HashMap<String, String> item = new HashMap<>();
        item = this.listPenghasilanSelesai.get(position);
        pref = new Pref(context.getApplicationContext());

        String image = item.get("gambar");
        String url = "https://jualanpraktis.net/img/"+image;


        Glide.with(context)
                .load(image)
                .into(holder.imgBarang);

        holder.txtId.setText(item.get("id_transaksi"));
        holder.txtTanggal.setText(item.get("tanggal"));
        holder.txtNama.setText(item.get("nama_produk"));
        holder.txtVariasi.setText(item.get("variasi"));
        holder.txtHargaJual.setText("Rp"+ NumberFormat.getInstance().format(Integer.parseInt(item.get("harga_jual"))));
        holder.txthargaProduk.setText("Rp"+ NumberFormat.getInstance().format(Integer.parseInt(item.get("harga_produk"))));
        holder.txtKeuntungan.setText("Rp"+ NumberFormat.getInstance().format(Integer.parseInt(item.get("untung"))));
        holder.txtStatus.setText(item.get("status_pesanan"));

        id_transaksi = holder.txtId.getText().toString();

        String status_kirim = item.get("status_kirim");

        AndroidNetworking.initialize(context.getApplicationContext());
        getJumlahProdukLainnya(id_transaksi, holder.txtProdukLainnya, status_kirim);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = holder.txtId.getText().toString();
                String tanggal = holder.txtTanggal.getText().toString();
                String status = holder.txtStatus.getText().toString();
                Intent intent = new Intent(context, RincianTransaksiActivity.class);
                intent.putExtra("id_transaksi", id);
                intent.putExtra("tanggal", tanggal);
                intent.putExtra("status", status);
                intent.putExtra("status_kirim", status_kirim);
                context.startActivity(intent);
            }
        });

    }

    private void getJumlahProdukLainnya(String id_transaksi, TextView txtProdukLainnya, String status_kirim) {

        String url = "https://jualanpraktis.net/android/detail_pesanan.php";

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.post(url)
                .addBodyParameter("id_transaksi", id_transaksi)
                .addBodyParameter("status_kirim", status_kirim)
                .setTag(context)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {


                            JSONArray array = response.getJSONArray("data_produk");


                            String jumlah_data2 = String.valueOf(array.length());
                            int jumlah_data = Integer.parseInt(jumlah_data2);
                            int totalJumlahData = jumlah_data - 1;

                            if (totalJumlahData<1){
                                txtProdukLainnya.setText("");
                            }else if (totalJumlahData>=1){
                                txtProdukLainnya.setText("+ "+totalJumlahData+" Produk Lainnya");
                            }

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
                            Toast.makeText(context, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            if (anError.getErrorDetail().equals("connectionError")) {
                                Toast.makeText(context, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    @Override
    public int getItemCount() {
        return (null != listPenghasilanSelesai ? listPenghasilanSelesai.size() : 0);
    }

    public class PenghasilanSelesaiViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearProdukLain;
        TextView txtId, txtTanggal, txtNama, txtVariasi, txthargaProduk, txtHargaJual, txtKeuntungan, txtStatus, txtProdukLainnya;
        ImageView imgBarang;

        public PenghasilanSelesaiViewHolder(@NonNull View itemView) {
            super(itemView);
            linearProdukLain = itemView.findViewById(R.id.linear_list_produk_lain_status_dipesan);
            txtId = itemView.findViewById(R.id.text_list_id_status_transaksi_dipesan);
            txtTanggal = itemView.findViewById(R.id.text_list_tanggal_status_transaksi_dipesan);
            imgBarang = itemView.findViewById(R.id.img_list_gambar_status_transaksi_dipesan);
            txtNama = itemView.findViewById(R.id.text_list_nama_status_transaksi_dipesan);
            txtVariasi = itemView.findViewById(R.id.text_list_variasi_status_transaksi_dipesan);
            txthargaProduk = itemView.findViewById(R.id.text_list_hargaproduk_status_transaksi_dipesan);
            txtHargaJual = itemView.findViewById(R.id.text_list_hargajual_status_transaksi_dipesan);
            txtKeuntungan = itemView.findViewById(R.id.text_list_keuntungan_status_transaksi_dipesan);
            txtStatus = itemView.findViewById(R.id.text_list_status_transaksi_dipesan);
            txtProdukLainnya = itemView.findViewById(R.id.text_produk_lainnya_dipesan);
        }
    }
}
