package www.starcom.com.jualanpraktis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.starcom.com.jualanpraktis.Login.SharedPrefManager;
import www.starcom.com.jualanpraktis.adapter.PeriodeAdapter;
import www.starcom.com.jualanpraktis.api.ConfigRetrofit;
import www.starcom.com.jualanpraktis.feature.akun.PenghasilanSayaActivity;
import www.starcom.com.jualanpraktis.model_retrofit.DataItem;
import www.starcom.com.jualanpraktis.model_retrofit.ResponseDataVideo;

public class WelcomePageActivity extends YouTubeBaseActivity {

    ImageView imgPlay;
    private YouTubePlayerView youTubePlayerView;
    ImageView youTubeThumbnailView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    String getVideo;
    CardView cardTontonNanti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

//        getDataVideo();
        getVideo();
        youTubePlayerView = findViewById(R.id.viewYoutubeWelcome);
        imgPlay = findViewById(R.id.img_play_video);
        cardTontonNanti = findViewById(R.id.card_tonton_nanti);
        youTubeThumbnailView = findViewById(R.id.imgThumbnail);

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(getVideo);
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubeThumbnailView.setVisibility(View.GONE);
                youTubePlayerView.setVisibility(View.VISIBLE);
                youTubePlayerView.initialize("AIzaSyDNd1vGpd_aKAhrQIN6xJ5dBiG0N6yAsN0", onInitializedListener);
                imgPlay.setVisibility(View.GONE);
                Log.d("dataVideo", "onClick: "+getVideo);
            }
        });

        cardTontonNanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), login.class);
                    intent.putExtra("tab",0);
                    startActivity(intent);
                    finish();
            }
        });
    }

    private void getDataVideo() {

        ConfigRetrofit.service.getDataVideo().enqueue(new Callback<ResponseDataVideo>() {
            @Override
            public void onResponse(Call<ResponseDataVideo> call, Response<ResponseDataVideo> response) {
                if (response.isSuccessful()){

                    List<DataItem> id_video = response.body().getData();

                    if (!id_video.isEmpty()){

                        getVideo = String.valueOf(response.body().getData().get(0).getIdVideo());

                    }else{
                        Toast.makeText(WelcomePageActivity.this, "Data Tidak Ada", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(WelcomePageActivity.this, "Data Tidak Ada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataVideo> call, Throwable t) {
                Toast.makeText(WelcomePageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getVideo(){
        AndroidNetworking.get("https://jualanpraktis.net/android/vid-welcome.php")
                .setTag(WelcomePageActivity.this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        String id_video = "";
                        try {

                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                HashMap<String,String> data = new HashMap<>();
                                getVideo = obj.getString("id_video");
                                id_video = obj.getString("id_video");
                            }

                            String url = "https://img.youtube.com/vi/"+id_video+"/0.jpg";

                            Glide.with(WelcomePageActivity.this)
                                    .load(url)
                                    .error(R.drawable.logo_jualan_merah)
                                    .into(youTubeThumbnailView);

                            Log.d("videoData", "onResponse: "+url);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(WelcomePageActivity.this, error.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}