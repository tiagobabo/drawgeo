package com.main;

import com.facebook.android.*;
import com.facebook.android.Facebook.*;

import android.accounts.Account;
import android.accounts.AccountManager;
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
import android.widget.Toast;


public class HomeActivity extends Activity{
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		final Button fButton = (Button) findViewById(R.id.loginFacebook);
		fButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Facebook facebook = new Facebook("374936152553880");
				facebook.authorize(HomeActivity.this, new DialogListener() {

					public void onComplete(Bundle values) {
						// TODO Auto-generated method stub
						
					}

					public void onFacebookError(FacebookError e) {
						// TODO Auto-generated method stub
						
					}

					public void onError(DialogError e) {
						// TODO Auto-generated method stub
						
					}

					public void onCancel() {
						// TODO Auto-generated method stub
						
					}
		           
		        });
				
				//goToMainMenu(v);
				
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
	
	
	
	public static void emailLogin(final Context context) {

		final CharSequence[] possibleEmails = getPossibleEmails(context);
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder.setTitle("Account chooser");
	    builder.setItems(possibleEmails, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int item) {
	        	Toast.makeText(context, possibleEmails[item], Toast.LENGTH_SHORT).show();
	        	//login
	        }
	    }).show();
	
	}
	private static CharSequence[] getPossibleEmails(final Context context) {
		AccountManager manager = AccountManager.get(context); 
	    Account[] accounts = manager.getAccountsByType("com.google"); 
	    CharSequence[] possibleEmails = new CharSequence[accounts.length];

	    int i = 0;
	    for (Account account : accounts) {
	      possibleEmails[i] = account.name;
	      i++;
	    }
		return possibleEmails;
	}
	

}
