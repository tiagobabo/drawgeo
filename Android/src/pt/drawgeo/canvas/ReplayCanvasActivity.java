package pt.drawgeo.canvas;

import org.json.JSONArray;
import org.json.JSONObject;

import pt.drawgeo.utility.Configurations;
import pt.drawgeo.utility.Connection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.main.R;

public class ReplayCanvasActivity extends Activity {
	
	private  DrawingPanel drawView; 
	private int lastLetterPos = 0;
	private String word = null;
	EditText[] guessLetters =  null;
	EditText[] usedLetters =  null;
	
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
		    word = info.getString("word");
		    TextView tv = (TextView) findViewById(R.id.title);
		    tv.setText(word);
		    addEnoughSquares(word.length());
	        JSONArray guess = info.getJSONArray("guess");
			EditText[] letters = new EditText[guess.length()];
			
			putLetters(letters);
			
			for(int i = 0; i <guess.length() ;i++){
				String l = guess.getString(i);
				letters[i].setText(l);
				letters[i].setClickable(true);
				letters[i].setOnClickListener(new LetterClickListener(letters[i]));
			}
			drawView.replay();
    	}
    	catch (Exception e){
    		
    	}
        
    }
    
    private void addEnoughSquares(int length) {
    	guessLetters =   new EditText[length];
    	usedLetters =   new EditText[length];
    	RelativeLayout guessLayout = (RelativeLayout) findViewById(R.id.guessLayout);
    	for(int i = 1; i <= length; i++){
	    	EditText et = new EditText(this);
	    	et.setId(i);
	    	
	    	
	    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( LayoutParams.WRAP_CONTENT, 40 );
	        if( i != 1 ){
	            params.addRule(RelativeLayout.RIGHT_OF, i-1 );
	            params.addRule(RelativeLayout.ALIGN_PARENT_TOP,1);
	        }
	        
	        et.setLayoutParams(params);
	        et.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_VERTICAL);
	        et.setEms(10);
	        et.setFocusable(false);
	        et.setWidth(35);
	        et.setClickable(true);
	        et.setOnClickListener(new GuessClickListener());
	        et.setText("");
	    	guessLayout.addView(et);
	    	
	    	guessLetters[i-1] = et;
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
    
    private class LetterClickListener implements View.OnClickListener
    {
    	
    	EditText et = null;
    	public LetterClickListener(EditText et){
    		this.et = et;
    	}
		public void onClick(View v) {
			
			if(lastLetterPos<guessLetters.length ){
				usedLetters[lastLetterPos] = et;
				guessLetters[lastLetterPos++].setText(et.getText());
				et.setVisibility(4);
				et.setClickable(false);
				
				
			}
			
			
		}
	}
    private class GuessClickListener implements View.OnClickListener
    {
    	
    	
    	
		public void onClick(View v) {
			
			int id = v.getId()-1;
			usedLetters[id].setVisibility(0);
			usedLetters[id].setClickable(true);
			guessLetters[id].setText("");
			lastLetterPos = Math.min(lastLetterPos, id);
			
			
			
		}
	}

	
    
}