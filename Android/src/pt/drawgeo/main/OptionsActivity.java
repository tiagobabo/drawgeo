package pt.drawgeo.main;

import pt.drawgeo.sound.MusicManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.main.R;
     
public class OptionsActivity extends PreferenceActivity {

	public boolean music = true;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	addPreferencesFromResource(R.layout.options);
	
		Preference myPref = (Preference) findPreference("logoutOption");
		myPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
		    
		public boolean onPreferenceClick(final Preference preference) {
			new AlertDialog.Builder(preference.getContext())
		    .setTitle("Confirmation")
		    .setMessage("Are you sure you want to logout?")
		    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(preference.getContext(), "Logout!", Toast.LENGTH_LONG)
					.show();
				}
		     }).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
			     })
		     .show();
			return true;
	    	}
		});
		
	
		Preference sound = (Preference) findPreference("soundOption");        
		sound.setOnPreferenceClickListener(new OnPreferenceClickListener() {

		    public boolean onPreferenceClick(Preference preference) {
		    	
		    	//SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(OptionsActivity.this);
		    	return true; 
		    }
		});

		
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
		if (music)
			MusicManager.start(this, MusicManager.CURRENT_MUSIC);
	}

}