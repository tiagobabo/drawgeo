package pt.drawgeo.map;

import pt.drawgeo.canvas.ReplayCanvasActivity;
import pt.drawgeo.utility.Configurations;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.main.R;

public class GuessChallenge extends Activity{

	public static Dialog dialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Modo fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.newchallenge);
        
        final ImageView eButton = (ImageView) findViewById(R.id.okButton);
        EditText description = (EditText) findViewById(R.id.description);
        description.setClickable(false);
        description.setEnabled(false);
        description.setText(Configurations.current_description);
		
		eButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText description = (EditText) findViewById(R.id.description);
				EditText password = (EditText) findViewById(R.id.password);
	
				if (description.getText().toString().length() == 0 || password.getText().toString().length() == 0)
					Toast.makeText(GuessChallenge.this.getApplicationContext(), "Fields cannot be empty...", Toast.LENGTH_SHORT).show();
				else
				{
					
					if(password.getText().toString().trim().equalsIgnoreCase(Configurations.current_password)){
						Intent intent = new Intent(v.getContext(),
								ReplayCanvasActivity.class);
						startActivity(intent);
						finish();
					}else
						Toast.makeText(GuessChallenge.this.getApplicationContext(), "Wrong password!", Toast.LENGTH_SHORT).show();
					
				}
				
				
			}
		});
        
    }
    
 

}
