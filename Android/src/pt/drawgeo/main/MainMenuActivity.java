package pt.drawgeo.main;

import org.json.JSONObject;

import pt.drawgeo.map.MapsActivity;
import pt.drawgeo.sound.MusicManager;
import pt.drawgeo.sound.SoundManager;
import pt.drawgeo.utility.Configurations;
import pt.drawgeo.utility.Connection;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.main.R;

public class MainMenuActivity extends Activity {

	private ProgressDialog dialog;

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
		
		final TextView ranking = (TextView) findViewById(R.id.ranking);
		ranking.setText(Configurations.getRanking(Configurations.ranking)+" Artist");
	
		final ImageView pButton = (ImageView) findViewById(R.id.btnPlay);
		pButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				
		    	if(!MusicManager.MUTE)		    
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
				
				
		    	if(!MusicManager.MUTE)		    
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
				
				
		    	if(!MusicManager.MUTE)		    
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
				
				
		    	if(!MusicManager.MUTE)		    
		    		new Thread(
		    				new Runnable() {
		    					public void run() {
		    						AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		    						float volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		    						SoundManager.spool.play(SoundManager.click, volume, volume, 1, 0, 1f);
		    					}
		    				}).start();

				Intent intent = new Intent(v.getContext(),
						HowTo.class);
				startActivityForResult(intent,100);
				MainMenuActivity.this.overridePendingTransition(R.anim.animation_enter,
		                R.anim.animation_leave);
			}
		});
		
		final ImageView soButton = (ImageView) findViewById(R.id.btnSound);
		soButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				
		    	if(!MusicManager.MUTE)		    
		    		new Thread(
		    				new Runnable() {
		    					public void run() {
		    						AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		    						float volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		    						SoundManager.spool.play(SoundManager.click, volume, volume, 1, 0, 1f);
		    					}
		    				}).start();
		    	
		    	MusicManager.MUTE = !MusicManager.MUTE;
		    	if(!MusicManager.MUTE) {
		    		soButton.setImageResource(R.drawable.soundon);
		    		MusicManager.start(MainMenuActivity.this, MusicManager.CURRENT_MUSIC);
		    	}
		    	else
		    	{
		    		soButton.setImageResource(R.drawable.soundoff);
		    		MusicManager.PAUSED = true;
		    		MusicManager.pause();
		    	}
				
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
		dialog = ProgressDialog.show(MainMenuActivity.this, "", 
                "Retrieving information...", true);
    
    	new DownloadUserInfo().execute();
    	
    	
		
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

	private class DownloadUserInfo extends AsyncTask<String, Integer, Long> {

		@Override
		protected Long doInBackground(String... userInfo) {
			Uri uri = new Uri.Builder()
            .scheme(Configurations.SCHEME)
            .authority(Configurations.AUTHORITY)
            .path(Configurations.CHECKUSER)
            .appendQueryParameter("email", Configurations.email)              
            .appendQueryParameter("format", Configurations.FORMAT)
            .build();
        	
        	String response = null;
        	try {
				response = Connection.getJSONLine(uri);
				JSONObject info = new JSONObject(response);
				String status = info.getString("status");
				if(status.equals("Ok")) {
					JSONObject user = info.getJSONObject("user"); 
					Configurations.num_done = user.getInt("num_done");
					Configurations.num_success = user.getInt("num_success");
					Configurations.num_created = info.getInt("created");
					Configurations.piggies = user.getInt("piggies");
					Configurations.ranking = user.getInt("ranking");
					MainMenuActivity.this.runOnUiThread(new Runnable() {
			          	  public void run() {
			          		
			          		final TextView challengesdone = (TextView) findViewById(R.id.challengesdonetext);
			        		challengesdone.setText(Configurations.num_done+"");
			        		
			        		final TextView challengescreated = (TextView) findViewById(R.id.challengescreatedtext);
			        		challengescreated.setText(Configurations.num_created+"");
			        		
			        		final TextView guessedyours = (TextView) findViewById(R.id.guessedyourstext);
			        		guessedyours.setText(Configurations.num_success+"");
			        		
			        		final TextView piggies = (TextView) findViewById(R.id.piggiestext);
			        		piggies.setText(Configurations.piggies+"");
			          		
			        		final TextView ranking = (TextView) findViewById(R.id.ranking);
			        		ranking.setText(Configurations.getRanking(Configurations.ranking)+" Artist");
			          	  }
				    	});
					
				}
		}
	    catch (Exception e) {
	    	MainMenuActivity.this.runOnUiThread(new Runnable() {
          	  public void run() {
          		Toast.makeText(MainMenuActivity.this.getApplicationContext(), "Something went wrong. Are you connected to the internet?", Toast.LENGTH_SHORT).show();
          		
          	  }
	    	});
	    	return (long) 1;
	    }
			return null;
		}
		
		protected void onPostExecute(Long result) {
      		dialog.dismiss();
     }
	};
	
}
