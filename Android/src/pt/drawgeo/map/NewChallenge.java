package pt.drawgeo.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

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
				//TODO
			}
		});
        
    }
    
 

}
