package com.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;
     
public class OptionsActivity extends PreferenceActivity {

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
	}
	
}