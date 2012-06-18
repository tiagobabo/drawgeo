package pt.drawgeo.map;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import pt.drawgeo.canvas.ReplayCanvasActivity;
import pt.drawgeo.utility.Configurations;
import pt.drawgeo.utility.Connection;
import pt.drawgeo.utility.MD5Util;
import pt.drawgeo.utility.Word;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import com.main.R;

@SuppressWarnings("rawtypes")
public class MapChallenge extends ItemizedOverlay {

	public ArrayList<Challenge> allChallenges = new ArrayList<Challenge>();
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private ArrayList<GeoPoint> mLocations = new ArrayList<GeoPoint>();
	private ArrayList<String> ids = new ArrayList<String>();
	private Context mContext;
	private Dialog dialog;
	private Bitmap bmp;
	private ProgressDialog pd;
	private Location location;
	private int currentIndex;
	private ArrayList<String> creatorEmails = new ArrayList<String>();
	private boolean isChallenge;

	public MapChallenge(Drawable defaultMarker, Context context, Location l,boolean isChallenge) {
		super(boundCenterBottom(defaultMarker));
		this.mContext = context;
		this.location = l;
		this.isChallenge = isChallenge;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	public void addOverlay(OverlayItem overlay, GeoPoint l, String email) {
		mLocations.add(l);
		creatorEmails.add(email);
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected boolean onTap(final int index) {
		OverlayItem item = mOverlays.get(index);
		final Challenge ch = allChallenges.get(index);
		dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.challengedialog);

		TextView title = (TextView) dialog.findViewById(R.id.title);
		title.setText(item.getTitle());
		
		TextView piggies = (TextView) dialog.findViewById(R.id.piggies);
		piggies.setText(ch.getPiggies());
		
		TextView numguess = (TextView) dialog.findViewById(R.id.numguess);
		numguess.setText(ch.getNumGuesses());

		final ImageView pButton = (ImageView) dialog.findViewById(R.id.playnow);
		pButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				dialog.dismiss();
				if(!isChallenge){
					Intent intent = new Intent(v.getContext(),
							ReplayCanvasActivity.class);

					Configurations.drawidreplay = ids.get(index);
					mContext.startActivity(intent);
				}
				else{
					Intent intent = new Intent(v.getContext(),
							GuessChallenge.class);
					Configurations.drawidreplay = ids.get(index);
					Configurations.current_description = ch.getDescription();
					Configurations.current_password = ch.getPassword();
					mContext.startActivity(intent);
				}
			}
		});
		
		pd = new ProgressDialog(mContext);
		pd.setMessage("Retrieving information...");
		pd.show();
		
		currentIndex = index;
		new DownloadAvatar().execute(item.getSnippet());
		
		
		//dialog.show();
		return true;
	}

	public void addItem(String string) {
		ids.add(string);
	}

	/**
	 * @return the isChallenge
	 */
	public boolean isChallenge() {
		return isChallenge;
	}

	/**
	 * @param isChallenge the isChallenge to set
	 */
	public void setChallenge(boolean isChallenge) {
		this.isChallenge = isChallenge;
	}

	private class DownloadAvatar extends AsyncTask<String, Integer, Long> {

		@Override
		protected Long doInBackground(String... userInfo) {
			Configurations.avatarURL = "http://www.gravatar.com/avatar/"
					+ MD5Util.md5Hex(userInfo[0]) + "?s=100&d=identicon&r=PG";

			URL connectURL;
			try {
				connectURL = new URL(Configurations.avatarURL);
				HttpURLConnection conn = (HttpURLConnection) connectURL
						.openConnection();

				// do some setup
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("GET");

				// connect and flush the request out
				InputStream is = conn.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is, 8 * 1024);
				bmp = BitmapFactory.decodeStream(bis);
				bis.close();
				is.close();

			} catch (MalformedURLException e) {}
			catch (IOException e) {}

			return null;
		}

		protected void onPostExecute(Long result) {
			
			if(bmp != null)
			{
				//Challenge ch = allChallenges.get(currentIndex);
		  		final ImageView avatar = (ImageView) dialog
						.findViewById(R.id.avatar);
				avatar.setImageBitmap(bmp);
				
				Location click = new Location("");
				GeoPoint clickPoint = mLocations.get(currentIndex);
				
				click.setLatitude(clickPoint.getLatitudeE6()/1e6);
				click.setLongitude(clickPoint.getLongitudeE6()/1e6);
				
		
				if(click.distanceTo(location) > Configurations.AVALIABLE_RADIUS || creatorEmails.get(currentIndex).equals(Configurations.email) || !allChallenges.get(currentIndex).isCheck()) {
					final ImageView playnow = (ImageView) dialog
							.findViewById(R.id.playnow);
					playnow.setImageResource(R.drawable.playnowbw);
					playnow.setClickable(false);
				}
				
				dialog.show();
				
			}
			else {
				Toast.makeText(mContext, "Something went wrong. Try again...", Toast.LENGTH_SHORT).show();
			}
			
			pd.dismiss();
		}
	}
}


