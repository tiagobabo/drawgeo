package pt.drawgeo.main;

import pt.drawgeo.map.MapsActivity;
import pt.drawgeo.utility.Configurations;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

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
				/*Intent intent = new Intent(v.getContext(),
						ReplayCanvasActivity.class);*/
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
				
				startActivityForResult(intent,100);
				MainMenuActivity.this.overridePendingTransition(R.anim.animation_enter,
		                R.anim.animation_leave);
				
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
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.overridePendingTransition(R.anim.animation_enter2,
                R.anim.animation_leave2);
    }
	
}
