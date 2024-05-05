/*
 * Copyright (C) 2020 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings.device.speaker;

import android.content.res.Resources;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.CompoundButton;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;
import androidx.preference.SeekBarPreference;
import androidx.preference.ListPreference;

import com.android.settingslib.widget.MainSwitchPreference;

import org.lineageos.settings.device.R;

import org.lineageos.settings.device.utils.SettingsUtils;

import org.lineageos.settings.device.speaker.SpeakerUtil;

public class SpeakerFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener, CompoundButton.OnCheckedChangeListener,
        SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String KEY_SPEAKER_TWEAKS_ENABLE = "speaker_tweaks_enable";
    public static final String KEY_SPEAKER_EQ_BASE = "speaker_control_eq_band_";
    public static final String KEY_SPEAKER_GAMEMODE = "speaker_gamemode_val";

    public static final String EQ_DEFAULT = "3";
    public static final String GAMEMODE_DISABLED = "0";
    public static final String GAMEMODE_DEFAULT = "1";
    public static final String GAMEMODE_EATENCHICKEN = "2";

    private MainSwitchPreference mSwitchBar;
    private ListPreference mGameMode;
    private SeekBarPreference mEqBand0;
    private SeekBarPreference mEqBand1;
    private SeekBarPreference mEqBand2;
    private SeekBarPreference mEqBand3;
    private SeekBarPreference mEqBand4;

    private void updateGameModeSummary() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String val = sharedPreferences.getString(KEY_SPEAKER_GAMEMODE, GAMEMODE_DISABLED);
        if (val.equals("0")) {
            mGameMode.setSummary(R.string.speaker_control_gamemode_0);
        } else if (val.equals("1")) {
            mGameMode.setSummary(R.string.speaker_control_gamemode_1);
        } else if (val.equals("2")) {
            mGameMode.setSummary(R.string.speaker_control_gamemode_2);
        } else {
            mGameMode.setSummary(null);
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.speaker);

        mSwitchBar = (MainSwitchPreference) findPreference(KEY_SPEAKER_TWEAKS_ENABLE);
        mSwitchBar.setChecked(SettingsUtils.getEnabled(getActivity(), KEY_SPEAKER_TWEAKS_ENABLE));
        mSwitchBar.addOnSwitchChangeListener(this);

        mGameMode = (ListPreference) findPreference(KEY_SPEAKER_GAMEMODE);

        mEqBand0 = (SeekBarPreference) findPreference(KEY_SPEAKER_EQ_BASE + "0");
        mEqBand1 = (SeekBarPreference) findPreference(KEY_SPEAKER_EQ_BASE + "1");
        mEqBand2 = (SeekBarPreference) findPreference(KEY_SPEAKER_EQ_BASE + "2");
        mEqBand3 = (SeekBarPreference) findPreference(KEY_SPEAKER_EQ_BASE + "3");
        mEqBand4 = (SeekBarPreference) findPreference(KEY_SPEAKER_EQ_BASE + "4");

        mEqBand0.setValue(SettingsUtils.getInt(getActivity(), KEY_SPEAKER_EQ_BASE + "0", 3));
        mEqBand1.setValue(SettingsUtils.getInt(getActivity(), KEY_SPEAKER_EQ_BASE + "1", 3));
        mEqBand2.setValue(SettingsUtils.getInt(getActivity(), KEY_SPEAKER_EQ_BASE + "2", 3));
        mEqBand3.setValue(SettingsUtils.getInt(getActivity(), KEY_SPEAKER_EQ_BASE + "3", 3));
        mEqBand4.setValue(SettingsUtils.getInt(getActivity(), KEY_SPEAKER_EQ_BASE + "4", 3));

        mEqBand0.setOnPreferenceChangeListener(this);
        mEqBand1.setOnPreferenceChangeListener(this);
        mEqBand2.setOnPreferenceChangeListener(this);
        mEqBand3.setOnPreferenceChangeListener(this);
        mEqBand4.setOnPreferenceChangeListener(this);

        if (mGameMode.getValue() == null) {
            mGameMode.setValue(GAMEMODE_DISABLED);
        }

        updateGameModeSummary();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean enabled) {
        SettingsUtils.setEnabled(getActivity(), KEY_SPEAKER_TWEAKS_ENABLE, enabled);

        SpeakerUtil.enabled = enabled;
        SpeakerUtil.updateParameters(getActivity());
        updateGameModeSummary();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        SettingsUtils.putInt(getActivity(), key, (Integer)value);
        SpeakerUtil.updateParameters(getActivity());
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!KEY_SPEAKER_GAMEMODE.equals(key)) return;
        String mode = sharedPreferences.getString(key, GAMEMODE_DISABLED);
        mGameMode.setValue(mode);
        updateGameModeSummary();
        SpeakerUtil.updateParameters(getActivity());
    }
}
