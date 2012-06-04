package pt.drawgeo.canvas;

import com.main.R;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class WinActivity extends Activity {
	
	private AnimationDrawable animation;
	private AnimationDrawable animation2;
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
