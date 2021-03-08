package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.R;

public class PenghasilanProsesAdapter extends RecyclerView.Adapter<PenghasilanProsesAdapter.PenghasilanProsesViewHolder> {

    ArrayList<HashMap<String, String>> listPenghasilanProses = new ArrayList<>();
    Context context;
    private Pref pref;

    public PenghasilanProsesAdapter(ArrayList<HashMap<String, String>> listPenghasilanProses, Context context) {
        this.listPenghasilanProses = listPenghasilanProses;
        this.context = context;
    }

    @NonNull
    @Override
    public PenghasilanProsesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_penghasilan_saya_batal, parent, false);
        return new PenghasilanProsesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenghasilanProsesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PenghasilanProsesViewHolder extends RecyclerView.ViewHolder {
        public PenghasilanProsesViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
