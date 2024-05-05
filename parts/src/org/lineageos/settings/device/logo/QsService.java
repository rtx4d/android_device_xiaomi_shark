/*
 * Copyright (C) 2024 The LineageOS Project
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

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import org.lineageos.settings.device.logo.LogoFragment;
import org.lineageos.settings.device.logo.LogoUtil;
import org.lineageos.settings.device.R;
import org.lineageos.settings.device.utils.SettingsUtils;

public class QsService extends TileService {
    private void updateState() {
        final boolean enabled = SettingsUtils.getEnabled(getApplicationContext(), LogoFragment.KEY_LOGO_ENABLE);
        getQsTile().setState(enabled ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        getQsTile().setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.logo));
        getQsTile().setLabel(getString(R.string.logo_control_title));
        getQsTile().updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateState();
    }

    @Override
    public void onClick() {
        super.onClick();
        final Context context = getApplicationContext();

        boolean enabled = !SettingsUtils.getEnabled(context, LogoFragment.KEY_LOGO_ENABLE);
        SettingsUtils.setEnabled(context, LogoFragment.KEY_LOGO_ENABLE, enabled);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int intValue = Integer.parseInt(sharedPreferences.getString(LogoFragment.KEY_LOGO_MODE, String.valueOf(LogoFragment.LOGO_MODE_BREATH)));
        if (enabled) {
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

        updateState();
    }
}
