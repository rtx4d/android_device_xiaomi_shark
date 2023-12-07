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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import org.lineageos.settings.device.logo.LogoFragment;
import org.lineageos.settings.device.utils.FileUtils;
import org.lineageos.settings.device.utils.SettingsUtils;

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
                FileUtils.writeLine(LogoFragment.RED_LED_BLINK, "1");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                FileUtils.writeLine(LogoFragment.GREEN_LED_BLINK, "1");
                    }
                }, 1000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                FileUtils.writeLine(LogoFragment.BLUE_LED_BLINK, "1");
                    }
                }, 2000);
            } else if (intValue == LogoFragment.LOGO_MODE_MANUAL) {
                String manualRedValue = String.valueOf(SettingsUtils.getInt(context, LogoFragment.KEY_LOGO_MODE_MANUAL_RED, 1));
                String manualGreenValue = String.valueOf(SettingsUtils.getInt(context, LogoFragment.KEY_LOGO_MODE_MANUAL_GREEN, 1));
                String manualBlueValue = String.valueOf(SettingsUtils.getInt(context, LogoFragment.KEY_LOGO_MODE_MANUAL_BLUE, 1));
                FileUtils.writeLine(LogoFragment.RED_LED_BLINK, "0");
                FileUtils.writeLine(LogoFragment.GREEN_LED_BLINK, "0");
                FileUtils.writeLine(LogoFragment.BLUE_LED_BLINK, "0");
                FileUtils.writeLine(LogoFragment.RED_LED, manualRedValue);
                FileUtils.writeLine(LogoFragment.GREEN_LED, manualGreenValue);
                FileUtils.writeLine(LogoFragment.BLUE_LED, manualBlueValue);
            }
        } else {
        FileUtils.writeLine(LogoFragment.RED_LED_BLINK, "0");
        FileUtils.writeLine(LogoFragment.GREEN_LED_BLINK, "0");
        FileUtils.writeLine(LogoFragment.BLUE_LED_BLINK, "0");
        FileUtils.writeLine(LogoFragment.RED_LED, "0");
        FileUtils.writeLine(LogoFragment.GREEN_LED, "0");
        FileUtils.writeLine(LogoFragment.BLUE_LED, "0");
        }
    }
}
