package pt.drawgeo.main;

import pt.drawgeo.map.MapsActivity;
import pt.drawgeo.sound.MusicManager;
import pt.drawgeo.sound.SoundManager;
import pt.drawgeo.utility.Configurations;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.main.R;

public class MainMenuActivity extends Activity {
	
    

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Modo fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		 
		setContentView(R.layout.mainmenu);
		
		final TextView challengesdone = (TextView) findViewById(R.id.challengesdonetext);
		challengesdone.setText(Configurations.num_done+"");
		
		final TextView challengescreated = (TextView) findViewById(R.id.challengescreatedtext);
		challengescreated.setText(Configurations.num_created+"");
		
		final TextView guessedyours = (TextView) findViewById(R.id.guessedyourstext);
		guessedyours.setText(Configurations.num_success+"");
		
		final TextView piggies = (TextView) findViewById(R.id.piggiestext);
		piggies.setText(Configurations.piggies+"");
		
		final TextView name = (TextView) findViewById(R.id.name);
		name.setText(Configurations.name+"");
		
		final ImageView avatar = (ImageView) findViewById(R.id.avatar);
		avatar.setImageBitmap(Configurations.avatarImage);
	
		final ImageView pButton = (ImageView) findViewById(R.id.btnPlay);
		pButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				PreferenceManager.setDefaultValues(v.getContext(), R.layout.options, true);
				SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(v.getContext());
		    	if(myPreference.getBoolean("soundOption", false))		    
		    		new Thread(
		    				new Runnable() {
		    					public void run() {
		    						AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		    						float volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		    						SoundManager.spool.play(SoundManager.click, volume, volume, 1, 0, 1f);
		    					}
		    				}).start();
				
				Intent intent = new Intent(v.getContext(),
				MapsActivity.class);
				startActivity(intent);
			}
		});
		final ImageView sButton = (ImageView) findViewById(R.id.btnStore);
		sButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				PreferenceManager.setDefaultValues(v.getContext(), R.layout.options, true);
				SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(v.getContext());
		    	if(myPreference.getBoolean("soundOption", false))		    
		    		new Thread(
		    				new Runnable() {
		    					public void run() {
		    						AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		    						float volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		    						SoundManager.spool.play(SoundManager.click, volume, volume, 1, 0, 1f);
		    					}
		    				}).start();
              
				Intent intent = new Intent(v.getContext(),
						Store.class);
				
				startActivityForResult(intent,100);
				MainMenuActivity.this.overridePendingTransition(R.anim.animation_enter,
		                R.anim.animation_leave);
				
			}
		});
		
		final ImageView aButton = (ImageView) findViewById(R.id.btnAbout);
		aButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				PreferenceManager.setDefaultValues(v.getContext(), R.layout.options, true);
				SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(v.getContext());
		    	if(myPreference.getBoolean("soundOption", false))		    
		    		new Thread(
		    				new Runnable() {
		    					public void run() {
		    						AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		    						float volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		    						SoundManager.spool.play(SoundManager.click, volume, volume, 1, 0, 1f);
		    					}
		    				}).start();
               
				showAbout();
			}
		});
		final ImageView oButton = (ImageView) findViewById(R.id.btnOptions);
		oButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				PreferenceManager.setDefaultValues(v.getContext(), R.layout.options, true);
				SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(v.getContext());
		    	if(myPreference.getBoolean("soundOption", false))		    
		    		new Thread(
		    				new Runnable() {
		    					public void run() {
		    						AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		    						float volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		    						SoundManager.spool.play(SoundManager.click, volume, volume, 1, 0, 1f);
		    					}
		    				}).start();

				Intent intent = new Intent(v.getContext(),
						OptionsActivity.class);
				startActivity(intent);
			}
		});
	}

	protected void showAbout() {
		new AlertDialog.Builder(this)
		.setTitle("About")
		.setMessage(
				"This application was developed by some really cool guys at Faculdade de Engenharia da Universidade do Porto! "
						+ "If you like it, feel free to have a chat with us at drawgeo@gmail.com!")
		.setPositiveButton("Close",
				new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog,
							int which) {
					}
			}
					).show();
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.overridePendingTransition(R.anim.animation_enter2,
                R.anim.animation_leave2);
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		MusicManager.PAUSED = true;
		MusicManager.pause();
	
	}

	@Override
	protected void onResume() {
		super.onResume();
		MusicManager.CURRENT_MUSIC = MusicManager.MENU_MUSIC;
		MusicManager.start(this, MusicManager.CURRENT_MUSIC);
	}
	
	private Toast toast;
	private long lastBackPressTime = 0;

	@Override
	public void onBackPressed() {
		
	  if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
	    toast = Toast.makeText(this, "Press back once more to exit", 4000);
	    toast.show();
	    this.lastBackPressTime = System.currentTimeMillis();
	  } else {
	    if (toast != null) {
	    toast.cancel();
	   
	  }
	    super.onBackPressed();
	  
	 }
	}


	
}
