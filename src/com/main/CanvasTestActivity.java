package com.main;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CanvasTestActivity extends Activity {
	
	private  DrawingPanel drawView; 

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
            	drawView.eraseMode();
            }
        });
        
        ImageView black = (ImageView) findViewById(R.id.black);
        createListener(black, Color.BLACK);
        
        ImageView yellow = (ImageView) findViewById(R.id.yellow);
        createListener(yellow, Color.YELLOW);
        
        ImageView green = (ImageView) findViewById(R.id.green);
        createListener(green, Color.GREEN);
        
        ImageView red = (ImageView) findViewById(R.id.red);
        createListener(red, Color.RED);
    }

	private void createListener(ImageView view, final int color) {
		view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	drawView.changeColor(color);
            }

        });	
	}
}