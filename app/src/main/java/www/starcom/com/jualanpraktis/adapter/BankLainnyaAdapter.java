package www.starcom.com.jualanpraktis.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.akun.DetailRekeningBankActivity;
import www.starcom.com.jualanpraktis.feature.akun.PilihBankDetailRekeningActivity;
import www.starcom.com.jualanpraktis.model_retrofit.bank_lainnya.DataItem;

public class BankLainnyaAdapter extends RecyclerView.Adapter<BankLainnyaAdapter.BankLainnyaViewHolder> {

    Context context;
    List<DataItem> listBank;
    PilihBankDetailRekeningActivity pilihBankDetailRekeningActivity;

    public BankLainnyaAdapter(Context context, List<DataItem> listBank, PilihBankDetailRekeningActivity pilihBankDetailRekeningActivity) {
        this.context = context;
        this.listBank = listBank;
        this.pilihBankDetailRekeningActivity = pilihBankDetailRekeningActivity;
    }

    @NonNull
    @Override
    public BankLainnyaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank_lainnya, parent, false);
        return new BankLainnyaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankLainnyaViewHolder holder, int position) {

        final String namaBank = listBank.get(position).getNamaBank();

        holder.txtNamaBank.setText(namaBank);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intentNama = pilihBankDetailRekeningActivity.nama;
                String intentRekening = pilihBankDetailRekeningActivity.rekening;
                Intent intent = new Intent(context, DetailRekeningBankActivity.class);
                intent.putExtra("nama_bank", namaBank);
                intent.putExtra("nama", intentNama);
                intent.putExtra("rekening", intentRekening);
                intent.putExtra(DetailRekeningBankActivity.ExtraNamaBank, namaBank);
//                ((PilihBankDetailRekeningActivity)context).setResult(Activity.RESULT_OK, intent);
//                ((PilihBankDetailRekeningActivity)context).finish();
                ((PilihBankDetailRekeningActivity)context).startActivityForResult(intent, 0);
                ((PilihBankDetailRekeningActivity)context).finish();
                Log.d("PilihBankAdapter", "onClick Data: "+namaBank);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listBank.size();
    }

    public class BankLainnyaViewHolder extends RecyclerView.ViewHolder {

        TextView txtNamaBank;

        public BankLainnyaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNamaBank = itemView.findViewById(R.id.text_item_bank_lainnya);
        }
    }
}
