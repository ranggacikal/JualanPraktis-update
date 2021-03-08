package www.starcom.com.jualanpraktis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.Login.loginuser;
import www.starcom.com.jualanpraktis.feature.akun.UbahKataSandiActivity;

public class SplashActivity extends AppCompatActivity {

    loginuser user ;


    private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AndroidNetworking.initialize(SplashActivity.this.getApplicationContext());
        user = SharedPrefManager.getInstance(SplashActivity.this).getUser();

        Context pContext;
        pContext = this;


        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.d("HashKey", "key: "+hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("SplashActivity", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("SplashActivity", "printHashKey()", e);
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPrefManager.getInstance(SplashActivity.this).isLoggedIn()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("tab",0);
                    startActivity(intent);
                    simpanLogUser();
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(), WelcomePageActivity.class);
                    intent.putExtra("tab",0);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void simpanLogUser() {

        String host = "https://jualanpraktis.net/android/log_user.php";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());


        Map<String, String> params = new HashMap<String, String>();
        params.put("customer", user.getId());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post(host)
                .addBodyParameter(params)
                .setTag(SplashActivity.this)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(SplashActivity.this, "Berhasil Save Log", Toast.LENGTH_SHORT).show();
                        finish();

                        try {
                            Toast.makeText(getApplicationContext(), response.getString("response"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void onError(ANError anError) {
//                        Toast.makeText(getApplicationContext(), "Gagal Save log", Toast.LENGTH_SHORT).show();
                        Log.d("readIdUser", "onError: "+user.getId());
                    }

                });

    }
}
