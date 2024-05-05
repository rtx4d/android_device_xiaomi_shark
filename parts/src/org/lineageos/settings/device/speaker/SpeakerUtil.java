package org.lineageos.settings.device.speaker;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.AudioPlaybackConfiguration;
import android.media.AudioDeviceInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;

import org.lineageos.settings.device.speaker.SpeakerFragment;
import org.lineageos.settings.device.utils.SettingsUtils;

public class SpeakerUtil {
    private static final String TAG = "SpeakerUtil";
    private static final boolean DEBUG = false;

    private static AudioManager audioManager = null;
    private static SharedPreferences sharedPreferences = null;
    private static Handler handler = new Handler();

    public static boolean enabled = false;
    
    public static void initInjector(Context context) {
        enabled = SettingsUtils.getEnabled(context, SpeakerFragment.KEY_SPEAKER_TWEAKS_ENABLE);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        audioManager.registerAudioPlaybackCallback(new AudioManager.AudioPlaybackCallback() {
            @Override
            public void onPlaybackConfigChanged(List<AudioPlaybackConfiguration> configs) {
                super.onPlaybackConfigChanged(configs);

                for (AudioPlaybackConfiguration config : configs) {
                    if (config == null) continue;
                    if (config.getAudioDeviceInfo() == null) continue;
                    if (config.getAudioDeviceInfo().getType() != AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
                        continue;
                    } else {
                        updateParameters(context);
                        break;
                    }
                }
            }
        }, handler);
    }

    public static void updateParameters(Context context) {
        if (!enabled) return;

        StringBuilder builder = new StringBuilder("eq=");
        builder.append(String.valueOf(SettingsUtils.getInt(context, SpeakerFragment.KEY_SPEAKER_EQ_BASE + "0", 3)));
        builder.append(String.valueOf(SettingsUtils.getInt(context, SpeakerFragment.KEY_SPEAKER_EQ_BASE + "1", 3)));
        builder.append(String.valueOf(SettingsUtils.getInt(context, SpeakerFragment.KEY_SPEAKER_EQ_BASE + "2", 3)));
        builder.append(String.valueOf(SettingsUtils.getInt(context, SpeakerFragment.KEY_SPEAKER_EQ_BASE + "3", 3)));
        builder.append(String.valueOf(SettingsUtils.getInt(context, SpeakerFragment.KEY_SPEAKER_EQ_BASE + "4", 3)));
        builder.append(";gamemode=");
        builder.append(sharedPreferences.getString(SpeakerFragment.KEY_SPEAKER_GAMEMODE, SpeakerFragment.GAMEMODE_DISABLED));
        String parameters = builder.toString();

        if (DEBUG) Log.d(TAG, "Setting speaker parameters to " + parameters);
        audioManager.setParameters(parameters);
    }
}
