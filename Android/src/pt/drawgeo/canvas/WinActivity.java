package pt.drawgeo.canvas;

import pt.drawgeo.map.GetNewWords;
import pt.drawgeo.map.MapsActivity;
import pt.drawgeo.sound.MusicManager;
import pt.drawgeo.sound.SoundManager;
import pt.drawgeo.utility.Configurations;

import com.main.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class WinActivity extends Activity {
	
	private AnimationDrawable animation;
	private AnimationDrawable animation2;
	private Dialog dialog;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Modo fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.win);
        
        ImageView coin = (ImageView) findViewById(R.id.coin);
		coin.setBackgroundResource(R.drawable.coinanimation);
		animation = (AnimationDrawable) coin.getBackground();
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		ImageView coin2 = (ImageView) findViewById(R.id.coin2);
		coin2.setBackgroundResource(R.drawable.coinanimation);
		animation2 = (AnimationDrawable) coin2.getBackground();
		
		Animation animation = new TranslateAnimation(0, 0,0, metrics.heightPixels/2);
		animation.setDuration(2000);
		animation.setFillAfter(true);
		animation.setRepeatCount(Animation.INFINITE);
		coin.startAnimation(animation);
		coin2.startAnimation(animation);
		
		final ImageView draw = (ImageView) findViewById(R.id.draw);
		final ImageView drawchallenge = (ImageView) findViewById(R.id.drawchallenge);
		final ImageView skip = (ImageView) findViewById(R.id.skip);
		draw.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog = ProgressDialog.show(WinActivity.this, "", 
						"Retreiving information...", true);
				
				GetNewWords gnw = new GetNewWords();
				gnw.finish = true;
				gnw.activity = WinActivity.this;
				gnw.replaceID = Integer.parseInt(Configurations.drawidreplay);
				gnw.dialog = dialog;
				gnw.execute();
				
				
			}
		});
		
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) {
    	  animation.start();
    	  animation2.start();
        }
	}

}
