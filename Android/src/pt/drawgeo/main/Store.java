package pt.drawgeo.main;

import org.json.JSONArray;
import org.json.JSONObject;

import pt.drawgeo.sound.MusicManager;
import pt.drawgeo.utility.Configurations;
import pt.drawgeo.utility.Connection;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.main.R;

public class Store extends Activity {

	public boolean music = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Modo fullscreen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.store);

		final TextView piggies = (TextView) findViewById(R.id.piggiestext);
		piggies.setText(Configurations.piggies + "");

		int[] locks = { R.id.lock2, R.id.lock3 };
		
		for (int i = 0; i < locks.length; i++) {
			final int serverID = i+2;
			final int it = i;
			final ImageView lock2 = (ImageView) findViewById(locks[i]);
			lock2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					if (Configurations.piggies < 30)
						Toast.makeText(Store.this,
								"You don't have enough piggies!",
								Toast.LENGTH_SHORT).show();
					else {
						AlertDialog.Builder builder = new AlertDialog.Builder(v
								.getContext());
						builder.setMessage(
								"You are about to buy this pallete. Are you sure?")
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {

												Uri uri = new Uri.Builder()
														.scheme(Configurations.SCHEME)
														.authority(
																Configurations.AUTHORITY)
														.path(Configurations.BUYPALLETE)
														.appendQueryParameter(
																"id",
																Configurations.id
																		+ "")
														.appendQueryParameter(
																"id_palette",
																serverID + "")
														.appendQueryParameter(
																"format",
																Configurations.FORMAT)
														.build();

												String response = null;

												try {
													response = Connection
															.getJSONLine(uri);
													JSONObject info = new JSONObject(
															response);
													String status = info
															.getString("status");
													if (status
															.equals("Palette bought successfully.")) {
														
														lock2.setImageResource(R.drawable.path3);

														piggies.setText((Configurations.piggies - 30-it*10)
																+ "");
														Toast.makeText(
																Store.this,
																"Palette bought successfully.",
																Toast.LENGTH_SHORT)
																.show();

													}
												} catch (Exception e) {} 
											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
											}
										});
						AlertDialog alert = builder.create();
						alert.show();
					}
				}
			});
		}
		
		Uri uri = new Uri.Builder().scheme(Configurations.SCHEME)
				.authority(Configurations.AUTHORITY)
				.path(Configurations.GETPALETTEBYUSER)
				.appendQueryParameter("id", Configurations.id + "")
				.appendQueryParameter("format", Configurations.FORMAT).build();

		String response = null;
		
		try {
			response = Connection.getJSONLine(uri);
			JSONArray info = new JSONArray(response);

			for (int i = 0; i < info.length(); i++) {
				JSONObject obj = info.getJSONObject(i);
				int id = Integer.parseInt(obj.getString("id"));
				final ImageView lock = (ImageView) findViewById(locks[id-2]);
				lock.setImageResource(R.drawable.path3);
				lock.setClickable(false);
			}
		}
		catch (Exception e) {} 

	}

	@Override
	protected void onPause() {
		super.onPause();
		MusicManager.PAUSED = true;
		MusicManager.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (music)
			MusicManager.start(this, MusicManager.CURRENT_MUSIC);
	}

}
