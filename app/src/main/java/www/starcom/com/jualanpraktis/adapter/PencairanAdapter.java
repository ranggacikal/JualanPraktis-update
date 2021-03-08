package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.model.ListPencairan;

public class PencairanAdapter extends RecyclerView.Adapter<PencairanAdapter.PencairanViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> listPencairan = new ArrayList<>();
    private Pref pref;

    public PencairanAdapter(Context context, ArrayList<HashMap<String, String>> listPencairan) {
        this.context = context;
        this.listPencairan = listPencairan;
    }

    @NonNull
    @Override
    public PencairanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat_pencairan, parent, false);
        return new PencairanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PencairanViewHolder holder, int position) {

        HashMap<String, String> item = new HashMap<>();
        item = this.listPencairan.get(position);
        pref = new Pref(context.getApplicationContext());

        holder.txtTanggal.setText(item.get("tgl"));
        holder.txtTotal.setText("Rp" + NumberFormat.getInstance().format(Integer.parseInt(item.get("komisi"))));

    }

    @Override
    public int getItemCount() {
        return (null != listPencairan ? listPencairan.size() : 0);
    }

    public class PencairanViewHolder extends RecyclerView.ViewHolder {

        TextView txtTanggal, txtWaktu, txtTotal;

        public PencairanViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTanggal = itemView.findViewById(R.id.text_item_tanggal_pencairan);
            txtTotal = itemView.findViewById(R.id.text_item_total_pencairan);
        }
    }
}
