/*
 * Copyright (C) 2020 The OmniROM Project
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
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class VolumeTile extends TileService {
	private AudioManager mAudioManager;
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        switch (mAudioManager.getRingerModeInternal()) {
			case AudioManager.RINGER_MODE_SILENT:
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_volume_ringer_mute));
                getQsTile().setLabel(getString(R.string.mute_mode));
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_volume_ringer_vibrate));
                getQsTile().setLabel(getString(R.string.vibrate_mode));
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_volume_ringer));
                getQsTile().setLabel(getString(R.string.volume_mode));
                break;
        }
        getQsTile().setState(Tile.STATE_ACTIVE);
        getQsTile().updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }
    
    @Override
    public void onClick() {
        super.onClick();
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        switch (mAudioManager.getRingerModeInternal()) {
            case AudioManager.RINGER_MODE_SILENT:
                mAudioManager.setRingerModeInternal(AudioManager.RINGER_MODE_NORMAL);
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_volume_ringer));
                getQsTile().setLabel(getString(R.string.volume_mode));
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                mAudioManager.setRingerModeInternal(AudioManager.RINGER_MODE_SILENT);
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_volume_ringer_mute));
                getQsTile().setLabel(getString(R.string.mute_mode));
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                mAudioManager.setRingerModeInternal(AudioManager.RINGER_MODE_VIBRATE);
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_volume_ringer_vibrate));
                getQsTile().setLabel(getString(R.string.vibrate_mode));                
                break;
        }
        getQsTile().updateTile();
    }

}