package www.starcom.com.jualanpraktis.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.model.DataItem;

public class ListBankAdapter extends RecyclerView.Adapter<ListBankAdapter.ListBankViewHolder> {

    Context context;
    List<DataItem> dataBankItems;

    public ListBankAdapter(Context context, List<DataItem> dataBankItems) {
        this.context = context;
        this.dataBankItems = dataBankItems;
    }

    @NonNull
    @Override
    public ListBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank, parent, false);
        return new ListBankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListBankViewHolder holder, int position) {

        holder.txtNamaBank.setText(dataBankItems.get(position).getBank());
        holder.txtNoRekening.setText(dataBankItems.get(position).getNoRek());
        holder.txtAtasNama.setText(dataBankItems.get(position).getAtasNama());
        holder.btnSalin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(dataBankItems.get(position).getNoRek(), dataBankItems.get(position).getNoRek());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context,"No.Rekening Disalin",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataBankItems.size();
    }

    public class ListBankViewHolder extends RecyclerView.ViewHolder {

        TextView txtNamaBank, txtNoRekening, txtAtasNama;
        Button btnSalin;

        public ListBankViewHolder(@NonNull View itemView) {
            super(itemView);

            btnSalin = itemView.findViewById(R.id.button_salin_rekening);
            txtNamaBank = itemView.findViewById(R.id.text_nama_bank_konfirmasi);
            txtNoRekening = itemView.findViewById(R.id.text_no_rekening_bank);
            txtAtasNama = itemView.findViewById(R.id.text_atas_nama_bank);
        }
    }
}
