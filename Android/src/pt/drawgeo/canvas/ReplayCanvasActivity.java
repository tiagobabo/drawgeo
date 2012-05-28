package pt.drawgeo.canvas;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.main.R;

public class ReplayCanvasActivity extends Activity {
	
	private  DrawingPanel drawView; 
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Modo fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.replaycanvas);
        
        // layout que vai conter o canvas
        LinearLayout canvas = (LinearLayout) findViewById(R.id.canvas);
        
        // criação de um novo canvas
        drawView = new DrawingPanel(this);
        canvas.addView(drawView);
        
        drawView.replay();
    }
    
}