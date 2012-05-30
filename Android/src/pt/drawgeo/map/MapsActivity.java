package pt.drawgeo.map;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import pt.drawgeo.canvas.CanvasActivity;
import pt.drawgeo.utility.Configurations;
import pt.drawgeo.utility.Connection;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.main.R;

public class MapsActivity extends MapActivity 
{    

	private LocationManager locationManager;
	private Dialog dialog;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Modo fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.map);
        
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        	  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        		startActivity(intent);
        	}
        
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
              // Called when a new location is found by the network location provider.
 
        		MapView mapView = (MapView)findViewById(R.id.mapview); 
            	List<Overlay> mapOverlays = mapView.getOverlays();
            	
            	// a minha posição
            	GeoPoint point = new GeoPoint((int)(location.getLatitude() * 1E6),((int)(location.getLongitude() * 1E6)));

            	MapCircleOverlay avaliable = new MapCircleOverlay(point, Configurations.AVALIABLE_RADIUS, 0,0,255, 32);
            	mapOverlays.add(avaliable);
            	
            	MapCircleOverlay me = new MapCircleOverlay(point, 3,255,0,0, 255);
            	mapOverlays.add(me);
            	
        		// desenhos perto de mim
            	Uri uri = new Uri.Builder()
                .scheme(Configurations.SCHEME)
                .authority(Configurations.AUTHORITY)
                .path(Configurations.GETBYCOORDINATES)
                .appendQueryParameter("lat", location.getLatitude()+"")
                .appendQueryParameter("long", location.getLongitude()+"")
                .appendQueryParameter("radius", Configurations.SEARCH_RADIUS + "")
                .appendQueryParameter("format", Configurations.FORMAT)
                .build();
            	
            	// são representados os desafios próximos de mim
            	String response = null;
            	try {
					response = Connection.getJSONLine(uri);
					JSONArray info = new JSONArray(response);
					Drawable drawableDraw = MapsActivity.this.getResources().getDrawable(R.drawable.pencil);
					Drawable drawableChallenge = MapsActivity.this.getResources().getDrawable(R.drawable.pencilchallenge);
	            	MapChallenge itemizedoverlayDraw = new MapChallenge(drawableDraw, MapsActivity.this);
	            	MapChallenge itemizedoverlayChallenge = new MapChallenge(drawableChallenge, MapsActivity.this);
					
					for(int i = 0; i < info.length(); i++) {
						JSONObject o = info.getJSONObject(i);
		            	GeoPoint point2 = new GeoPoint((int)(o.getDouble("latitude") * 1E6),((int)(o.getDouble("longitude") * 1E6)));
		            	OverlayItem overlay = new OverlayItem(point2, "Draw", o.getString("creator_email"));
		            	
		            	if (!o.getBoolean("challenge"))
		            	{
		            		itemizedoverlayDraw.addOverlay(overlay);
		            		itemizedoverlayDraw.addItem(o.getString("id"));
		            		itemizedoverlayDraw.allPiggies.add(o.getString("piggies"));
		            		itemizedoverlayDraw.allNumguess.add(o.getString("times_guessed"));
		            		mapOverlays.add(itemizedoverlayDraw);
		            	}
		            	else
		            	{
		            		itemizedoverlayChallenge.addOverlay(overlay);
		            		itemizedoverlayChallenge.addItem(o.getString("id"));
		            		itemizedoverlayChallenge.allPiggies.add(o.getString("piggies"));
		            		itemizedoverlayChallenge.allNumguess.add(o.getString("times_guessed"));
		            		mapOverlays.add(itemizedoverlayChallenge);
		            	}  	

					}

				} catch (Exception e) {} 

			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}

			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}
		};

		// regista o listener para as mudanças de localização
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

		getMockLocation();

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	// cria uma localização fictícia
	private void getMockLocation()
	{
		try{
			locationManager.removeTestProvider(LocationManager.GPS_PROVIDER);    		
		}
		catch(Exception e){}

		locationManager.addTestProvider
		(
				LocationManager.GPS_PROVIDER,
				"requiresNetwork" == "",
				"requiresSatellite" == "",
				"requiresCell" == "",
				"hasMonetaryCost" == "",
				"supportsAltitude" == "",
				"supportsSpeed" == "",
				"supportsBearing" == "",

				android.location.Criteria.POWER_LOW,
				android.location.Criteria.ACCURACY_FINE
				);      

		Location newLocation = new Location(LocationManager.GPS_PROVIDER);


		newLocation.setLatitude(41.1405161);
		newLocation.setLongitude(-8.6366445);

		newLocation.setAccuracy(500);

		locationManager.setTestProviderEnabled
		(
				LocationManager.GPS_PROVIDER, 
				true
				);

		locationManager.setTestProviderStatus
		(
				LocationManager.GPS_PROVIDER,
				LocationProvider.AVAILABLE,
				null,
				System.currentTimeMillis()
				);      

		locationManager.setTestProviderLocation
		(
				LocationManager.GPS_PROVIDER, 
				newLocation
				);      
	}

	// menu de contexto com as opções para criar novos desafios
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mapmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.newdraw:
		{

			dialog = ProgressDialog.show(MapsActivity.this, "", 
					"Retreiving information...", true);
			new getNewWordsTask().execute();

		
		}
		break;
		case R.id.newchallenge:
			//TODO
			break;
		}
		return true;
	}

	private class getNewWordsTask extends AsyncTask<Void, Integer, String[]> {

		@Override
		protected String[] doInBackground(Void... nothing) {
			Uri uri = new Uri.Builder()
			.scheme(Configurations.SCHEME)
			.authority(Configurations.AUTHORITY)
			.path(Configurations.GETNEWWORDS)
			.appendQueryParameter("format", Configurations.FORMAT)
			.build();
			String[] words = new String[3];
			String response = null;
			try {
				response = Connection.getJSONLine(uri);
				JSONObject info = new JSONObject(response);
				String status = info.getString("status");
				if(status.equals("Ok")) {
					JSONObject easy = info.optJSONObject("easy");

					if(easy!=null)
						words[0] = easy.getString("word");
					else
						words[0] = "null";
					JSONObject medium = info.optJSONObject("medium");
					if(medium!=null)
						words[1] = medium.getString("word");
					else
						words[1] = "null";

					JSONObject hard = info.optJSONObject("hard");
					if(hard!=null)

						words[2] = hard.getString("word");
					else
						words[2] = "null";



				}
			}
			catch (Exception e) {
				MapsActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(MapsActivity.this.getApplicationContext(), "Something went wrong. Try again...", Toast.LENGTH_SHORT).show();

					}
				});
				return null;
			}
			return words;
		}

		protected void onPostExecute(final String[] result) {
			dialog.dismiss();
			final Dialog wDialog = new Dialog(MapsActivity.this);
			wDialog.setTitle("Word chooser");
			wDialog.setContentView(R.layout.newworddialog);
			TextView text = (TextView) wDialog.findViewById(R.id.easyWord);
			text.setText(result[0]);
		
			TableRow tr = (TableRow) wDialog.findViewById(R.id.tableRow1);
			tr.setClickable(true);
			tr.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					
					Configurations.current_word = result[0];
					wDialog.dismiss();
					Intent intent = new Intent(v.getContext(),
    				CanvasActivity.class);
    				startActivity(intent);
				}
			});
			
			TextView text1 = (TextView) wDialog.findViewById(R.id.mediumWord);
			text1.setText(result[1]);
			
			TableRow tr1= (TableRow) wDialog.findViewById(R.id.tableRow2);
			
			tr1.setClickable(true);
			tr1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					
					Configurations.current_word = result[1];
					wDialog.dismiss();
					Intent intent = new Intent(v.getContext(),
    				CanvasActivity.class);
    				startActivity(intent);
				}
			});
			
			
			TextView text2 = (TextView) wDialog.findViewById(R.id.hardWord);
			text2.setText(result[2]);
			
			TableRow tr2= (TableRow) wDialog.findViewById(R.id.tableRow3);
			tr2.setClickable(true);
			tr2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					
					Configurations.current_word = result[2];
					wDialog.dismiss();
					Intent intent = new Intent(v.getContext(),
    				CanvasActivity.class);
    				startActivity(intent);
				}
			});
			
			wDialog.show();
		}
	}


}