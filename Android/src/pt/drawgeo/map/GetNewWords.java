package pt.drawgeo.map;

import org.json.JSONObject;

import pt.drawgeo.canvas.CanvasActivity;
import pt.drawgeo.utility.Configurations;
import pt.drawgeo.utility.Connection;
import pt.drawgeo.utility.Word;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.main.R;

public class GetNewWords extends AsyncTask<Void, Integer, Word[]> {

	public Activity activity = null;
	public Dialog dialog = null;
	
	@Override
	protected Word[] doInBackground(Void... nothing) {
		Uri uri = new Uri.Builder().scheme(Configurations.SCHEME)
				.authority(Configurations.AUTHORITY)
				.path(Configurations.GETNEWWORDS)
				.appendQueryParameter("format", Configurations.FORMAT).build();
		Word[] words = new Word[3];
		String response = null;
		try {
			response = Connection.getJSONLine(uri);
			JSONObject info = new JSONObject(response);
			String status = info.getString("status");
			if (status.equals("Ok")) {
				JSONObject easy = info.optJSONObject("easy");
				Word easyW = easy == null ? null : new Word(easy);
				JSONObject medium = info.optJSONObject("medium");
				Word mediumW = medium == null ? null : new Word(medium);
				JSONObject hard = info.optJSONObject("hard");
				Word hardW = hard == null ? null : new Word(hard);
				words[0] = easyW;
				words[1] = mediumW;
				words[2] = hardW;
			}
		} catch (Exception e) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(activity.getApplicationContext(),
							"Something went wrong. Try again...",
							Toast.LENGTH_SHORT).show();

				}
			});
			return null;
		}
		return words;
	}

	protected void onPostExecute(final Word[] result) {
		dialog.dismiss();
		final Dialog wDialog = new Dialog(activity);
		wDialog.setTitle("Word chooser");
		wDialog.setContentView(R.layout.newworddialog);
		TextView text = (TextView) wDialog.findViewById(R.id.easyWord);
		text.setText(result[0].getWord());

		TableRow tr = (TableRow) wDialog.findViewById(R.id.tableRow1);
		tr.setClickable(true);
		tr.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Configurations.current_word = result[0];
				wDialog.dismiss();
				Intent intent = new Intent(v.getContext(), CanvasActivity.class);
				activity.startActivity(intent);
			}
		});

		TextView text1 = (TextView) wDialog.findViewById(R.id.mediumWord);
		text1.setText(result[1].getWord());

		TableRow tr1 = (TableRow) wDialog.findViewById(R.id.tableRow2);

		tr1.setClickable(true);
		tr1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Configurations.current_word = result[1];
				wDialog.dismiss();
				Intent intent = new Intent(v.getContext(), CanvasActivity.class);
				activity.startActivity(intent);
			}
		});

		TextView text2 = (TextView) wDialog.findViewById(R.id.hardWord);
		text2.setText(result[2].getWord());

		TableRow tr2 = (TableRow) wDialog.findViewById(R.id.tableRow3);
		tr2.setClickable(true);
		tr2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Configurations.current_word = result[2];
				wDialog.dismiss();
				Intent intent = new Intent(v.getContext(), CanvasActivity.class);
				activity.startActivity(intent);
			}
		});

		wDialog.show();
	}
}
