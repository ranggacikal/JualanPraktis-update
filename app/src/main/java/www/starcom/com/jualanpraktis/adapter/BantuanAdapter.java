package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.model_retrofit.model_bantuan.DataItem;

public class BantuanAdapter extends RecyclerView.Adapter<BantuanAdapter.BantuanViewHolder> {

    Context context;
    List<DataItem> dataItems;

    public BantuanAdapter(Context context, List<DataItem> dataItems) {
        this.context = context;
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public BantuanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bantuan, parent, false);
        return new BantuanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BantuanViewHolder holder, int position) {

        Glide.with(context)
                .load(dataItems.get(position).getIcon())
                .error(R.drawable.logo_jualan_merah)
                .into(holder.imgBantuan);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataItems.get(position).getLink()));
                context.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class BantuanViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBantuan;

        public BantuanViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBantuan = itemView.findViewById(R.id.img_item_bantuan);
        }
    }
}
