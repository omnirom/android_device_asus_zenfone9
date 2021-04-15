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
import android.content.Context;
import android.content.res.Resources;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
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
import java.util.Map;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    protected static final String DEFAULT_FPS_VALUE = "60";
    private static final String KEY_CATEGORY_SCREEN = "screen";
    public static final String KEY_GLOVE_SWITCH = "glove";
    private static final String KEY_FRAME_MODE = "frame_mode_key";
    private static final String KEY_FRAME_CATEGORY = "frame_mode_main";
    public static final String KEY_SETTINGS_PREFIX = "device_setting_";
    public static final String FPS = "fps";

    private static ListPreference mFrameModeRate;
    private static TwoStatePreference mGloveModeSwitch;

    private static final String SURFACE_FLINGER_SERVICE_KEY = "SurfaceFlinger";
    private static final String SURFACE_COMPOSER_INTERFACE_KEY = "android.ui.ISurfaceComposer";
    private static final int SURFACE_FLINGER_CODE = 1035;
    private static Map<Integer, Integer> fpsMap = Map.of(60, 0, 90, 1, 120, 2);

    private static IBinder mSurfaceFlinger;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main, rootKey);

        mFrameModeRate = (ListPreference) findPreference(KEY_FRAME_MODE);
        int framevalue = Settings.System.getInt(getContext().getContentResolver(),
                            FPS, 60);
        mFrameModeRate.setValue(Integer.toString(framevalue));
        mFrameModeRate.setSummary(mFrameModeRate.getEntry());
        mFrameModeRate.setOnPreferenceChangeListener(this);

        mGloveModeSwitch = (TwoStatePreference) findPreference(KEY_GLOVE_SWITCH);
        mGloveModeSwitch.setEnabled(GloveModeSwitch.isSupported());
        mGloveModeSwitch.setChecked(GloveModeSwitch.isCurrentlyEnabled(this.getContext()));
        mGloveModeSwitch.setOnPreferenceChangeListener(new GloveModeSwitch(getContext()));

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mFrameModeRate) {
            int value = Integer.valueOf((String) newValue);
            int index = mFrameModeRate.findIndexOfValue((String) newValue);
            mFrameModeRate.setSummary(mFrameModeRate.getEntries()[index]);
            changeFps(getContext(), value);
            Settings.System.putInt(getContext().getContentResolver(), FPS, value);
        }
        return true;
    }

    protected static void changeFps(Context context, int fps) {
        mSurfaceFlinger = ServiceManager.getService(SURFACE_FLINGER_SERVICE_KEY);
        try {
            if (mSurfaceFlinger != null) {
                mSurfaceFlinger = ServiceManager.getService(SURFACE_FLINGER_SERVICE_KEY);
                Parcel data = Parcel.obtain();
                data.writeInterfaceToken(SURFACE_COMPOSER_INTERFACE_KEY);
                data.writeInt(fpsMap.getOrDefault(fps, -1));
                mSurfaceFlinger.transact(SURFACE_FLINGER_CODE, data, null, 0);
                data.recycle();
                System.out.println("OmnifpsMap: " + fpsMap +" Omnifps: "+ fps);
            }
        } catch (RemoteException ex) {
               // intentional no-op
        }
            Settings.System.putInt(context.getContentResolver(), FPS, fps);
    }
}
