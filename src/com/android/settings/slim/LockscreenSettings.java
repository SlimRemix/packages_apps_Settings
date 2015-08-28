 /*
 * Copyright (C) 2015 SlimSaber
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
package com.android.settings.slim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.CheckBoxPreference;
import android.preference.SwitchPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.slimremix.util.AbstractAsyncSuCMDProcessor;
import com.android.settings.slimremix.util.CMDProcessor;
import com.android.settings.slimremix.util.Helpers;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class LockscreenSettings extends SettingsPreferenceFragment
        implements OnPreferenceChangeListener {

    private static final String LOCKSCREEN_BOTTOM_SHORTCUTS = "lockscreen_bottom_shortcuts";
    private static final String CARRIERLABEL_ON_LOCKSCREEN="lock_screen_hide_carrier";

    private SwitchPreference mCarrierLabelOnLockScreen;
    private SwitchPreference mLockscreenBottomShortcuts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.lockscreen_settings);

        ContentResolver resolver = getActivity().getContentResolver();
        PreferenceScreen prefSet = getPreferenceScreen();

        // Lockscreen bottom shortcuts
        mLockscreenBottomShortcuts = (SwitchPreference) findPreference(LOCKSCREEN_BOTTOM_SHORTCUTS);
        if (mLockscreenBottomShortcuts != null) {
            boolean lockScreenBottomShortcutsEnabled = Settings.Secure.getInt(getContentResolver(),
                    Settings.Secure.LOCKSCREEN_BOTTOM_SHORTCUTS, 1) == 1;
            mLockscreenBottomShortcuts.setChecked(lockScreenBottomShortcutsEnabled);
            mLockscreenBottomShortcuts.setSummary(lockScreenBottomShortcutsEnabled
                    ? R.string.lockscreen_bottom_shortcuts_enabled :
                      R.string.lockscreen_bottom_shortcuts_disabled);
            mLockscreenBottomShortcuts.setOnPreferenceChangeListener(this);
        }

        //CarrierLabel on LockScreen
        mCarrierLabelOnLockScreen = (SwitchPreference) findPreference(CARRIERLABEL_ON_LOCKSCREEN);
        if (!Utils.isWifiOnly(getActivity())) {
            mCarrierLabelOnLockScreen.setOnPreferenceChangeListener(this);

            boolean hideCarrierLabelOnLS = Settings.System.getInt(
                    getActivity().getContentResolver(),
                    Settings.System.LOCK_SCREEN_HIDE_CARRIER, 0) == 1;
            mCarrierLabelOnLockScreen.setChecked(hideCarrierLabelOnLS);
        } else {
            prefSet.removePreference(mCarrierLabelOnLockScreen);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mLockscreenBottomShortcuts) {
            Settings.Secure.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.Secure.LOCKSCREEN_BOTTOM_SHORTCUTS,
                    (Boolean) objValue ? 1 : 0);
            return true;
        } else if (preference == mCarrierLabelOnLockScreen) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.LOCK_SCREEN_HIDE_CARRIER,
                    (Boolean) objValue ? 1 : 0);
            Helpers.restartSystemUI();
            return true;
        }
        return false;
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
 		return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
