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
import android.os.Handler;
import android.os.Parcel;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.Switch;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;
import androidx.preference.SeekBarPreference;
import androidx.preference.ListPreference;

import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

import org.lineageos.settings.device.R;

import org.lineageos.settings.device.utils.FileUtils;
import org.lineageos.settings.device.utils.SettingsUtils;

public class LogoFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener, OnMainSwitchChangeListener,
        SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String KEY_LOGO_ENABLE = "logo_control_enable";
    public static final String KEY_LOGO_MODE = "logo_control_mode";
    public static final String KEY_LOGO_MODE_MANUAL_RED = "logo_control_manual_red";
    public static final String KEY_LOGO_MODE_MANUAL_GREEN = "logo_control_manual_green";
    public static final String KEY_LOGO_MODE_MANUAL_BLUE = "logo_control_manual_blue";
    public static final String KEY_LOGO_MODE_BREATH = "logo_control_breath";

    public static final String RED_LED = "/sys/rgb/leds/green_1/brightness";
    public static final String GREEN_LED = "/sys/rgb/leds/red_1/brightness";
    public static final String BLUE_LED = "/sys/rgb/leds/blue_1/brightness";

    public static final String RED_LED_BLINK = "/sys/rgb/leds/green_1/blink";
    public static final String GREEN_LED_BLINK = "/sys/rgb/leds/red_1/blink";
    public static final String BLUE_LED_BLINK = "/sys/rgb/leds/blue_1/blink";

    public static final int LOGO_MODE_BREATH = 1;
    public static final int LOGO_MODE_MANUAL = 2;
    private static final int LOGO_DISABLED_VALUE = 0;
    private static final int LOGO_MIN_VALUE = 1;
    private static final int LOGO_MAX_VALUE = 255;

    private MainSwitchPreference mSwitchBar;
    private ListPreference mLogoControlMode;
    private SeekBarPreference mLogoManualBarRed;
    private SeekBarPreference mLogoManualBarGreen;
    private SeekBarPreference mLogoManualBarBlue;

    private String summary = null;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        int logoModeValue;
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

        if (mLogoControlMode.getValue() != null) {
            logoModeValue = Integer.parseInt((String) mLogoControlMode.getValue());

            if (logoModeValue == LOGO_MODE_BREATH) {
                summary = getResources().getString(R.string.logo_control_breath_title);
                mLogoManualBarRed.setVisible(false);
                mLogoManualBarGreen.setVisible(false);
                mLogoManualBarBlue.setVisible(false);
            } else {
                summary = getResources().getString(R.string.logo_control_manual_title);
                mLogoManualBarRed.setVisible(true);
                mLogoManualBarGreen.setVisible(true);
                mLogoManualBarBlue.setVisible(true);
            }
        }

        mLogoControlMode.setSummary(summary);
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
        int logoModeValue = Integer.parseInt((String) mLogoControlMode.getValue());
        String manualRedValue;
        String manualGreenValue;
        String manualBlueValue;

        if (enabled) {
            SettingsUtils.setEnabled(getActivity(), KEY_LOGO_ENABLE, enabled);
            if (logoModeValue == LOGO_MODE_BREATH) {
                FileUtils.writeLine(RED_LED_BLINK, "1");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                FileUtils.writeLine(GREEN_LED_BLINK, "1");
                    }
                }, 1000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                FileUtils.writeLine(BLUE_LED_BLINK, "1");
                    }
                }, 2000);
            } else if (logoModeValue == LOGO_MODE_MANUAL) {
                manualRedValue = String.valueOf(SettingsUtils.getInt(getActivity(), KEY_LOGO_MODE_MANUAL_RED, 1));
                FileUtils.writeLine(RED_LED, manualRedValue);
                manualGreenValue = String.valueOf(SettingsUtils.getInt(getActivity(), KEY_LOGO_MODE_MANUAL_GREEN, 1));
                FileUtils.writeLine(GREEN_LED, manualGreenValue);
                manualBlueValue = String.valueOf(SettingsUtils.getInt(getActivity(), KEY_LOGO_MODE_MANUAL_BLUE, 1));
                FileUtils.writeLine(BLUE_LED, manualBlueValue);
            }
        } else {
            FileUtils.writeLine(RED_LED_BLINK, "0");
            FileUtils.writeLine(GREEN_LED_BLINK, "0");
            FileUtils.writeLine(BLUE_LED_BLINK, "0");
            FileUtils.writeLine(RED_LED, "0");
            FileUtils.writeLine(GREEN_LED, "0");
            FileUtils.writeLine(BLUE_LED, "0");
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        String intValueStr;
        int intValue;

        if (KEY_LOGO_MODE_MANUAL_RED.equals(key)) {
            intValue = (Integer) value;
            intValueStr = String.valueOf(intValue);
            mLogoManualBarRed.setValue(intValue);
            SettingsUtils.putInt(getActivity(), KEY_LOGO_MODE_MANUAL_RED, intValue);
            FileUtils.writeLine(RED_LED, intValueStr);
        }

        if (KEY_LOGO_MODE_MANUAL_GREEN.equals(key)) {
            intValue = (Integer) value;
            intValueStr = String.valueOf(intValue);
            mLogoManualBarGreen.setValue(intValue);
            SettingsUtils.putInt(getActivity(), KEY_LOGO_MODE_MANUAL_GREEN, intValue);
            FileUtils.writeLine(GREEN_LED, intValueStr);
        }

        if (KEY_LOGO_MODE_MANUAL_BLUE.equals(key)) {
            intValue = (Integer) value;
            intValueStr = String.valueOf(intValue);
            mLogoManualBarBlue.setValue(intValue);
            SettingsUtils.putInt(getActivity(), KEY_LOGO_MODE_MANUAL_BLUE, intValue);
            FileUtils.writeLine(BLUE_LED, intValueStr);
        }

        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (KEY_LOGO_MODE.equals(key)) {
            int intValue = Integer.parseInt(sharedPreferences.getString(key, String.valueOf(LOGO_MODE_BREATH)));
            mLogoControlMode.setValue(String.valueOf(intValue));
            if (intValue == LOGO_MODE_BREATH) {
                summary = getResources().getString(R.string.logo_control_breath_title);
                mLogoManualBarRed.setVisible(false);
                mLogoManualBarGreen.setVisible(false);
                mLogoManualBarBlue.setVisible(false);
                FileUtils.writeLine(RED_LED, "0");
                FileUtils.writeLine(GREEN_LED, "0");
                FileUtils.writeLine(BLUE_LED, "0");
                FileUtils.writeLine(RED_LED_BLINK, "1");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                FileUtils.writeLine(GREEN_LED_BLINK, "1");
                    }
                }, 1000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                FileUtils.writeLine(BLUE_LED_BLINK, "1");
                    }
                }, 2000);
            } else if (intValue == LOGO_MODE_MANUAL) {
                String manualRedValue = String.valueOf(SettingsUtils.getInt(getContext(), KEY_LOGO_MODE_MANUAL_RED, 1));
                String manualGreenValue = String.valueOf(SettingsUtils.getInt(getContext(), KEY_LOGO_MODE_MANUAL_GREEN, 1));
                String manualBlueValue = String.valueOf(SettingsUtils.getInt(getContext(), KEY_LOGO_MODE_MANUAL_BLUE, 1));
                summary = getResources().getString(R.string.logo_control_manual_title);
                mLogoManualBarRed.setVisible(true);
                mLogoManualBarGreen.setVisible(true);
                mLogoManualBarBlue.setVisible(true);
                FileUtils.writeLine(RED_LED_BLINK, "0");
                FileUtils.writeLine(GREEN_LED_BLINK, "0");
                FileUtils.writeLine(BLUE_LED_BLINK, "0");
                FileUtils.writeLine(RED_LED, manualRedValue);
                FileUtils.writeLine(GREEN_LED, manualGreenValue);
                FileUtils.writeLine(BLUE_LED, manualBlueValue);
            }
            mLogoControlMode.setSummary(summary);
        }
    }
}
