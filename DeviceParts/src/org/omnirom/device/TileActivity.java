/*
 * Copyright (C) 2021 The OmniROM Project
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

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class TileActivity extends AppCompatActivity {
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = new Intent();
        String className = ((ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME")).getClassName();
        if (className.equals("org.omnirom.device.FrameRateTileService") ||
                className.equals("org.omnirom.device.GloveModeTileService")) {
            intent.setPackage("org.omnirom.device");
            intent.setAction("org.omnirom.device.DEVICE_SETTING_PAGE");
            startActivity(intent);
        }
        finish();
    }
}
