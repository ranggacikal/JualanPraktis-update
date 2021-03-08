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

public class PenerimaRincianAdapter extends RecyclerView.Adapter<PenerimaRincianAdapter.PenerimaRincianViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> listPenerimaRincian = new ArrayList<>();
    private Pref pref;

    public PenerimaRincianAdapter(Context context, ArrayList<HashMap<String, String>> listPenerimaRincian) {
        this.context = context;
        this.listPenerimaRincian = listPenerimaRincian;
    }

    @NonNull
    @Override
    public PenerimaRincianViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_penerima_rincian_transaksi, parent, false);
        return new PenerimaRincianViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenerimaRincianViewHolder holder, int position) {

        HashMap<String, String> item = new HashMap<>();
        item = this.listPenerimaRincian.get(position);
        pref = new Pref(context.getApplicationContext());

        String alamat = item.get("alamat");
        String kecamatan = item.get("kecamatan");
        String kota = item.get("kota");

        String alamt_full = alamat+", "+kecamatan+", "+kota;

        holder.txtNama.setText(item.get("nama_penerima"));
        holder.txtNohp.setText(item.get("no_hp"));
        holder.txtAlamat.setText(alamt_full);
        holder.txtKodePos.setText(item.get("kode_pos"));
        holder.txtOpsiBayar.setText(item.get("opsi_pembayaran"));

    }

    @Override
    public int getItemCount() {
        return (null != listPenerimaRincian ? listPenerimaRincian.size() : 0);
    }

    public class PenerimaRincianViewHolder extends RecyclerView.ViewHolder {

        TextView txtNama, txtNohp, txtAlamat, txtKodePos, txtOpsiBayar;

        public PenerimaRincianViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.text_item_nama_rincian);
            txtNohp = itemView.findViewById(R.id.text_item_nohp_rincian);
            txtAlamat = itemView.findViewById(R.id.text_item_alamat_rincian);
            txtKodePos = itemView.findViewById(R.id.text_item_kodepos_rincian);
            txtOpsiBayar = itemView.findViewById(R.id.text_item_opsi_bayar_rincian);
        }
    }
}
