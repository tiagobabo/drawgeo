package pt.drawgeo.main;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import pt.drawgeo.utility.Configurations;
import pt.drawgeo.utility.Connection;
import pt.drawgeo.utility.MD5Util;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
	Boolean loginFacebook = false;
	
	// ecrã de loading para o pedido da informação do utilizador
	ProgressDialog dialog;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Modo fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

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
				                        final String email = obj.optString("email");
				                        final String id = obj.optString("id");
				                        final String name = obj.optString("name");
				                        
				                        HomeActivity.this.runOnUiThread(new Runnable() {
				                        	  public void run() {
				                        		  loginFacebook = true;
				                        		  dialog = ProgressDialog.show(HomeActivity.this, "", 
							                                "Retrieving information...", true);
				                        		  
				                        		  new DownloadFilesTask().execute(email, id, name);
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
				emailLogin();
			}
		});
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // depois do login, verifica as permissões da aplicação
        facebook.authorizeCallback(requestCode, resultCode, data);
        this.overridePendingTransition(R.anim.animation_enter2,
                R.anim.animation_leave2);
    }
	

	
	protected void goToMainMenu() {
		Intent intent = new Intent(this,
				MainMenuActivity.class);
		startActivityForResult(intent, 100);
		
		this.overridePendingTransition(R.anim.animation_enter,
                R.anim.animation_leave);
	}
	
	
	public void emailLogin() {

		final CharSequence[] possibleEmails = getPossibleEmails(HomeActivity.this);
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
	    builder.setTitle("Account chooser");
	    builder.setItems(possibleEmails, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface d, int email) {
	        	
	        	dialog = ProgressDialog.show(HomeActivity.this, "", 
                        "Retreiving information...", true);
	        
	        	new DownloadFilesTask().execute(possibleEmails[email].toString());
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
	
	private class DownloadFilesTask extends AsyncTask<String, Integer, Long> {

		@Override
		protected Long doInBackground(String... userInfo) {
			Uri uri = new Uri.Builder()
            .scheme(Configurations.SCHEME)
            .authority(Configurations.AUTHORITY)
            .path(Configurations.CHECKUSER)
            .appendQueryParameter("email", userInfo[0])              
            .appendQueryParameter("format", Configurations.FORMAT)
            .build();
        	
        	String response = null;
        	try {
				response = Connection.getJSONLine(uri);
				JSONObject info = new JSONObject(response);
				String status = info.getString("status");
				if(status.equals("Ok")) {
					JSONObject user = info.getJSONObject("user"); 
					Configurations.email = user.getString("email");
					Configurations.name = user.getString("name");
					Configurations.id = user.getInt("id");
					Configurations.keys = user.getInt("keys");
					Configurations.num_done = user.getInt("num_done");
					Configurations.num_success = user.getInt("num_success");
					Configurations.num_created = info.getInt("created");
					Configurations.piggies = user.getInt("piggies");
					//JSONObject avatar = info.getJSONObject("avatar"); 
					//Configurations.avatarURL = avatar.getString("url");
					
					if(loginFacebook) {
						Configurations.avatarURL = "https://graph.facebook.com/" + userInfo[1] + "/picture";
						Configurations.name = userInfo[2];
						loginFacebook = false;
					}
					else
						Configurations.avatarURL = "http://www.gravatar.com/avatar/" + MD5Util.md5Hex(Configurations.email) + "?s=100&d=identicon&r=PG";
					
					URL connectURL = new URL(Configurations.avatarURL);
					HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection(); 

					// do some setup
					conn.setDoInput(true); 
					conn.setDoOutput(true); 
					conn.setUseCaches(false); 
					conn.setRequestMethod("GET"); 

					// connect and flush the request out
					//conn.connect();
					
					InputStream is = conn.getInputStream();
					BufferedInputStream bis = new BufferedInputStream(is, 8 * 1024);
					Bitmap bmp = BitmapFactory.decodeStream(bis);
					bis.close();
					is.close(); 
					
					Configurations.avatarImage = bmp;

				}
		}
	    catch (Exception e) {
	    	HomeActivity.this.runOnUiThread(new Runnable() {
          	  public void run() {
          		Toast.makeText(HomeActivity.this.getApplicationContext(), "Something went wrong. Try again...", Toast.LENGTH_SHORT).show();
          		
          	  }
	    	});
	    	return (long) 1;
	    }
			return null;
		}
		
		protected void onPostExecute(Long result) {
          		dialog.dismiss();
          		if(result == null)
          			goToMainMenu();
	     }
	}


}
