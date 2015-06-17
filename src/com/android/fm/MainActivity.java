package com.android.fm;

import android.app.Activity;
import android.content.Context;
import android.hardware.fmradio.FmReceiverJNI;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.media.AudioSystem;
import com.android.fm.R;


public class MainActivity extends Activity {

    private AudioManager mAudioManager;
    private static final String LOGTAG = "FMTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.v(LOGTAG, "AudioFocus: received AUDIOFOCUS_LOSS, turning FM off");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.v(LOGTAG, "AudioFocus: received AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");

                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Log.v(LOGTAG, "AudioFocus: received AUDIOFOCUS_LOSS_TRANSIENT");

                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.v(LOGTAG, "AudioFocus: received AUDIOFOCUS_GAIN");

                    break;
                default:
                    Log.e(LOGTAG, "Unknown audio focus change code " + focusChange);
            }
        }
    };


    public void disableFM(View view) {

        Log.d("FMTest", "DisableFM");
        mAudioManager.abandonAudioFocus(mAudioFocusListener);
        FmReceiverJNI.closeFdNative(0);
        AudioSystem.setDeviceConnectionState(AudioSystem.DEVICE_OUT_FM, AudioSystem.DEVICE_STATE_UNAVAILABLE, "");
    }

    public void enableFM(View view) {

        Log.d("FMTest", "EnableFM");

        mAudioManager.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        FmReceiverJNI.acquireFdNative("");
        AudioSystem.setDeviceConnectionState(AudioSystem.DEVICE_OUT_FM, AudioSystem.DEVICE_STATE_AVAILABLE, "");
    }

}
