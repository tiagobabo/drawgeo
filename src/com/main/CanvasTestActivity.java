package com.main;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CanvasTestActivity extends Activity {
	
	private  DrawingPanel drawView; 
	private ArrayList<ImageView> colors;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);
        
        LinearLayout canvas = (LinearLayout) findViewById(R.id.teste);
        
        drawView = new DrawingPanel(this);
        canvas.addView(drawView);
        
        ImageView clean = (ImageView) findViewById(R.id.clean);
        clean.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	drawView.cleanCanvas();
            }
        }); 
        
        ImageView eraser = (ImageView) findViewById(R.id.eraser);
        eraser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	for(ImageView color : colors)
            		setAlpha(color, 0.3f);
            	drawView.eraseMode();
            }
        });
        
        colors = new ArrayList<ImageView>();
        
        ImageView black = (ImageView) findViewById(R.id.black);
        createListener(black, Color.BLACK);
        colors.add(black);
        
        ImageView yellow = (ImageView) findViewById(R.id.yellow);
        createListener(yellow, Color.YELLOW);
        colors.add(yellow);
        setAlpha(yellow, 0.3f);
        
        ImageView green = (ImageView) findViewById(R.id.green);
        createListener(green, Color.GREEN);
        colors.add(green);
        setAlpha(green, 0.3f);
        
        ImageView red = (ImageView) findViewById(R.id.red);
        createListener(red, Color.RED);
        colors.add(red);
        setAlpha(red, 0.3f);
    }

    
    private static void setAlpha(View view, float alphaValue){

        if(alphaValue == 1){
            view.clearAnimation();
        }else{
            AlphaAnimation alpha = new AlphaAnimation(alphaValue, alphaValue);
            alpha.setDuration(0); // Make animation instant
            alpha.setFillAfter(true); // Tell it to persist after the animation ends        
            view.startAnimation(alpha);
        }

    }

    
	private void createListener(final ImageView view, final int color) {
		view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	for(ImageView color : colors)
            		setAlpha(color, 0.3f);
            	
            	setAlpha(view, 1.0f);
            	drawView.changeColor(color);
            }
        });	
	}
}