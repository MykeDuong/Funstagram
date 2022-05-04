package com.minhsoumay.funstagram.Runnable;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

public class PlaybackRunnable implements Runnable {
    Context context;
    AppCompatActivity invokerActivity;
    int res;

    /**
     * The constructor method for the class. It takes the invoking activity, app context,
     * delay time and resource index as parameters.
     * @param activity      The invoking activity
     * @param context       The context of the app
     * @param res           The resource index of the sound file.
     */
    public PlaybackRunnable(AppCompatActivity activity, Context context, int res) {
        this.context = context;
        invokerActivity = activity;
        this.res = res;
    }

    /**
     * This method is use to run this runnable object. It will wait depending on the
     * delay field and play the audio by calling playAudio().
     */
    @Override
    public void run() {
        playAudio(res);
    }

    /**
     * This method is used to play the audio with the resource from the resource
     * id given as its parameter. It will use MediaPlayer to do so.
     * @param res   The resource of the sound file.
     */
    private void playAudio(int res) {
        MediaPlayer player = MediaPlayer.create(context,
                res);
        player.setLooping(false); // don’t loop
        player.setVolume(1, 1);

        System.out.println("Start playing...");
        player.start();
    }
}
