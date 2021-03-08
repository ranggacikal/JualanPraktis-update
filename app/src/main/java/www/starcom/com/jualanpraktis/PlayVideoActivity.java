package www.starcom.com.jualanpraktis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayVideoActivity extends YouTubeBaseActivity {

    String id_video, nama_video, waktu_video;

    ImageView imgPlay, imgThumbnail, imgBack;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    TextView txtNama, txtWaktu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        youTubePlayerView = findViewById(R.id.viewPlayVideo);
        imgPlay = findViewById(R.id.img_play_video_panduan);
        imgThumbnail = findViewById(R.id.img_thumbnail);
        txtNama = findViewById(R.id.text_nama_video);
        txtWaktu = findViewById(R.id.text_waktu_video);
        imgBack = findViewById(R.id.img_back_play);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        id_video = getIntent().getStringExtra("id_video");
        nama_video = getIntent().getStringExtra("nama_video");
        waktu_video = getIntent().getStringExtra("waktu_video");

        String url = "https://img.youtube.com/vi/"+id_video+"/0.jpg";

        Glide.with(PlayVideoActivity.this)
                .load(url)
                .into(imgThumbnail);

        txtNama.setText(nama_video);
        txtWaktu.setText(waktu_video);

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(id_video);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerView.initialize("AIzaSyDNd1vGpd_aKAhrQIN6xJ5dBiG0N6yAsN0", onInitializedListener);
                imgPlay.setVisibility(View.GONE);
                youTubePlayerView.setVisibility(View.VISIBLE);
                imgThumbnail.setVisibility(View.GONE);

            }
        });

    }
}