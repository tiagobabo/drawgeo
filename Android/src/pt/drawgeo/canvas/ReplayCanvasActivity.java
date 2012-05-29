package pt.drawgeo.canvas;

import org.json.JSONArray;
import org.json.JSONObject;

import pt.drawgeo.utility.Configurations;
import pt.drawgeo.utility.Connection;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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
       
        
    	Uri uri = new Uri.Builder()
        .scheme(Configurations.SCHEME)
        .authority(Configurations.AUTHORITY)
        .path(Configurations.GETDRAW+"/" + Configurations.drawidreplay)          
        .appendQueryParameter("format", Configurations.FORMAT)
        .build();
    	
    	String response = null;
    	
    	try {
			response = Connection.getJSONLine(uri);
			JSONObject info = new JSONObject(response);
			JSONObject draw = info.getJSONObject("draw");
			String colorsString = draw.getString("draw");
			String xsString = draw.getString("drawx");
			String ysString = draw.getString("drawy");
			drawView = new DrawingPanel(this,colorsString,xsString,ysString);
		    canvas.addView(drawView);
        
	        JSONArray guess = info.getJSONArray("guess");
			EditText[] letters = new EditText[guess.length()];
			
			putLetters(letters);
			
			for(int i = 0; i <guess.length() ;i++)
				letters[i].setText(guess.getString(i));
			drawView.replay();
    	}
    	catch (Exception e){
    		
    	}
        
    }
    
    private void putLetters(EditText[] letters) {
		
		letters[0] =  (EditText) findViewById(R.id.char0);
		letters[1] =  (EditText) findViewById(R.id.char1);
		letters[2] =  (EditText) findViewById(R.id.char2);
		letters[3] =  (EditText) findViewById(R.id.char3);
		letters[4] =  (EditText) findViewById(R.id.char4);
		letters[5] =  (EditText) findViewById(R.id.char5);
		letters[6] =  (EditText) findViewById(R.id.char6);
		letters[7] =  (EditText) findViewById(R.id.char7);
		letters[8] =  (EditText) findViewById(R.id.char8);
		letters[9] =  (EditText) findViewById(R.id.char9);
		letters[10] =  (EditText) findViewById(R.id.char10);
		letters[11] =  (EditText) findViewById(R.id.char11);
		letters[12] =  (EditText) findViewById(R.id.char12);
		letters[13] =  (EditText) findViewById(R.id.char13);
		letters[14] =  (EditText) findViewById(R.id.char14);
		letters[15] =  (EditText) findViewById(R.id.char15);
		
	}
    
}