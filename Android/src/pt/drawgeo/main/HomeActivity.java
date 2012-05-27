package pt.drawgeo.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.main.R;


public class HomeActivity extends Activity{
	
	// objeto para a ligação com o Facebook
	final Facebook facebook = new Facebook("374936152553880");
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		final ImageView fButton = (ImageView) findViewById(R.id.loginFacebook);
		fButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// chamada à função de autenticação do facebook
				facebook.authorize(HomeActivity.this, new String[] { "email" }, new DialogListener() {

					public void onComplete(Bundle values) {
						// depois de completar o login e a verificação das permissões da app, passamos a ter acesso à informação do jogador
						// requisitamos os dados deste, para sabermos o seu e-mail
						 new AsyncFacebookRunner(facebook).request("/me", 
			                        new RequestListener () {
			                   
								public void onComplete(String response,
										Object state) {
									
									// a string está no formato JSON
									JSONObject obj;
									try {
										obj = Util.parseJson(response);
										
										// retiramos a informação que precisamos, neste caso, o email
				                        final String name = obj.optString("email");
				                        
				                        HomeActivity.this.runOnUiThread(new Runnable() {
				                        	  public void run() {
				                        		  Toast.makeText(HomeActivity.this, name, Toast.LENGTH_SHORT).show();
				                        	  }
			                        	});
									} catch (FacebookError e) {} 
									catch (JSONException e) {}
								}

								public void onIOException(IOException e,
										Object state) {}
								public void onFileNotFoundException(
										FileNotFoundException e, Object state) {}
								public void onMalformedURLException(
										MalformedURLException e, Object state) {}
								public void onFacebookError(FacebookError e,
										Object state) {}
			                }, null);
						
					}

					public void onFacebookError(FacebookError e) {}
					public void onError(DialogError e) {}
					public void onCancel() {}
		           
		        });
				
				
				
				//goToMainMenu(v);
				
			}
		});
		final ImageView eButton = (ImageView) findViewById(R.id.loginEmail);
		eButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//emailLogin(HomeActivity.this);
				goToMainMenu(v);
				
			}
		});
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // depois do login, verifica as permissões da aplicação
        facebook.authorizeCallback(requestCode, resultCode, data);
    }
	
	protected void goToMainMenu(View v) {
		Intent intent = new Intent(v.getContext(),
				MainMenuActivity.class);
		startActivity(intent);
		
	}
	
	
	
	public static void emailLogin(final Context context) {

		final CharSequence[] possibleEmails = getPossibleEmails(context);
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder.setTitle("Account chooser");
	    builder.setItems(possibleEmails, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int item) {
	        	Toast.makeText(context, possibleEmails[item], Toast.LENGTH_SHORT).show();
	        	//login
	        }
	    }).show();
	
	}
	private static CharSequence[] getPossibleEmails(final Context context) {
		AccountManager manager = AccountManager.get(context); 
	    Account[] accounts = manager.getAccountsByType("com.google"); 
	    CharSequence[] possibleEmails = new CharSequence[accounts.length];

	    int i = 0;
	    for (Account account : accounts) {
	      possibleEmails[i] = account.name;
	      i++;
	    }
		return possibleEmails;
	}
	

}
