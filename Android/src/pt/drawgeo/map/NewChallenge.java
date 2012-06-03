package pt.drawgeo.map;

import pt.drawgeo.utility.Configurations;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.main.R;

public class NewChallenge extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Modo fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.newchallenge);
        
        final ImageView eButton = (ImageView) findViewById(R.id.okButton);
		eButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView description = (TextView) findViewById(R.id.textView1);
				TextView password = (TextView) findViewById(R.id.textView2);
				if (description.getText().toString().length() == 0 || password.getText().toString().length() == 0)
					Toast.makeText(NewChallenge.this.getApplicationContext(), "Fields cannot be empty...", Toast.LENGTH_SHORT).show();
				else
				{
					Configurations.current_description = description.getText().toString();
					Configurations.current_password = password.getText().toString();
				}
				
			}
		});
        
    }
    
 

}
