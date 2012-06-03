package pt.drawgeo.utility;

import java.util.Collection;
import java.util.HashMap;


import com.main.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.util.Log;

public class MusicManager {

	private static HashMap<Integer, MediaPlayer> players = new HashMap<Integer, MediaPlayer>();
	private static int currentMusic = -1;
	private static String TAG = "MusicManager";
	
	public static void start(Context context, int music) {
		
		PreferenceManager.setDefaultValues(context, R.layout.options, true);
		SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(context);
    	if(!myPreference.getBoolean("soundOption", false))
    		return;
		
		if (Configurations.PAUSED)
		{
			currentMusic = -1;
			Configurations.PAUSED = false;
		}
		
		if (currentMusic == music) {
			// already playing this music
			return;
		}
		if (currentMusic != -1) {
			
			Log.d(TAG, "Pausing music [" + currentMusic + "]");
			pause();
		}
		
		currentMusic = music;
		Log.d(TAG, "Current music is now [" + currentMusic + "]");
		
		MediaPlayer mp = (MediaPlayer) players.get(music);
		if (mp != null) {
			if (!mp.isPlaying()) {
				mp.start();
			}
		} else {
			if (music == Configurations.MENU_MUSIC) {
				mp = MediaPlayer.create(context, R.raw.menu_music);
			}
			players.put(music, mp);
			
			if (mp == null) {
				Log.e(TAG, "player was not created successfully");
			} else {
				try {
					mp.setLooping(true);
					mp.start();
				} catch (Exception e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}
	}

	public static void pause() {
		Collection<MediaPlayer> mps = players.values();
		for (MediaPlayer p : mps) {
			if (p.isPlaying()) {
				p.pause();
				
			}
		}
	}
}