package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.form_pemesanan.FormTransaksiActivity;
import www.starcom.com.jualanpraktis.interfaces.PilihPengirimanClickInterface;

public class PilihPengirimanAdapter extends RecyclerView.Adapter<PilihPengirimanAdapter.PilihPengirimanViewHolder> {

    Context context;
    private ArrayList<HashMap<String, String>> data;
    ArrayList<String> hargaOngkirArray = new ArrayList<>();
    int harga_ongkir;
    String nama_kurir;
    private FormTransaksiActivity formTransaksiActivity;
    private PilihPengirimanClickInterface onItemClickListener;


    public PilihPengirimanAdapter(Context context, ArrayList<HashMap<String, String>> data, FormTransaksiActivity formTransaksiActivity, PilihPengirimanClickInterface clickListener) {
        this.context = context;
        this.data = data;
        this.formTransaksiActivity = formTransaksiActivity;
        this.onItemClickListener = clickListener;
    }

    @NonNull
    @Override
    public PilihPengirimanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_pilihan_pengiriman, parent, false);
        return new PilihPengirimanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PilihPengirimanViewHolder holder, int position) {

        Drawable dr = formTransaksiActivity.getResources().getDrawable(R.drawable.background_pilih_pengiriman_red);

        Drawable dr2 = formTransaksiActivity.getResources().getDrawable(R.drawable.background_pilih_pengiriman);


        holder.linearRodaDua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                formTransaksiActivity.dataOngkir.clear();

                formTransaksiActivity.isClicked = "linearRodaDua";
                onItemClickListener.onItemClick(v, position);
//                harga_ongkir = 10000;
//                nama_kurir = "motor";
//                formTransaksiActivity.dataOngkir.add(harga_ongkir);
//                formTransaksiActivity.dataKurir.add(nama_kurir);
                holder.linearRodaDua.setBackground(dr);
                holder.linearRodaEmpat.setBackground(dr2);

                //set text color kendaraan roda dua
                holder.txtKendaraanRodaDua.setTextColor(ContextCompat.getColor(formTransaksiActivity, R.color.white));
                holder.txtHargaRodaDua.setTextColor(ContextCompat.getColor(formTransaksiActivity, R.color.white));
                holder.txtPerlokasiRodaDua.setTextColor(ContextCompat.getColor(formTransaksiActivity, R.color.white));

                //set text color kendaraan roda empat
                holder.txtKendaraanRodaEmpat.setTextColor(ContextCompat.getColor(formTransaksiActivity, R.color.colorPrimary));
                holder.txtHargaRodaEmpat.setTextColor(ContextCompat.getColor(formTransaksiActivity, R.color.colorPrimary));
                holder.txtPerlokasiRodaEmpat.setTextColor(ContextCompat.getColor(formTransaksiActivity, R.color.colorPrimary));

            }
        });

        holder.linearRodaEmpat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                formTransaksiActivity.dataOngkir.clear();
                formTransaksiActivity.isClicked = "linearRodaEmpat";
                onItemClickListener.onItemClick(v, position);
                //                harga_ongkir = 50000;
//                nama_kurir = "mobil";
//                formTransaksiActivity.dataOngkir.add(harga_ongkir);
//                formTransaksiActivity.dataKurir.add(nama_kurir);
                holder.linearRodaEmpat.setBackground(dr);
                holder.linearRodaDua.setBackground(dr2);

                //set text color kendaraan roda empat
                holder.txtKendaraanRodaEmpat.setTextColor(ContextCompat.getColor(formTransaksiActivity, R.color.white));
                holder.txtHargaRodaEmpat.setTextColor(ContextCompat.getColor(formTransaksiActivity, R.color.white));
                holder.txtPerlokasiRodaEmpat.setTextColor(ContextCompat.getColor(formTransaksiActivity, R.color.white));

                //set text color kendaraan roda dua
                holder.txtKendaraanRodaDua.setTextColor(ContextCompat.getColor(formTransaksiActivity, R.color.colorPrimary));
                holder.txtHargaRodaDua.setTextColor(ContextCompat.getColor(formTransaksiActivity, R.color.colorPrimary));
                holder.txtPerlokasiRodaDua.setTextColor(ContextCompat.getColor(formTransaksiActivity, R.color.colorPrimary));

            }
        });

        holder.txtNamaVendor.setText(data.get(position).get("nama_vendor"));

//        for (int i = 0; i<getItemCount(); i++){
//
//            hargaOngkirArray.add(String.valueOf(harga_ongkir));
//
//        }


    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public class PilihPengirimanViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearRodaDua, linearRodaEmpat;
        TextView txtNamaVendor, txtKendaraanRodaDua, txtHargaRodaDua, txtPerlokasiRodaDua, txtKendaraanRodaEmpat, txtHargaRodaEmpat, txtPerlokasiRodaEmpat;


        public PilihPengirimanViewHolder(@NonNull View itemView) {
            super(itemView);



        }
    }
}
