package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import www.starcom.com.jualanpraktis.Login.Pref;
import www.starcom.com.jualanpraktis.R;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.NotifikasiViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> listNotifikasi = new ArrayList<>();
    private Pref pref;

    public NotifikasiAdapter(Context context, ArrayList<HashMap<String, String>> listNotifikasi) {
        this.context = context;
        this.listNotifikasi = listNotifikasi;
    }

    @NonNull
    @Override
    public NotifikasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifikasi, parent, false);
        return new NotifikasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifikasiViewHolder holder, int position) {

        HashMap<String, String> item = new HashMap<>();
        item = this.listNotifikasi.get(position);
        pref = new Pref(context.getApplicationContext());

        holder.txtSubject.setText(item.get("subject"));
        holder.txtDeskripsi.setText(item.get("deskripsi"));


    }

    @Override
    public int getItemCount() {
        return (null != listNotifikasi ? listNotifikasi.size() : 0);
    }

    public class NotifikasiViewHolder extends RecyclerView.ViewHolder {

        TextView txtSubject, txtDeskripsi;

        public NotifikasiViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSubject = itemView.findViewById(R.id.text_item_subject_notifikasi);
            txtDeskripsi = itemView.findViewById(R.id.text_item_deskripsi_notifikasi);
        }
    }
}
