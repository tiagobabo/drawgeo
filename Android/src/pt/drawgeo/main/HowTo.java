package pt.drawgeo.main;

import pt.drawgeo.sound.MusicManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.main.R;

public class HowTo extends Activity{

	public boolean music = true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Modo fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.howto);
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
