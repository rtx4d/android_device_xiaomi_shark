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

package org.lineageos.settings.device;

import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.content.Intent;
import android.os.Bundle;
import androidx.preference.PreferenceFragment;
import androidx.preference.Preference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import androidx.preference.TwoStatePreference;
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity;
import com.android.settingslib.widget.R;

import org.lineageos.settings.device.logo.LogoActivity;

public class SharkParts extends CollapsingToolbarBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.content_frame);
        SharkPartsFragment mSharkPartsFragment;
        if (fragment == null) {
            mSharkPartsFragment = new SharkPartsFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.content_frame, mSharkPartsFragment)
                    .commit();
        }
    }

    public static class SharkPartsFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.sharkparts, rootKey);
            Preference mLogoPref = findPreference("logo");
            mLogoPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getContext(), LogoActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            final String key = preference.getKey();
            return true;
        }
    }
}
