package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.form_pemesanan.FormTransaksiActivity;
import www.starcom.com.jualanpraktis.feature.produk.ProdukDetailActivity;

public class VariasiAdapter extends RecyclerView.Adapter<VariasiAdapter.VariasiViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> listDataVariasi = new ArrayList<>();
    private Pref pref;

    ProdukDetailActivity produkDetailActivity;

    private static int lastClickedPosition = -1;
    private int selectedItem;

    public VariasiAdapter(Context context, ArrayList<HashMap<String, String>> listDataVariasi, ProdukDetailActivity produkDetailActivity) {
        this.context = context;
        this.listDataVariasi = listDataVariasi;
        this.produkDetailActivity = produkDetailActivity;
        selectedItem = 0;
    }

    @NonNull
    @Override
    public VariasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_variasi_detail_produk, parent, false);
        return new VariasiViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull VariasiViewHolder holder, int position) {

        HashMap<String, String> item = new HashMap<>();
        item = this.listDataVariasi.get(position);
        pref = new Pref(context.getApplicationContext());

//        Drawable dr = context.getResources().getDrawable(R.drawable.background_pilih_pengiriman_red);
//        Drawable dr2 = context.getResources().getDrawable(R.drawable.background_border_black);
//        holder.txtVariasi.setTextColor(ContextCompat.getColor(context, R.color.black));
//
//        holder.linearVariasi.setBackground(dr2);


        holder.txtVariasi.setText(item.get("variasi"));
        String nama_variasi = item.get("variasi");
        String id_variasi = item.get("id_variasi");
        String stock = item.get("stok");

        produkDetailActivity.getVariasi = id_variasi;

//        if (selectedItem == position){
//
//            holder.linearVariasi.setBackground(dr);
//            holder.txtVariasi.setTextColor(ContextCompat.getColor(context, R.color.white));
//
//            Toast.makeText(context, "Anda Memilih : "+nama_variasi, Toast.LENGTH_SHORT).show();
//

//
//        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                produkDetailActivity.get_nama_variasi = nama_variasi;
                produkDetailActivity.get_id_variasi = id_variasi;
                produkDetailActivity.getVariasi = id_variasi;
                produkDetailActivity.txtVariasiDetailProduk.setText(nama_variasi);
                produkDetailActivity.txtStock.setText(stock);
                produkDetailActivity.dialogVariasi.dismiss();

            }
        });



    }

    @Override
    public int getItemCount() {
        return (null != listDataVariasi ? listDataVariasi.size() : 0);
    }

    public class VariasiViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearVariasi;
        TextView txtVariasi;

        public VariasiViewHolder(@NonNull View itemView) {
            super(itemView);
            linearVariasi = itemView.findViewById(R.id.linear_item_variasi);
            txtVariasi = itemView.findViewById(R.id.txt_item_nama_variasi);
        }
    }
}
