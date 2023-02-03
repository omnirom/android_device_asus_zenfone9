/*
 * Copyright (c) 2023 The OmniRom Project
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

package org.omnirom.device;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsusSensor implements SensorEventListener {
    private static final boolean DEBUG = true;
    private static final String TAG = "AsusSensor";

    private ExecutorService mExecutorService;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor mSensor2;
    private Sensor mSensor3;
    private Sensor mSensor4;
    private Context mContext;

    public AsusSensor(Context context) {
        mContext = context;
        mSensorManager = mContext.getSystemService(SensorManager.class);
        mExecutorService = Executors.newSingleThreadExecutor();

        for (Sensor sensor : mSensorManager.getSensorList(Sensor.TYPE_ALL)) {
            if (DEBUG) Log.d(TAG, "Sensor type: " + sensor.getStringType());
            if (TextUtils.equals(sensor.getStringType(), "android.sensor.asus_motion")) {
                if (DEBUG) Log.d(TAG, "Found asus_motion sensor");
                mSensor = sensor;
                break;
            }
        }
        for (Sensor sensor2 : mSensorManager.getSensorList(Sensor.TYPE_ALL)) {
            if (DEBUG) Log.d(TAG, "Sensor type: " + sensor2.getStringType());
            if (TextUtils.equals(sensor2.getStringType(), "android.sensor.asus_stationary")) {
                if (DEBUG) Log.d(TAG, "Found asus_stationary sensor");
                mSensor2 = sensor2;
                break;
            }
        }
        for (Sensor sensor3 : mSensorManager.getSensorList(Sensor.TYPE_ALL)) {
            if (DEBUG) Log.d(TAG, "Sensor type: " + sensor3.getStringType());
            if (TextUtils.equals(sensor3.getStringType(), "android.sensor.tap_gesture")) {
                if (DEBUG) Log.d(TAG, "Found Asus tap_gesture sensor");
                mSensor3 = sensor3;
                break;
            }
        }
        for (Sensor sensor4 : mSensorManager.getSensorList(Sensor.TYPE_ALL)) {
            if (DEBUG) Log.d(TAG, "Sensor type: " + sensor4.getStringType());
            if (TextUtils.equals(sensor4.getStringType(), "android.sensor.free_fall")) {
                if (DEBUG) Log.d(TAG, "Found Asus free_fall sensor");
                mSensor4 = sensor4;
                break;
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] > 0) {
            Log.d(TAG, "Fall detected, ensuring front camera is closed");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        /* Empty */
    }

    void enable() {
        if (DEBUG) Log.d(TAG, "Enabling");
        mExecutorService.submit(() -> {
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mSensor2, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mSensor3, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mSensor4, SensorManager.SENSOR_DELAY_NORMAL);
        });
    }

    void disable() {
        if (DEBUG) Log.d(TAG, "Disabling");
        mExecutorService.submit(() -> {
            mSensorManager.unregisterListener(this, mSensor);
            mSensorManager.unregisterListener(this, mSensor2);
            mSensorManager.unregisterListener(this, mSensor3);
            mSensorManager.unregisterListener(this, mSensor4);
        });
    }
}
