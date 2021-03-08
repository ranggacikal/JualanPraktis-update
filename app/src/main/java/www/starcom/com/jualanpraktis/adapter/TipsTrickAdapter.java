package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import www.starcom.com.jualanpraktis.PlayVideoActivity;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.model_retrofit.model_tips.DataItem;

public class TipsTrickAdapter extends RecyclerView.Adapter<TipsTrickAdapter.TipsTrickViewholder> {

    Context context;
    List<DataItem> dataItems;

    public TipsTrickAdapter(Context context, List<DataItem> dataItems) {
        this.context = context;
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public TipsTrickViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tips_tick, parent, false);
        return new TipsTrickViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipsTrickViewholder holder, int position) {

        holder.txtNama.setText(dataItems.get(position).getTitle());
        holder.txtWaktu.setText(dataItems.get(position).getDurasi());

        String id = dataItems.get(position).getIdVideo();
        String url = "https://img.youtube.com/vi/"+id+"/0.jpg";

        Glide.with(context)
                .load(url)
                .into(holder.imgTips);

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

    public class TipsTrickViewholder extends RecyclerView.ViewHolder {

        TextView txtNama, txtWaktu;
        ImageView imgTips;

        public TipsTrickViewholder(@NonNull View itemView) {
            super(itemView);

            txtNama = itemView.findViewById(R.id.text_item_nama_tips);
            txtWaktu = itemView.findViewById(R.id.text_item_waktu_tips);
            imgTips = itemView.findViewById(R.id.img_item_tips);
        }
    }
}
