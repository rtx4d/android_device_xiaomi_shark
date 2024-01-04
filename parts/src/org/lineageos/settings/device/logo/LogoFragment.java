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

package org.lineageos.settings.device.logo;

import android.content.res.Resources;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.ImageButton;
import android.widget.Switch;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;
import androidx.preference.SeekBarPreference;
import androidx.preference.ListPreference;

import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

import org.lineageos.settings.device.R;

import org.lineageos.settings.device.utils.SettingsUtils;

import org.lineageos.settings.device.logo.LogoUtil;

public class LogoFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener, OnMainSwitchChangeListener,
        SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String KEY_LOGO_ENABLE = "logo_control_enable";
    public static final String KEY_LOGO_MODE = "logo_control_mode";
    public static final String KEY_LOGO_MODE_MANUAL_RED = "logo_control_manual_red";
    public static final String KEY_LOGO_MODE_MANUAL_GREEN = "logo_control_manual_green";
    public static final String KEY_LOGO_MODE_MANUAL_BLUE = "logo_control_manual_blue";
    public static final String KEY_LOGO_MODE_BREATH = "logo_control_breath";
    public static final String KEY_LOGO_MODE_MONO_BREATH = "logo_control_mono_breath";

    public static final int LOGO_MODE_BREATH = 1;
    public static final int LOGO_MODE_MANUAL = 2;
    public static final int LOGO_MODE_MONO_BREATH = 3;
    private static final int LOGO_DISABLED_VALUE = 0;
    private static final int LOGO_MIN_VALUE = 1;
    private static final int LOGO_MAX_VALUE = 255;

    private ImageButton mLogoMonoBreathRed;
    private ImageButton mLogoMonoBreathGreen;
    private ImageButton mLogoMonoBreathBlue;
    private MainSwitchPreference mSwitchBar;
    private ListPreference mLogoControlMode;
    private SeekBarPreference mLogoManualBarRed;
    private SeekBarPreference mLogoManualBarGreen;
    private SeekBarPreference mLogoManualBarBlue;

    private void setSlidersVisibility(boolean state) {
        mLogoManualBarRed.setVisible(state);
        mLogoManualBarGreen.setVisible(state);
        mLogoManualBarBlue.setVisible(state);
    }

    private void setButtonsVisibility(int value) {
        mLogoMonoBreathRed.setVisibility(View.GONE);
        mLogoMonoBreathGreen.setVisibility(View.GONE);
        mLogoMonoBreathBlue.setVisibility(View.GONE);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.logo);

        mSwitchBar = (MainSwitchPreference) findPreference(KEY_LOGO_ENABLE);
        mSwitchBar.setChecked(SettingsUtils.getEnabled(getActivity(), KEY_LOGO_ENABLE));
        mSwitchBar.addOnSwitchChangeListener(this);

        mLogoControlMode = (ListPreference) findPreference(KEY_LOGO_MODE);

        mLogoManualBarRed = (SeekBarPreference) findPreference(KEY_LOGO_MODE_MANUAL_RED);
        mLogoManualBarRed.setValue(SettingsUtils.getInt(getActivity(), KEY_LOGO_MODE_MANUAL_RED, 1));
        mLogoManualBarGreen = (SeekBarPreference) findPreference(KEY_LOGO_MODE_MANUAL_GREEN);
        mLogoManualBarGreen.setValue(SettingsUtils.getInt(getActivity(), KEY_LOGO_MODE_MANUAL_GREEN, 1));
        mLogoManualBarBlue = (SeekBarPreference) findPreference(KEY_LOGO_MODE_MANUAL_BLUE);
        mLogoManualBarBlue.setValue(SettingsUtils.getInt(getActivity(), KEY_LOGO_MODE_MANUAL_BLUE, 1));

        mLogoManualBarRed.setOnPreferenceChangeListener(this);
        mLogoManualBarGreen.setOnPreferenceChangeListener(this);
        mLogoManualBarBlue.setOnPreferenceChangeListener(this);

        if (mLogoControlMode.getValue() == null) {
            mLogoControlMode.setValue("1");
        }

        final int mode = Integer.parseInt((String) mLogoControlMode.getValue());
        if (mode == LOGO_MODE_BREATH) {
            mLogoControlMode.setSummary(getResources().getString(R.string.logo_control_breath_title));
            setButtonsVisibility(View.INVISIBLE);
            setSlidersVisibility(false);
        } else if (mode == LOGO_MODE_MANUAL) {
            mLogoControlMode.setSummary(getResources().getString(R.string.logo_control_manual_title));
            setButtonsVisibility(View.INVISIBLE);
            setSlidersVisibility(true);
        } else if (mode == LOGO_MODE_MONO_BREATH) {
            mLogoControlMode.setSummary(getResources().getString(R.string.logo_control_mono_breath_title));
            setButtonsVisibility(View.VISIBLE);
            setSlidersVisibility(false);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mLogoMonoBreathRed = view.findViewById(R.id.BreathButtonRed);
        mLogoMonoBreathGreen = view.findViewById(R.id.BreathButtonGreen);
        mLogoMonoBreathBlue = view.findViewById(R.id.BreathButtonBlue);
        
        mLogoMonoBreathRed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LogoUtil.turnOff();
                LogoUtil.setBlinkRed(true);
            }
        });
        mLogoMonoBreathGreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LogoUtil.turnOff();
                LogoUtil.setBlinkGreen(true);
            }
        });
        mLogoMonoBreathBlue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LogoUtil.turnOff();
                LogoUtil.setBlinkBlue(true);
            }
        });
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
    public void onSwitchChanged(Switch switchView, boolean enabled) {
        SettingsUtils.setEnabled(getActivity(), KEY_LOGO_ENABLE, enabled);

        LogoUtil.turnOff();
        if (!enabled) return;

        int mode = Integer.parseInt((String) mLogoControlMode.getValue());
        if (mode == LOGO_MODE_BREATH) {
            enableBreathingMode();
        } else if (mode == LOGO_MODE_MANUAL) {
            enableManualMode();
        } else if (mode == LOGO_MODE_MONO_BREATH) {
            enableMonoBreathingMode();
        }
    }

    private void enableManualMode() {
        mLogoControlMode.setSummary(getResources().getString(R.string.logo_control_manual_title));
        setButtonsVisibility(View.INVISIBLE);
        setSlidersVisibility(true);

        final int r = SettingsUtils.getInt(getActivity(), KEY_LOGO_MODE_MANUAL_RED, 1);
        final int g = SettingsUtils.getInt(getActivity(), KEY_LOGO_MODE_MANUAL_GREEN, 1);
        final int b = SettingsUtils.getInt(getActivity(), KEY_LOGO_MODE_MANUAL_BLUE, 1);
        LogoUtil.setRGBStill(r, g, b);
    }

    private void enableBreathingMode() {
        mLogoControlMode.setSummary(getResources().getString(R.string.logo_control_breath_title));
        setButtonsVisibility(View.INVISIBLE);
        setSlidersVisibility(false);
        LogoUtil.enableBreathingEffect();
    }

    private void enableMonoBreathingMode() {
        mLogoControlMode.setSummary(getResources().getString(R.string.logo_control_mono_breath_title));
        setButtonsVisibility(View.VISIBLE);
        setSlidersVisibility(false);
        LogoUtil.enableMonoBreathingEffect();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        if (KEY_LOGO_MODE_MANUAL_RED.equals(key)) {
            final int r = (Integer)value;
            SettingsUtils.putInt(getActivity(), KEY_LOGO_MODE_MANUAL_RED, r);
            LogoUtil.setStillRed(r);
        } else if (KEY_LOGO_MODE_MANUAL_GREEN.equals(key)) {
            final int g = (Integer)value;
            SettingsUtils.putInt(getActivity(), KEY_LOGO_MODE_MANUAL_GREEN, g);
            LogoUtil.setStillGreen(g);
        } else if (KEY_LOGO_MODE_MANUAL_BLUE.equals(key)) {
            final int b = (Integer)value;
            SettingsUtils.putInt(getActivity(), KEY_LOGO_MODE_MANUAL_BLUE, b);
            LogoUtil.setStillBlue(b);
        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!KEY_LOGO_MODE.equals(key)) return;
        int mode = Integer.parseInt(sharedPreferences.getString(key, String.valueOf(LOGO_MODE_BREATH)));
        mLogoControlMode.setValue(String.valueOf(mode));

        // Reset everything before switching mode
        LogoUtil.turnOff();

        if (mode == LOGO_MODE_BREATH) {
            enableBreathingMode();
        } else if (mode == LOGO_MODE_MANUAL) {
            enableManualMode();
        } else if (mode == LOGO_MODE_MONO_BREATH) {
            enableMonoBreathingMode();
        }
    }
}
