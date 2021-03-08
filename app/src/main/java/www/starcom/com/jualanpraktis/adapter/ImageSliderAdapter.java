package www.starcom.com.jualanpraktis.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.R;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder> {

    private ArrayList<HashMap<String, String>> data;
    private Activity activity;

    public ImageSliderAdapter(ArrayList<HashMap<String, String>> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_slider, parent, false);
        return new ImageSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        HashMap<String, String> item = new HashMap<>();
        item = this.data.get(position);

        final String url = "https://jualanpraktis.net/img/" ;
        Glide.with(activity.getApplicationContext())
                .load(url+ item.get("gambar"))
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(200,200).skipMemoryCache(false))
                .into(holder.imgGambar);

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public class ImageSliderViewHolder extends RecyclerView.ViewHolder {

        ImageView imgGambar;

        public ImageSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGambar = itemView.findViewById(R.id.img_item_slider);
        }
    }
}
