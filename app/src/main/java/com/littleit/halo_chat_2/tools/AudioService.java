package com.littleit.halo_chat_2.tools;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

public class AudioService {
    private Context context;
    private MediaPlayer tmpmediaPlayer;
    private OnPlayCallBack onPlayCallBack;

    public AudioService(Context context) {
        this.context = context;
    }

    public void playAudioFromUri(String uri, OnPlayCallBack onPlayCallBack){
        if (tmpmediaPlayer!=null){
            tmpmediaPlayer.stop();
        }

        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(uri);
            mediaPlayer.prepare();
            mediaPlayer.start();

            tmpmediaPlayer = mediaPlayer;
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                onPlayCallBack.onFinished();
            }
        });

    }

    public interface OnPlayCallBack{
        void onFinished();
    }
}
