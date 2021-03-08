package www.starcom.com.jualanpraktis.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import www.starcom.com.jualanpraktis.EditAkunActivity;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.MainActivity;
import www.starcom.com.jualanpraktis.R;
import www.starcom.com.jualanpraktis.feature.akun.DetailRekeningBankActivity;
import www.starcom.com.jualanpraktis.feature.pembayaran.FormatText;
import www.starcom.com.jualanpraktis.keranjang;
import www.starcom.com.jualanpraktis.model.HargaItem;
import www.starcom.com.jualanpraktis.utils.Enums.ActionEnum;
import www.starcom.com.jualanpraktis.utils.Interface.ValueChangedListener;
import www.starcom.com.jualanpraktis.utils.NumberPicker;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> data;
    private Activity activity;
    private www.starcom.com.jualanpraktis.keranjang keranjang;
    private CartAdapter adapter;
    ArrayList<String> hargaItem2 = new ArrayList<>();
    int harga = 0;
    String pengiriman;
    private List<HargaItem> hargaDropshipper;
    int hargaItem;
    loginuser user;
    int stokFromServer;

    MainActivity mainActivity;

    public CartAdapter(Activity activity, ArrayList<HashMap<String, String>> data, keranjang keranjang) {
        this.data = data;
        this.activity = activity;
        this.keranjang = keranjang;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        LayoutInflater inflater = activity.getLayoutInflater();
        View view;
        if (keranjang == null) {
            view = inflater.inflate(R.layout.item_cart, viewGroup, false);
        } else {
            view = inflater.inflate(R.layout.item_cart_new, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        HashMap<String, String> item = new HashMap<>();
        item = this.data.get(i);


        if (keranjang == null) {
            viewHolder.card_item_jumlah.setVisibility(View.VISIBLE);
            viewHolder.number_picker.setVisibility(View.GONE);
            viewHolder.number_button.setVisibility(View.GONE);
            viewHolder.txt_sisa.setVisibility(View.GONE);

//            viewHolder.harga_dropshipper.setVisibility(View.GONE);

        } else {

            viewHolder.card_item_jumlah.setVisibility(View.GONE);
            viewHolder.number_picker.setVisibility(View.VISIBLE);
            viewHolder.number_button.setVisibility(View.GONE);
//            viewHolder.txt_sisa.setVisibility(View.VISIBLE);
//            viewHolder.harga_dropshipper.setVisibility(View.VISIBLE);
            viewHolder.txt_sisa.setText("Stok tersedia " + item.get("stok"));
            viewHolder.lblVariasi.setText(item.get("variasi"));

        }

        String stokServer = item.get("stok");

        if (stokServer==null){
            viewHolder.itemView.setEnabled(false);
        }


        TextDrawable drawable = TextDrawable.builder()
                .buildRound("" + item.get("jumlah"), Color.RED);
        viewHolder.card_item_jumlah.setImageDrawable(drawable);

        viewHolder.number_picker.setValue(Integer.parseInt(item.get("jumlah")));
        viewHolder.number_picker.setMin(1);
        String label = item.get("nama") + " - " + item.get("variasi");
        viewHolder.lbl_nama.setText(label);


        int hargaItem2 = Integer.parseInt(item.get("harga")) * Integer.parseInt(item.get("jumlah"));
        viewHolder.lbl_nominal.setText("Rp" + NumberFormat.getInstance().format(hargaItem2));

        int harga = Integer.parseInt(item.get("harga"));
        int jumlah = Integer.parseInt(item.get("jumlah"));

//        ArrayList<Integer> hargaItemArray = new ArrayList<>();
//
//        for (int a = 0; a<data.size(); a++) {
//
//            hargaItemArray.add(a, hargaItem);
//
//        }

//        Log.d("hargaItemArray", "onTextChanged: "+hargaItemArray);
//        Log.d("hargaItemArray", "jumlahData: "+hargaItemArray.size());


//        int hargaDropshipper = Integer.parseInt(viewHolder.harga_dropshipper.getText().toString());
//        viewHolder.harga_dropshipper.setText(FormatText.rupiahFormat(hargaDropshipper));

        String urlImage = "https://jualanpraktis.net/img/" + item.get("gambar");
        // Picasso.get().load(urlImage).into(viewHolder.gambar);


        Glide.with(activity.getApplicationContext())
                .load(item.get("gambar"))
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(150, 150).skipMemoryCache(false))
                .into(viewHolder.gambar);


//        viewHolder.btn_pilih.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ArrayList<String> hargaItem2 = new ArrayList<>();
//
//                    String harga = "";
//                    for (int b = 0; b<hargaItem2.size(); b++){
//                        harga += hargaItem2.get(b);
//                    }
//                    Toast.makeText(activity, "Berhasil Input Angka-"+harga+" Ke Array", Toast.LENGTH_SHORT).show();
//            }
//        });


        if (keranjang == null) {
            viewHolder.buang.setVisibility(View.GONE);
        } else {


//            for (int a = 0; a<data.size(); a++){
//                hargaItem2.add(viewHolder.harga_dropshipper.getText(item.get(a)).toString());
//            }
//
//            keranjang.getArrayHarga(hargaItem2);

//            for (int b = 0; b<data.size(); b++) {
            keranjang.getHargaDropshipper(viewHolder.harga_dropshipper.getText().toString());

//            viewHolder.harga_dropshipper.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                    if (actionId == EditorInfo.IME_ACTION_DONE) {
//                        // do your stuff here
//                        String harga = viewHolder.harga_dropshipper.getText().toString();
////                        keranjang.dataHarga.set(i, harga);
//                        keranjang.dataHarga.add(harga);
//                        Log.d("cartAdapter", "onEditorAction: "+harga);
//                        Log.d("dataHargaArray", "onEditorAction: "+keranjang.dataHarga);
//                    }
//                    return false;
//                }
//            });


            viewHolder.harga_dropshipper.addTextChangedListener(new TextWatcher() {

                private DecimalFormat df;
                private DecimalFormat dfnd;
                private boolean hasFractionalPart;

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int idd, int i1, int i2) {
//                        keranjang.dataHarga.add(charSequence.toString());
//                        Log.d("cartAdapter", "onTextChanged: "+charSequence.toString());
//                        Log.d("dataHargaArray", "onTextChanged: "+keranjang.dataHarga);

                    //hargaItem[i}

//                        ArrayList<Integer> hargaItemList = new ArrayList<>();
//
//                        hargaItemList.add(i, harga * jumlah);

                    String harga_jual = viewHolder.harga_dropshipper.getText().toString();

                    Log.d("hargaItem", "jumlahData: " + hargaItem);

                    Double hargaJual = 0.0;
                    DecimalFormat format = new DecimalFormat("0.#");
//                        NumberFormat format = NumberFormat.getInstance(Locale.US);

                    if (!harga_jual.equals("")) {
//                            Double harga_jual_double = Double.parseDouble(harga_jual);
                        int sdk = Build.VERSION.SDK_INT;
                        Log.d("checkSDK", "sdk int: " + sdk);

                        String str = "";

//                            if (strTitik.equals(".")) {
//                                str = harga_jual.replace(".", "");
//                            }else if (strKoma.equals(",")){
//                                str = harga_jual.replace(",", "");
//                            }

                        str = harga_jual.replace(".", "").replace(",", "");
//                            str = harga_jual.replace(",", "");
//                            String strKoma = harga_jual.replace(",", "");
//                            int intValue = (int) Math.round(harga_jual_double);

                        hargaJual = Double.parseDouble(str);

                    }

                    if (i != 0 && keranjang.dataHarga.size()<1) {

                        Toast.makeText(activity, "Masukan Harga Jual Dari data yg pertama terlebih dahulu", Toast.LENGTH_SHORT).show();


                    } else {

                        keranjang.dataHarga.add(i, String.valueOf(format.format(hargaJual)));

                    }


                    Log.d("checkValue", "onTextChanged: " + keranjang.dataHarga);

                    if (hargaJual < hargaItem2) {
                        viewHolder.harga_dropshipper.setError("Harga Jual Tidak Boleh Di bawah Harga Item");
                        keranjang.btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(activity, "Harga Jual Tidak Boleh Di bawah Harga Item", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (keranjang.status_user.equals("0")) {

                        keranjang.btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(activity, "Anda Belum Melengkapi Data Diri", Toast.LENGTH_SHORT).show();
                                activity.startActivity(new Intent(activity, EditAkunActivity.class));
                            }
                        });

                    }else if (keranjang.status_user.equals("2")) {
                        keranjang.btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(activity, "Anda belum melengkapi data bank anda, silahkan lengkapi data bank anda", Toast.LENGTH_SHORT).show();
                                activity.startActivity(new Intent(activity, DetailRekeningBankActivity.class));
                            }
                        });
                    } else {
                        keranjang.btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                keranjang.proccess();
                            }
                        });
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                    df = new DecimalFormat("###,###,###,###.##");
                    df.setDecimalSeparatorAlwaysShown(true);
                    dfnd = new DecimalFormat("##,###,###,###");
                    hasFractionalPart = false;

                    viewHolder.harga_dropshipper.removeTextChangedListener(this);

                    try {
                        int inilen, endlen;
                        inilen = viewHolder.harga_dropshipper.getText().length();

                        String v = editable.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
                        Number n = df.parse(v);
                        int cp = viewHolder.harga_dropshipper.getSelectionStart();
                        if (hasFractionalPart) {
                            viewHolder.harga_dropshipper.setText(df.format(n));
                        } else {
                            viewHolder.harga_dropshipper.setText(dfnd.format(n));
                        }
                        endlen = viewHolder.harga_dropshipper.getText().length();
                        int sel = (cp + (endlen - inilen));
                        if (sel > 0 && sel <= viewHolder.harga_dropshipper.getText().length()) {
                            viewHolder.harga_dropshipper.setSelection(sel);
                        } else {
                            // place cursor at the end?
                            viewHolder.harga_dropshipper.setSelection(viewHolder.harga_dropshipper.getText().length() - 1);
                        }
                    } catch (NumberFormatException nfe) {
                        // do nothing?
                    } catch (ParseException e) {
                        // do nothing?
                    }

                    viewHolder.harga_dropshipper.addTextChangedListener(this);

//                        try {
//
//                            if (!viewHolder.harga_dropshipper.getText().toString().isEmpty()) {
//
//                                ArrayList<String> dataArray = new ArrayList<>(data.size());
//                                keranjang.dataHarga = new ArrayList<>(data.size());
//
//                                    String data1 = editable.toString();
//                                    String data2 = editable.toString();
//
//                                    keranjang.dataHarga.add(data1);
//                                    keranjang.dataHarga.add(data2);
//                                Log.d("dataHargaArray", "onTextChanged: "+keranjang.dataHarga);
//                            }
//
//                        } catch (NullPointerException e) {
//
//
//                        }

                }
            });

        }


        final HashMap<String, String> finalItem = item;
        viewHolder.buang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialAlertDialogBuilder(activity)
                        .setTitle("Hapus Item")
                        .setMessage("Anda yakin ingin menghapus pesanan ini?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteItem(finalItem.get("nomor"));
                                MainActivity.getInstance().getCountCart();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

            }
        });
        int stok = 0;
        String stokFromServer ="";
        if (keranjang != null) {
//            Log.d("getStock", "onBindViewHolder: "+stokFromServer);
            if (stokServer!=null) {
                stok = Integer.parseInt(stokServer);
            }
        }

        final int finalStok = stok;
        viewHolder.number_picker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                if (finalStok < value) {
                    viewHolder.number_picker.setValue(Integer.parseInt(finalItem.get("jumlah")));
                    //  finalItem.put("jumlah",String.valueOf(oldValue));
                    Toast.makeText(activity, "Pesanan tidak boleh kurang dari satu", Toast.LENGTH_SHORT).show();
                } else {

                    finalItem.put("jumlah", String.valueOf(value));
                    hargaItem = Integer.parseInt(finalItem.get("harga")) * Integer.parseInt(finalItem.get("jumlah"));
                    viewHolder.lbl_nominal.setText(FormatText.rupiahFormat(hargaItem));
                    keranjang.onChangeData();
                    notifyDataSetChanged();
                }
            }
        });


        viewHolder.number_button.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if (finalStok < newValue) {
                    viewHolder.number_button.setNumber(finalItem.get("jumlah"));
                    //  finalItem.put("jumlah",String.valueOf(oldValue));
                    Toast.makeText(activity, "Pesanan tidak boleh kurang dari satu", Toast.LENGTH_SHORT).show();
                } else {

                    finalItem.put("jumlah", String.valueOf(newValue));
                    hargaItem = Integer.parseInt(finalItem.get("harga")) * Integer.parseInt(finalItem.get("jumlah"));
                    viewHolder.lbl_nominal.setText(FormatText.rupiahFormat(hargaItem));
                    keranjang.onChangeData();
                    notifyDataSetChanged();
                }
            }
        });


    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }


    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }


    /**
     * View holder to display each RecylerView item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView lbl_nama, lbl_status, lbl_nominal, lbl_tanggal, txt_sisa, txtHargaDibawah, lblVariasi;
        private ImageView gambar, buang, card_item_jumlah;
        private ElegantNumberButton number_button;
        private NumberPicker number_picker;
        public EditText harga_dropshipper;
        private CardView cardPilihanPengiriman;


        public ViewHolder(View view) {
            super(view);
            lbl_nama = view.findViewById(R.id.card_item_nama);
            lbl_nominal = view.findViewById(R.id.card_item_harga);
            txt_sisa = view.findViewById(R.id.txt_sisa);
            gambar = view.findViewById(R.id.gambar);
            number_button = view.findViewById(R.id.number_button);
            number_picker = view.findViewById(R.id.number_picker);
            buang = view.findViewById(R.id.buang);
            card_item_jumlah = view.findViewById(R.id.card_item_jumlah);
            harga_dropshipper = view.findViewById(R.id.harga_dropshipper);
            lblVariasi = view.findViewById(R.id.card_item_variasi);
            txtHargaDibawah = view.findViewById(R.id.text_harga_jual_dibawah);
        }
    }

    void deleteItem(String nomor) {
        AndroidNetworking.initialize(activity);
        AndroidNetworking.post("https://jualanpraktis.net/android/delete_item.php")
                .addBodyParameter("nomor", nomor)
                .setTag(activity)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("Berhasil Hapus")) {
                            //  keranjang.getCart();w
//                            activity.overridePendingTransition(0, 0);
//                            activity.startActivity(activity.getIntent().setFlags(activity.getIntent().FLAG_ACTIVITY_NO_ANIMATION));
//                            activity.overridePendingTransition(0, 0);
                            Toast.makeText(activity, "Berhasil Hapus", Toast.LENGTH_SHORT).show();
                            keranjang.getCart();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


}

