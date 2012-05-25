package com.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);
		
		final Button pButton = (Button) findViewById(R.id.btnPlay);
		pButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				
			}
		});
		final Button sButton = (Button) findViewById(R.id.btnStore);
		sButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						Store.class);
				startActivity(intent);
			}
		});
		
		final Button aButton = (Button) findViewById(R.id.btnAbout);
		aButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showAbout();
			}
		});
		final Button oButton = (Button) findViewById(R.id.btnOptions);
		oButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
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

}
