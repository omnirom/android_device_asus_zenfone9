/*
* Copyright (C) 2016 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package org.omnirom.device;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.content.Intent;
import android.os.Bundle;
import androidx.preference.PreferenceFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.TwoStatePreference;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.util.Log;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    public static final String SLIDER_DEFAULT_VALUE = "2,1,0";

    private static final String KEY_SETTINGS_PREFIX = "device_setting_";
    public static final String KEY_GLOVE_SWITCH = "glove";
    public static final String KEY_GLOVE_PATH = "/proc/driver/glove";
    public static final String SETTINGS_GLOVE_KEY = KEY_SETTINGS_PREFIX + KEY_GLOVE_SWITCH;

    private static final String KEY_CATEGORY_SCREEN = "screen";
    private static TwoStatePreference mGloveModeSwitch;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main, rootKey);

        mGloveModeSwitch = (TwoStatePreference) findPreference(KEY_GLOVE_SWITCH);
        mGloveModeSwitch.setChecked(Settings.System.getInt(getContext().getContentResolver(),
        KEY_GLOVE_SWITCH, 0) == 1);

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mGloveModeSwitch) {
            Settings.System.putInt(getContext().getContentResolver(), KEY_GLOVE_SWITCH, mGloveModeSwitch.isChecked() ? 1 : 0);
            Utils.writeValue(getFile(), mGloveModeSwitch.isChecked() ? "1" : "0");
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }

    public static String getFile() {
        if (Utils.fileWritable(KEY_GLOVE_PATH)) {
            return KEY_GLOVE_PATH;
        }
        return null;
    }

    public static String getGestureFile(String key) {
        switch(key) {
            case KEY_GLOVE_PATH:
                return "/proc/driver/glove";
        }
        return null;
    }

    public static boolean isCurrentlyEnabled() {
        return Utils.getLineValueAsBoolean(getFile(), true);
    }
}
