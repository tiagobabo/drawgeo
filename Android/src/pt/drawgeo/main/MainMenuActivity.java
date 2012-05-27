package pt.drawgeo.main;

import pt.drawgeo.map.MapsActivity;

import com.main.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MainMenuActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Modo fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		 
		setContentView(R.layout.mainmenu);
		
		final ImageView pButton = (ImageView) findViewById(R.id.btnPlay);
		pButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						MapsActivity.class);
				startActivity(intent);
			}
		});
		final ImageView sButton = (ImageView) findViewById(R.id.btnStore);
		sButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						Store.class);
				startActivity(intent);
			}
		});
		
		final ImageView aButton = (ImageView) findViewById(R.id.btnAbout);
		aButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showAbout();
			}
		});
		final ImageView oButton = (ImageView) findViewById(R.id.btnOptions);
		oButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
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
	
}
