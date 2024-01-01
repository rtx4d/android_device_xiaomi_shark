/*
 * Copyright (C) 2015 The CyanogenMod Project
 *               2017-2019 The LineageOS Project
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

package org.lineageos.settings.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.lineageos.settings.device.logo.LogoFragment;
import org.lineageos.settings.device.utils.SettingsUtils;

import org.lineageos.settings.device.logo.LogoUtil;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final boolean DEBUG = false;
    private static final String TAG = "SharkParts";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (DEBUG) Log.d(TAG, "Received boot completed intent");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int intValue = Integer.parseInt(sharedPreferences.getString(LogoFragment.KEY_LOGO_MODE, String.valueOf(LogoFragment.LOGO_MODE_BREATH)));

        if (SettingsUtils.getEnabled(context, LogoFragment.KEY_LOGO_ENABLE)) {
            if (intValue == LogoFragment.LOGO_MODE_BREATH) {
                LogoUtil.enableBreathingEffect();
            } else if (intValue == LogoFragment.LOGO_MODE_MANUAL) {
                final int r = SettingsUtils.getInt(context, LogoFragment.KEY_LOGO_MODE_MANUAL_RED, 1);
                final int g = SettingsUtils.getInt(context, LogoFragment.KEY_LOGO_MODE_MANUAL_GREEN, 1);
                final int b = SettingsUtils.getInt(context, LogoFragment.KEY_LOGO_MODE_MANUAL_BLUE, 1);
                LogoUtil.setRGBStill(r, g, b);
            }
        } else {
            LogoUtil.turnOff();
        }
    }
}
