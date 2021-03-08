package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.R;

public class ViewPagerProdukAdapter extends PagerAdapter {

    Context context;
    ArrayList<HashMap<String, String>> listDataGambar = new ArrayList<>();
    LayoutInflater layoutInflater;
    private Pref pref;
    private View mCurrentView;

    public ViewPagerProdukAdapter(Context context, ArrayList<HashMap<String, String>> listDataGambar) {
        this.context = context;
        this.listDataGambar = listDataGambar;
    }

    @Override
    public int getCount() {
        return (null != listDataGambar ? listDataGambar.size() : 0);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        HashMap<String, String> item = new HashMap<>();
        item = this.listDataGambar.get(position);
        pref = new Pref(context.getApplicationContext());

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_view_pager, null);

        ImageView imageView = view.findViewById(R.id.img_item_view_pager);
        imageView.setTag(position);

        final String url = "https://jualanpraktis.net/img/" ;
        String imgUrl = item.get("gambar");

        Glide.with(context)
                .load(imgUrl)
                .into(imageView);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        mCurrentView = (View) object;
    }
}
