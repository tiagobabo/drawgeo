package pt.drawgeo.canvas;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import pt.drawgeo.main.Store;
import pt.drawgeo.sound.MusicManager;
import pt.drawgeo.utility.Configurations;
import pt.drawgeo.utility.Connection;
import pt.drawgeo.utility.Word;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.R;

public class CanvasActivity extends Activity {

	private DrawingPanel drawView;
	private ArrayList<ImageView> colors;
	private int replaceId = -1;
	private Word word = null;
	private int[] colorsArray = {R.drawable.color5, R.drawable.color6, R.drawable.color7, 
			R.drawable.color8, R.drawable.color9, R.drawable.color10, 
			R.drawable.color11, R.drawable.color12, };
	private int[] colorsArrayC = {Color.rgb(255, 0, 255), Color.rgb(255, 102, 0), Color.rgb(128, 128, 0), 
			Color.rgb(0, 128, 128), Color.rgb(128, 0, 128),Color.rgb(0, 0, 255), 
			Color.rgb(128, 0, 0),Color.rgb(0, 128, 51), };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle b = this.getIntent().getExtras();
		if (!(b == null) && !b.isEmpty())
			replaceId = b.getInt("replaceID", -1);
		word = Configurations.current_word;
		// Modo fullscreen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.drawcanvas);
		TextView text2 = (TextView) findViewById(R.id.title);
		text2.setText("You are now drawing " + word.getWord());

		// layout que vai conter o canvas
		LinearLayout canvas = (LinearLayout) findViewById(R.id.canvas);

		// criação de um novo canvas
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		drawView = new DrawingPanel(this, metrics.widthPixels,
				metrics.heightPixels, replaceId, word);
		canvas.addView(drawView);

		// listener para a acção de desenho concluído
		final ImageView done = (ImageView) findViewById(R.id.done);
		done.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// drawView.replay();
				drawView.save();

			}
		});

		// listener para a acção de limpar o ecrã todo
		final ImageView clean = (ImageView) findViewById(R.id.clean);
		clean.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				drawView.cleanCanvas();
			}
		});

		colors = new ArrayList<ImageView>();

		// listener para a loja
		// listener para a acção de limpar o ecrã com a borracha
		final ImageView store = (ImageView) findViewById(R.id.store);

		store.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(CanvasActivity.this, Store.class);
				startActivity(i);
			}
		});

		// listener para a acção de limpar o ecrã com a borracha
		final ImageView eraser = (ImageView) findViewById(R.id.eraser);
		setAlpha(eraser, 0.3f);
		colors.add(eraser);
		eraser.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				for (ImageView color : colors)
					setAlpha(color, 0.3f);

				setAlpha(eraser, 1.0f);
				drawView.eraseMode();
			}
		});

		// criação do listener para as várias cores

		final ImageView black = (ImageView) findViewById(R.id.black);
		createListener(black, Color.BLACK);
		colors.add(black);

		final ImageView yellow = (ImageView) findViewById(R.id.yellow);
		createListener(yellow, Color.YELLOW);
		colors.add(yellow);
		setAlpha(yellow, 0.3f);

		final ImageView green = (ImageView) findViewById(R.id.green);
		createListener(green, Color.GREEN);
		colors.add(green);
		setAlpha(green, 0.3f);

		final ImageView red = (ImageView) findViewById(R.id.red);
		createListener(red, Color.RED);
		colors.add(red);
		setAlpha(red, 0.3f);
	}

	
	private void getPalettes() {
		
		Uri uri = new Uri.Builder().scheme(Configurations.SCHEME)
				.authority(Configurations.AUTHORITY)
				.path(Configurations.GETPALETTEBYUSER)
				.appendQueryParameter("id", Configurations.id + "")
				.appendQueryParameter("format", Configurations.FORMAT).build();

		String response = null;
		
		
		
		try {
			response = Connection.getJSONLine(uri);
			JSONArray info = new JSONArray(response);
			LinearLayout ll = (LinearLayout) findViewById(R.id.colors);
			int count = ll.getChildCount();
			for(int j = 4; j < count; j++)
				ll.removeViewAt(4);
			
			for (int i = 0; i < info.length(); i++) {
				JSONObject obj = info.getJSONObject(i);
				int id = Integer.parseInt(obj.getString("id"));
				for(int z = 0+4*(id-2); z < 4+4*(id-2); z++) {
					ImageView view = new ImageView(this);
					view.setImageResource(colorsArray[z]);
					int px = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
									.getDisplayMetrics());
					view.setPadding(px, 0, 0, 0);
					createListener(view,colorsArrayC[z]);
					colors.add(view);
					setAlpha(view, 0.3f);
					ll.addView(view);
				}
				
			}

		} catch (Exception e) {}
	}

	// função que adiciona o efeito alpha a uma ImageView
	private static void setAlpha(View view, float alphaValue) {
		if (alphaValue == 1) {
			view.clearAnimation();
		} else {
			AlphaAnimation alpha = new AlphaAnimation(alphaValue, alphaValue);
			alpha.setDuration(0);
			alpha.setFillAfter(true);
			view.startAnimation(alpha);
		}
	}

	// listener que muda o alpha da imagem que é seleccionada e muda a cor de
	// desenho
	private void createListener(final ImageView view, final int color) {
		view.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				for (ImageView color : colors)
					setAlpha(color, 0.3f);

				setAlpha(view, 1.0f);
				
				drawView.changeColor(color);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getPalettes(); 
	}
}