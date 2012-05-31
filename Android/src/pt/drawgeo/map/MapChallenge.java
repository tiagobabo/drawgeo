package pt.drawgeo.map;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import pt.drawgeo.canvas.ReplayCanvasActivity;
import pt.drawgeo.utility.Configurations;
import pt.drawgeo.utility.MD5Util;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import com.main.R;

@SuppressWarnings("rawtypes")
public class MapChallenge extends ItemizedOverlay {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	public ArrayList<String> allPiggies = new ArrayList<String>();
	public ArrayList<String> allNumguess = new ArrayList<String>();
	private ArrayList<String> ids = new ArrayList<String>();
	private Context mContext;
	private Dialog dialog;
	private Bitmap bmp;
	private ProgressDialog pd;

	public MapChallenge(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		this.mContext = context;
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

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected boolean onTap(final int index) {
		OverlayItem item = mOverlays.get(index);
		dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.challengedialog);

		TextView title = (TextView) dialog.findViewById(R.id.title);
		title.setText(item.getTitle());
		
		TextView piggies = (TextView) dialog.findViewById(R.id.piggies);
		piggies.setText(allPiggies.get(index));
		
		TextView numguess = (TextView) dialog.findViewById(R.id.numguess);
		numguess.setText(allNumguess.get(index));

		final ImageView pButton = (ImageView) dialog.findViewById(R.id.playnow);
		pButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						ReplayCanvasActivity.class);

				Configurations.drawidreplay = ids.get(index);
				dialog.dismiss();
				mContext.startActivity(intent);
			}
		});
		
		pd = new ProgressDialog(mContext);
		pd.setMessage("Retrieving information...");
		pd.show();
		
		
		new DownloadAvatar().execute(item.getSnippet());

		//dialog.show();
		return true;
	}

	public void addItem(String string) {
		ids.add(string);
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
	  		final ImageView avatar = (ImageView) dialog
					.findViewById(R.id.avatar);
			avatar.setImageBitmap(bmp);
			dialog.show();
			pd.dismiss();
		}
	}
}


