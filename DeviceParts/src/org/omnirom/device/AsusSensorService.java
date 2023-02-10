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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AsusSensorService extends Service {
    private static final String TAG = "AsusSensorService";
    private static final boolean DEBUG = true;

    private AsusSensor mAsusSensor;

    @Override
    public void onCreate() {
        if (DEBUG) Log.d(TAG, "Creating service");
        mAsusSensor = new AsusSensor(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (DEBUG) Log.d(TAG, "Starting service");
        mAsusSensor.enable();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (DEBUG) Log.d(TAG, "Destroying service");
        mAsusSensor.disable();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}