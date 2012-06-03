package pt.drawgeo.sound;

import java.util.Collection;
import java.util.HashMap;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.main.R;

public class MusicManager {

	private static HashMap<Integer, MediaPlayer> players = new HashMap<Integer, MediaPlayer>();
	private static int currentMusic = -1;
	private static String TAG = "MusicManager";
	
	public static final int MENU_MUSIC = 0;
	public static final int GAME_MUSIC = 1;
	public static final int END_MUSIC = 2;
	public static int CURRENT_MUSIC = -1;
	public static boolean PAUSED = false;
	public static boolean MUTE = false;
	
	public static void start(Context context, int music) {
		
		
    	if(MusicManager.MUTE)
    		return;
		
		if (MusicManager.PAUSED)
		{
			currentMusic = -1;
			MusicManager.PAUSED = false;
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
			if (music == MusicManager.MENU_MUSIC) {
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