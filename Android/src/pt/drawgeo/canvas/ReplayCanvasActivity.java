package pt.drawgeo.canvas;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import pt.drawgeo.main.HomeActivity;
import pt.drawgeo.map.MapsActivity;
import pt.drawgeo.utility.Configurations;
import pt.drawgeo.utility.Connection;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.main.R;

public class ReplayCanvasActivity extends Activity {
	
	private  DrawingPanel drawView; 
	private int lastLetterPos = 0;
	private String word = null;
	EditText[] guessLetters =  null;
	EditText[] usedLetters =  null;
	
	private int piggiesEarned = -1;
	
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
		   
		    word = info.getString("word");
		    piggiesEarned = info.getInt("piggies");
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
			canvas.addView(drawView);
			drawView.replay();
    	}
    	catch (Exception e){
    		
    	}
        
    }
    
    private void addEnoughSquares(int length) {
    	guessLetters =   new EditText[length];
    	usedLetters =   new EditText[length];
    	RelativeLayout guessLayout = (RelativeLayout) findViewById(R.id.guessLayout);
    	float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
    	float ems = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
    	for(int i = 1; i <= length; i++){
	    	EditText et = new EditText(this);
	    	et.setId(i);
	    	
	    	
	    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( LayoutParams.WRAP_CONTENT, (int)px );
	        if( i != 1 ){
	            params.addRule(RelativeLayout.RIGHT_OF, i-1 );
	            params.addRule(RelativeLayout.ALIGN_PARENT_TOP,1);
	        }
	        
	        et.setLayoutParams(params);
	        et.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_VERTICAL);
	        et.setEms((int)ems);
	        et.setFocusable(false);
	        et.setWidth((int)px);
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
			
			if(lastLetterPos<guessLetters.length  ){
				usedLetters[lastLetterPos] = et;
				guessLetters[lastLetterPos++].setText(et.getText());
				et.setVisibility(4);
				et.setClickable(false);
				
				while(lastLetterPos<guessLetters.length && !guessLetters[lastLetterPos].getText().toString().equals(""))
					lastLetterPos++;
			
				if(lastLetterPos==guessLetters.length){
					if(isGuessCorrect()){
						Timer t = new Timer(); 
						final Handler handler = new Handler(); 
				        t.schedule(new TimerTask() { 
				                public void run() { 
				                	handler.post(new Runnable() { 
		                                public void run() { 
		                                playerGuessed(); 
		                                } 
		                        }); 
				                } 
				        }, 700); 
					}
					else
						playerFailed();
				}
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
	public boolean isGuessCorrect() {

		String guess = "";
		for(int i = 0; i < usedLetters.length; i++)
			guess+=usedLetters[i].getText().toString();
		
		return guess.equals(word);
		
	}

	public void playerGuessed() {

		
		
		final Dialog wDialog = new Dialog(ReplayCanvasActivity.this);
		wDialog.setTitle("Congratulations!!!");
		wDialog.setContentView(R.layout.wordguesseddialog);
		TextView text = (TextView) wDialog.findViewById(R.id.title);
		text.setText("You just guessed the word! You earned " + piggiesEarned + " piggies!! Do you want to replace this drawing with one of your own?");
		
		text.postInvalidate();
		Button btnYes = (Button) wDialog.findViewById(R.id.btnYes);
		btnYes.setClickable(true);
		btnYes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				
				wDialog.dismiss();
				Intent intent = new Intent(v.getContext(),
				CanvasActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		Button btnNo = (Button) wDialog.findViewById(R.id.btnNo);
		btnNo.setClickable(true);
		btnNo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				
				finish();
				wDialog.dismiss();
				
			}
		});
		
		wDialog.show();
	
		new sendSuccessTask().execute();
		
	}

	public void playerFailed() {

		ReplayCanvasActivity.this.runOnUiThread(new Runnable() {
      	  public void run() {
      		Toast.makeText(ReplayCanvasActivity.this.getApplicationContext(), "Daaaaamn you missed that one!",Toast.LENGTH_LONG).show();
      		
      	  }
	    	});
		
	}
	
	private class sendSuccessTask extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Void... nothing) {
			Uri uri = new Uri.Builder()
			.scheme(Configurations.SCHEME)
			.authority(Configurations.AUTHORITY)
			.path(Configurations.GUESS)
			.appendQueryParameter("format", Configurations.FORMAT)
			.appendQueryParameter("id", Configurations.id+"")
			.appendQueryParameter("draw_id", Configurations.drawidreplay)
			.appendQueryParameter("guess", word)
			.build();
			String response = null;
			try {
				response = Connection.getJSONLine(uri);
				JSONObject info = new JSONObject(response);
				String status = info.getString("status");
				if(status.equals("Ok")) {
					
				}
			}
			catch (Exception e) {
				ReplayCanvasActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(ReplayCanvasActivity.this.getApplicationContext(), "Something went wrong. Try again...", Toast.LENGTH_SHORT).show();

					}
				});
				return false;
			}
			return true;
		}

		protected void onPostExecute(final boolean succeess) {
			
		}
	}

	
    
}