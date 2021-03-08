package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import www.starcom.com.jualanpraktis.PlayVideoActivity;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.interfaces.FragmentCommunication;
import www.starcom.com.jualanpraktis.model_retrofit.model_penggunaan_app.DataItem;

public class PenggunaanAppAdapter extends RecyclerView.Adapter<PenggunaanAppAdapter.PenggunaanAppViewHolder> {

    Context context;
    List<DataItem> dataItems;
    FragmentCommunication mCommunication;

    public PenggunaanAppAdapter(Context context, List<DataItem> dataItems) {
        this.context = context;
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public PenggunaanAppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_panduan_app, parent, false);
        return new PenggunaanAppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenggunaanAppViewHolder holder, int position) {

        holder.txtnama.setText(dataItems.get(position).getTitle());
        holder.txtWaktu.setText(dataItems.get(position).getDurasi());

        String id = dataItems.get(position).getIdVideo();
        String url = "https://img.youtube.com/vi/"+id+"/0.jpg";

        Glide.with(context)
                .load(url)
                .into(holder.imgPanduan);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("id_video", dataItems.get(position).getIdVideo());
                intent.putExtra("nama_video", dataItems.get(position).getTitle());
                intent.putExtra("waktu_video", dataItems.get(position).getDurasi());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class PenggunaanAppViewHolder extends RecyclerView.ViewHolder {

        TextView txtnama, txtWaktu;
        ImageView imgPanduan;

        public PenggunaanAppViewHolder(@NonNull View itemView) {
            super(itemView);

            txtnama = itemView.findViewById(R.id.text_item_nama_panduan);
            txtWaktu = itemView.findViewById(R.id.text_item_waktu_panduan);
            imgPanduan = itemView.findViewById(R.id.img_item_panduan);
        }
    }
}
