package pt.drawgeo.main;

import pt.drawgeo.sound.MusicManager;
import pt.drawgeo.utility.Configurations;

import com.main.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Store extends Activity{

	public boolean music = true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Modo fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.store);
        
        final TextView piggies = (TextView) findViewById(R.id.piggiestext);
		piggies.setText(Configurations.piggies+"");
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
