package www.starcom.com.jualanpraktis;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import www.starcom.com.jualanpraktis.feature.akun.AkunCoorperateFragment;
import www.starcom.com.jualanpraktis.feature.akun.AkunFragment;
import www.starcom.com.jualanpraktis.feature.belanja_bulanan.ListBelanjaBulananFragment;
import www.starcom.com.jualanpraktis.feature.pembayaran.ListPembayaranFragment;
import www.starcom.com.jualanpraktis.feature.pesanan_saya.ListPesananFragment;

public class PanduanFragment extends Fragment {

    TabLayout tabLayoutPanduan;
    FrameLayout frameLayoutPanduan;

    private PenggunaanAppFragment penggunaanAppFragment;
    private TipsTrickFragment tipsTrickFragment;

    public PanduanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_panduan, container, false);
        tabLayoutPanduan = rootView.findViewById(R.id.tablayoutPanduan);
        frameLayoutPanduan = rootView.findViewById(R.id.framePanduan);


        bindWidgetsWithAnEvent();
        setupTabLayout();
        return rootView;
    }

    private void setupTabLayout() {
//        tabLayout.removeAllTabs();
//        AkunFragment = new AkunFragment();
        penggunaanAppFragment = new PenggunaanAppFragment();
        tipsTrickFragment = new TipsTrickFragment();
        // tabLayout.removeAllTabs();
        tabLayoutPanduan.addTab(tabLayoutPanduan.newTab().setText("Penggunaan App"));
        tabLayoutPanduan.addTab(tabLayoutPanduan.newTab().setText("Tips & Trick"));

    }

    private void bindWidgetsWithAnEvent()
    {
        tabLayoutPanduan.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0 :
                replaceFragment(penggunaanAppFragment);
                break;
            case 1 :
                replaceFragment(tipsTrickFragment);
                break;
        }
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.framePanduan, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}