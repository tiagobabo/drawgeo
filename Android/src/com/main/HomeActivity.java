package com.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;


public class HomeActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		final Button fButton = (Button) findViewById(R.id.loginFacebook);
		fButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//TODO facebook login
				goToMainMenu(v);
			}
		});
		final Button eButton = (Button) findViewById(R.id.loginEmail);
		eButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//emailLogin(HomeActivity.this);
				goToMainMenu(v);
			}
		});
	}
	protected void goToMainMenu(View v) {
		Intent intent = new Intent(v.getContext(),
				MainMenuActivity.class);
		startActivity(intent);
		
	}
	public static void emailLogin(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String account = prefs.getString("accountchooser",null);
		if (account == null) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("Please choose an account to synchonize in the configurations!")
			       .setCancelable(false)
			       .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			                
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	

}
