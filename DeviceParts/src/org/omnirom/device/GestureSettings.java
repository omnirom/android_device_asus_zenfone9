/*
* Copyright (C) 2017 The OmniROM Project
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
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.AsyncTask;
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
import static android.provider.Settings.Secure.SYSTEM_NAVIGATION_KEYS_ENABLED;
import android.os.UserHandle;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GestureSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    public static final String KEY_PROXI_SWITCH = "proxi";
    public static final String KEY_OFF_SCREEN_GESTURE_FEEDBACK_SWITCH = "off_screen_gesture_feedback";
    public static final String KEY_SWIPEUP_SWITCH = "swipeup";
    public static final String KEY_SETTINGS_SWIPEUP_PREFIX = "gesture_setting_";

    public static final String SETTINGS_GESTURE_KEY = KEY_SETTINGS_SWIPEUP_PREFIX + KEY_SWIPEUP_SWITCH;

    public static final int KEY_C_ID = 0;
    public static final int KEY_E_ID = 1;
    public static final int KEY_S_ID = 2;
    public static final int KEY_V_ID = 3;
    public static final int KEY_W_ID = 4;
    public static final int KEY_Z_ID = 5;
    public static final String KEY_C_APP = "c_gesture_app";
    public static final String KEY_E_APP = "e_gesture_app";
    public static final String KEY_S_APP = "s_gesture_app";
    public static final String KEY_V_APP = "v_gesture_app";
    public static final String KEY_W_APP = "w_gesture_app";
    public static final String KEY_Z_APP = "z_gesture_app";
    public static final String KEY_GOOGLE_APP = "smart_switch";

    public static final String KEY_FP_GESTURE_CATEGORY = "key_fp_gesture_category";
    public static final String KEY_FP_GESTURE_DEFAULT_CATEGORY = "gesture_settings";

    public static final String FP_GESTURE_LONG_PRESS_APP = "fp_long_press_gesture_app";

    public static final String DEVICE_GESTURE_MAPPING_0 = "device_gesture_mapping_0_0";
    public static final String DEVICE_GESTURE_MAPPING_1 = "device_gesture_mapping_1_0";
    public static final String DEVICE_GESTURE_MAPPING_2 = "device_gesture_mapping_2_0";
    public static final String DEVICE_GESTURE_MAPPING_3 = "device_gesture_mapping_3_0";
    public static final String DEVICE_GESTURE_MAPPING_4 = "device_gesture_mapping_4_0";
    public static final String DEVICE_GESTURE_MAPPING_5 = "device_gesture_mapping_5_0";
    public static final String DEVICE_GESTURE_MAPPING_6 = "device_gesture_mapping_6_0";
    public static final String DEVICE_GESTURE_MAPPING_7 = "device_gesture_mapping_7_0";

    private TwoStatePreference mProxiSwitch;
    private TwoStatePreference mSwipeUpSwitch;
    private AppSelectListPreference mFPLongPressApp;
    private AppSelectListPreference mLetterCGesture;
    private AppSelectListPreference mLetterEGesture;
    private AppSelectListPreference mLetterSGesture;
    private AppSelectListPreference mLetterVGesture;
    private AppSelectListPreference mLetterWGesture;
    private AppSelectListPreference mLetterZGesture;
    private AppSelectListPreference mSmartKeySwitch;

    public static final String GESTURE_CONTROL_PATH = "/proc/driver/gesture_type";
    private static final String SWIPEUP_PATH = "/proc/driver/swipeup";

    private PreferenceCategory fpGestures;
    private boolean mFpDownSwipe;
    private List<AppSelectListPreference.PackageItem> mInstalledPackages = new LinkedList<AppSelectListPreference.PackageItem>();
    private PackageManager mPm;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.gesture_settings, rootKey);
        mPm = getContext().getPackageManager();

        mProxiSwitch = (TwoStatePreference) findPreference(KEY_PROXI_SWITCH);
        mProxiSwitch.setChecked(Settings.System.getInt(getContext().getContentResolver(),
                Settings.System.OMNI_DEVICE_PROXI_CHECK_ENABLED, 1) != 0);

        mFPLongPressApp = (AppSelectListPreference) findPreference(FP_GESTURE_LONG_PRESS_APP);
        mFPLongPressApp.setEnabled(true);
        String value = Settings.System.getString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_0);
        mFPLongPressApp.setValue(value);
        mFPLongPressApp.setOnPreferenceChangeListener(this);

        mLetterCGesture = (AppSelectListPreference) findPreference(KEY_C_APP);
        mLetterCGesture.setEnabled(true);
        value = Settings.System.getString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_1);
        mLetterCGesture.setValue(value);
        mLetterCGesture.setOnPreferenceChangeListener(this);

        mLetterEGesture = (AppSelectListPreference) findPreference(KEY_E_APP);
        mLetterEGesture.setEnabled(true);
        value = Settings.System.getString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_2);
        mLetterEGesture.setValue(value);
        mLetterEGesture.setOnPreferenceChangeListener(this);

        mLetterSGesture = (AppSelectListPreference) findPreference(KEY_S_APP);
        mLetterSGesture.setEnabled(true);
        value = Settings.System.getString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_3);
        mLetterSGesture.setValue(value);
        mLetterSGesture.setOnPreferenceChangeListener(this);

        mLetterVGesture = (AppSelectListPreference) findPreference(KEY_V_APP);
        mLetterVGesture.setEnabled(true);
        value = Settings.System.getString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_4);
        mLetterVGesture.setValue(value);
        mLetterVGesture.setOnPreferenceChangeListener(this);

        mLetterWGesture = (AppSelectListPreference) findPreference(KEY_W_APP);
        mLetterWGesture.setEnabled(true);
        value = Settings.System.getString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_5);
        mLetterWGesture.setValue(value);
        mLetterWGesture.setOnPreferenceChangeListener(this);

        mLetterZGesture = (AppSelectListPreference) findPreference(KEY_Z_APP);
        mLetterZGesture.setEnabled(true);
        value = Settings.System.getString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_6);
        mLetterZGesture.setValue(value);
        mLetterZGesture.setOnPreferenceChangeListener(this);

        mSmartKeySwitch = (AppSelectListPreference) findPreference(KEY_GOOGLE_APP);
        mSmartKeySwitch.setEnabled(isGestureSupported(KEY_GOOGLE_APP));
        value = Settings.System.getString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_7);
        mSmartKeySwitch.setValue(value);
        mSmartKeySwitch.setOnPreferenceChangeListener(this);

        mSwipeUpSwitch = (TwoStatePreference) findPreference(KEY_SWIPEUP_SWITCH);
        mSwipeUpSwitch.setChecked(Settings.System.getInt(getContext().getContentResolver(),
        KEY_SWIPEUP_SWITCH, 0) == 1);

        new FetchPackageInformationTask().execute();
    }

    private boolean areSystemNavigationKeysEnabled() {
        return Settings.Secure.getInt(getContext().getContentResolver(),
               Settings.Secure.SYSTEM_NAVIGATION_KEYS_ENABLED, 0) == 1;
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mProxiSwitch) {
            Settings.System.putInt(getContext().getContentResolver(),
                    Settings.System.OMNI_DEVICE_PROXI_CHECK_ENABLED, mProxiSwitch.isChecked() ? 1 : 0);
            return true;
        }
        if (preference == mSwipeUpSwitch) {
            Settings.System.putInt(getContext().getContentResolver(), KEY_SWIPEUP_SWITCH, mSwipeUpSwitch.isChecked() ? 1 : 0);
            Utils.writeValue(getFile(), mSwipeUpSwitch.isChecked() ? "1" : "0");
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mFPLongPressApp) {
            String value = (String) newValue;
            Settings.System.putString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_0, value);
        } else if (preference == mLetterCGesture) {
            String value = (String) newValue;
            boolean gestureDisabled = value.equals(AppSelectListPreference.DISABLED_ENTRY);
            setGestureEnabled(KEY_C_ID, !gestureDisabled);
            Settings.System.putString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_1, value);
        } else if (preference == mLetterEGesture) {
            String value = (String) newValue;
            boolean gestureDisabled = value.equals(AppSelectListPreference.DISABLED_ENTRY);
            setGestureEnabled(KEY_E_ID, !gestureDisabled);
            Settings.System.putString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_2, value);
        } else if (preference == mLetterSGesture) {
            String value = (String) newValue;
            boolean gestureDisabled = value.equals(AppSelectListPreference.DISABLED_ENTRY);
            setGestureEnabled(KEY_S_ID, !gestureDisabled);
            Settings.System.putString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_3, value);
        } else if (preference == mLetterVGesture) {
            String value = (String) newValue;
            boolean gestureDisabled = value.equals(AppSelectListPreference.DISABLED_ENTRY);
            setGestureEnabled(KEY_V_ID, !gestureDisabled);
            Settings.System.putString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_4, value);
        } else if (preference == mLetterWGesture) {
            String value = (String) newValue;
            boolean gestureDisabled = value.equals(AppSelectListPreference.DISABLED_ENTRY);
            setGestureEnabled(KEY_W_ID, !gestureDisabled);
            Settings.System.putString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_5, value);
        } else if (preference == mLetterZGesture) {
            String value = (String) newValue;
            boolean gestureDisabled = value.equals(AppSelectListPreference.DISABLED_ENTRY);
            setGestureEnabled(KEY_Z_ID, !gestureDisabled);
            Settings.System.putString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_6, value);
        } else if (preference == mSmartKeySwitch) {
            String value = (String) newValue;
            boolean gestureDisabled = value.equals(AppSelectListPreference.DISABLED_ENTRY);
            setGestureEnabledGoogle(KEY_GOOGLE_APP, !gestureDisabled);
            Settings.System.putString(getContext().getContentResolver(), DEVICE_GESTURE_MAPPING_7, value);
        }
        return true;
    }

    public static String getFile() {
        if (Utils.fileWritable(SWIPEUP_PATH)) {
            return SWIPEUP_PATH;
        }
        return null;
    }

    public static String getGestureFile(String key) {
        switch(key) {
            case GESTURE_CONTROL_PATH:
                return "/proc/driver/gesture_type";
            case KEY_GOOGLE_APP:
                return "/sys/devices/platform/soc/soc:asustek_googlekey/googlekey_enable";
            case SWIPEUP_PATH:
                return "/proc/driver/swipeup";
        }
        return null;
    }

    private static final int KEY_MASK_GESTURE_CONTROL = 0x40;
    private static final int[] ALL_GESTURE_MASKS = {
        0x04, // c gesture mask
        0x08, // e gesture mask
        0x10, // s gesture mask
        0x01, // v gesture mask
        0x20, // w gesture mask
        0x02, // z gesture mask
    };

    private void setGestureEnabled(int id, boolean enabled) {
        Log.i("GestureSettings", "setGestureEnabled called with key=" +id+ ",enabled=" +enabled);
        int gestureMode = Integer.parseInt(Utils.readLine(GESTURE_CONTROL_PATH));
        int mask = ALL_GESTURE_MASKS[id];

        if (enabled)
            gestureMode |= mask;
        else
            gestureMode &= ~mask;

        if (gestureMode != 0)
            gestureMode |= KEY_MASK_GESTURE_CONTROL;

        String gestureType = String.format("%7s", Integer.toBinaryString(gestureMode)).replace(' ', '0');
        Log.i("GestureSettings", "gestureType=" +gestureType);

        String gestureTypeMapping = Settings.System.getString(getContext().getContentResolver(), Settings.System.OMNI_BUTTON_EXTRA_KEY_MAPPING);
        Settings.System.putString(getContext().getContentResolver(), Settings.System.OMNI_BUTTON_EXTRA_KEY_MAPPING, gestureType);

        Utils.writeLine(GESTURE_CONTROL_PATH, gestureType);
    }

    private boolean isGestureSupported(String key) {
        return Utils.fileWritable(getGestureFile(key));
    }

    private void setGestureEnabledGoogle(String key, boolean enabled) {
        Utils.writeValue(getGestureFile(key), enabled ? "1" : "0");
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (!(preference instanceof AppSelectListPreference)) {
            super.onDisplayPreferenceDialog(preference);
            return;
        }
        DialogFragment fragment =
                AppSelectListPreference.AppSelectListPreferenceDialogFragment
                        .newInstance(preference.getKey());
        fragment.setTargetFragment(this, 0);
        fragment.show(getFragmentManager(), "dialog_preference");
    }

    private void loadInstalledPackages() {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> installedAppsInfo = mPm.queryIntentActivities(mainIntent, 0);

        for (ResolveInfo info : installedAppsInfo) {
            ActivityInfo activity = info.activityInfo;
            ApplicationInfo appInfo = activity.applicationInfo;
            ComponentName componentName = new ComponentName(appInfo.packageName, activity.name);
            CharSequence label = null;
            try {
                label = activity.loadLabel(mPm);
            } catch (Exception e) {
            }
            if (label != null) {
                final AppSelectListPreference.PackageItem item = new AppSelectListPreference.PackageItem(activity.loadLabel(mPm), 0, componentName);
                mInstalledPackages.add(item);
            }
        }
        Collections.sort(mInstalledPackages);
    }

    private class FetchPackageInformationTask extends AsyncTask<Void, Void, Void> {
        public FetchPackageInformationTask() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadInstalledPackages();
            return null;
        }

        @Override
        protected void onPostExecute(Void feed) {
            mFPLongPressApp.setPackageList(mInstalledPackages);
            mLetterCGesture.setPackageList(mInstalledPackages);
            mLetterEGesture.setPackageList(mInstalledPackages);
            mLetterSGesture.setPackageList(mInstalledPackages);
            mLetterVGesture.setPackageList(mInstalledPackages);
            mLetterWGesture.setPackageList(mInstalledPackages);
            mLetterZGesture.setPackageList(mInstalledPackages);
            mSmartKeySwitch.setPackageList(mInstalledPackages);
        }
    }
}
