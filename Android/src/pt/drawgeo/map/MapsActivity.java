package pt.drawgeo.map;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import pt.drawgeo.utility.Configurations;
import pt.drawgeo.utility.Connection;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
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
	private List<Overlay> mapOverlays;
	private MapView mapView;
	private MapCircleOverlay avaliable;
	private MapCircleOverlay me;
	private Boolean firstTime = true;
	private Boolean noGPS = true;
	private GeoPoint point;
	private ArrayList<Location> locations = new ArrayList<Location>();
	private boolean isUpdateDone = true;
    
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
        
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        	  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        		startActivity(intent);
        }
        
        mapView = (MapView)findViewById(R.id.mapview); 
    	mapOverlays = mapView.getOverlays();
    	mapOverlays.clear();

    	mapView.getController().setZoom(19);
        
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
              // Called when a new location is found by the network location provider.
            	
            	if(location.getProvider().equals(LocationManager.GPS_PROVIDER) || noGPS)
            	{
            		if(location.getProvider().equals(LocationManager.GPS_PROVIDER))
            			noGPS = false;
	            	// a minha posi�‹o
	            	point = new GeoPoint((int)(location.getLatitude() * 1E6),((int)(location.getLongitude() * 1E6)));
	            	
	            	if(isCurrentLocationVisible(point) || firstTime) {
	            		mapView.getController().setCenter(point);
	            		firstTime = false;
	            		if(isUpdateDone){
	            			isUpdateDone = false;
	            			new GetDrawsNear().execute(point);
	            		}
	            	}
	            	
	            	Configurations.latitudenow = location.getLatitude(); 
	            	Configurations.longitudenow = location.getLongitude();
	            	
	            	mapOverlays.remove(avaliable);
	            	mapOverlays.remove(me);
	            	
	            	avaliable = new MapCircleOverlay(point, Configurations.AVALIABLE_RADIUS, 0,0,255, 32);
	            	mapOverlays.add(avaliable);
	            	
	            	me = new MapCircleOverlay(point, 3,255,0,0, 255);
	            	mapOverlays.add(me);
	            	
	            	
            	
            	}
            	
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}

			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}
		};
		


		// regista o listener para as mudan�as de localiza�‹o
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

		//getMockLocation();

	}
    
    private boolean isCurrentLocationVisible(GeoPoint p)
    {
        Rect currentMapBoundsRect = new Rect();
        Point currentDevicePosition = new Point();
        GeoPoint deviceLocation = new GeoPoint((int) (p.getLatitudeE6()), (int) (p.getLongitudeE6()));

        mapView.getProjection().toPixels(deviceLocation, currentDevicePosition);
        mapView.getDrawingRect(currentMapBoundsRect);

        return currentMapBoundsRect.contains(currentDevicePosition.x, currentDevicePosition.y);

    }
    
    private class GetDrawsNear extends AsyncTask<GeoPoint, Integer, Long> {
		protected Long doInBackground(GeoPoint... locations) {
        	
        	// desenhos perto de mim
        	Uri uri = new Uri.Builder()
            .scheme(Configurations.SCHEME)
            .authority(Configurations.AUTHORITY)
            .path(Configurations.GETBYCOORDINATES)
            .appendQueryParameter("lat", locations[0].getLatitudeE6()/1e6+"")
            .appendQueryParameter("long", locations[0].getLongitudeE6()/1e6+"")
            .appendQueryParameter("radius", Configurations.SEARCH_RADIUS + "")
            .appendQueryParameter("id", Configurations.id + "")
            .appendQueryParameter("format", Configurations.FORMAT)
            .build();
        	
        	// s‹o representados os desafios pr—ximos de mim
        	String response = null;
        	try {
				response = Connection.getJSONLine(uri);
				JSONArray info = new JSONArray(response);
				Drawable drawableDraw = MapsActivity.this.getResources().getDrawable(R.drawable.pencil);
				Drawable drawableChallenge = MapsActivity.this.getResources().getDrawable(R.drawable.pencilchallenge);
				Location l = new Location("");
				l.setLatitude(locations[0].getLatitudeE6()/1e6);
				l.setLongitude(locations[0].getLongitudeE6()/1e6);
            	MapChallenge itemizedoverlayDraw = new MapChallenge(drawableDraw, MapsActivity.this, l,false);
            	MapChallenge itemizedoverlayChallenge = new MapChallenge(drawableChallenge, MapsActivity.this, l,true);
            	
            	mapOverlays.clear();
            	
            	avaliable = new MapCircleOverlay(point, Configurations.AVALIABLE_RADIUS, 0,0,255, 32);
            	mapOverlays.add(avaliable);
            	
            	me = new MapCircleOverlay(point, 3,255,0,0, 255);
            	mapOverlays.add(me);
            	
				for(int i = 0; i < info.length(); i++) {
					JSONObject o = info.getJSONObject(i);
					boolean c = o.getBoolean("challenge");
					String title = c? "Challenge" : "Draw";
					
	            	GeoPoint point2 = new GeoPoint((int)(o.getDouble("latitude") * 1E6),((int)(o.getDouble("longitude") * 1E6)));
	            	OverlayItem overlay = new OverlayItem(point2, title , o.getString("creator_email"));
	            	
	            	Location loc = new Location("");
	            	loc.setLatitude(point2.getLatitudeE6()/1e6);
	            	loc.setLongitude(point2.getLongitudeE6()/1e6);
	            	
	            	MapsActivity.this.locations.add(loc);
	            	
	            	if (!c)
	            	{
	            		itemizedoverlayDraw.addOverlay(overlay, point2, o.getString("creator_email"));
	            		itemizedoverlayDraw.addItem(o.getString("id"));
	            		Challenge ch = new Challenge();
	            		ch.setPiggies(o.getString("piggies"));
	            		ch.setNumGuesses(o.getString("times_guessed"));
	            		ch.setCheck(o.getBoolean("check"));
	            		itemizedoverlayDraw.allChallenges.add(ch);
	            		
	   
	            		mapOverlays.add(itemizedoverlayDraw);
	            	}
	            	else
	            	{
	            		itemizedoverlayChallenge.addOverlay(overlay, point2, o.getString("creator_email"));
	            		itemizedoverlayChallenge.addItem(o.getString("id"));
	            		Challenge ch = new Challenge();
	            		ch.setPiggies(o.getString("piggies"));
	            		ch.setNumGuesses(o.getString("times_guessed"));
	            		ch.setCheck(o.getBoolean("check"));
	            		ch.setDescription(o.getString("description"));
	            		ch.setPassword(o.getString("password"));
	            		itemizedoverlayChallenge.allChallenges.add(ch);
	            		mapOverlays.add(itemizedoverlayChallenge);
	            	}  	

				}
				
				mapView.postInvalidate();

			} catch (Exception e) { isUpdateDone = true;} 
        	isUpdateDone = true;
            return null;
        }
    }
    
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	// cria uma localiza�‹o fict’cia
	/*private void getMockLocation()
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
	}*/

	// menu de contexto com as op�›es para criar novos desafios
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mapmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(Configurations.latitudenow != -1.0)
		{
			
			Location here = new Location("");
			here.setLatitude(Configurations.latitudenow);
			here.setLongitude(Configurations.longitudenow);
			
			/*for(Location l : locations) {
				if(l.distanceTo(here) < Configurations.MINIMUM_RADIUS) {
					Toast.makeText(MapsActivity.this.getApplicationContext(), "You can't add it here, because there is another challenge in this area...", Toast.LENGTH_LONG).show();
					return false;
				}
			}*/
			
			switch (item.getItemId()) {
			case R.id.newdraw:
			{
	
				dialog = ProgressDialog.show(MapsActivity.this, "", 
						"Retreiving information...", true);
				
				GetNewWords gnw = new GetNewWords();
				gnw.activity = this;
				gnw.dialog = this.dialog;
				gnw.execute();
	
			
			}
			break;
			case R.id.newchallenge:
				Intent intent = new Intent(this,
				NewChallenge.class);
		startActivity(intent);
				break;
			}
		}
		else {
			Toast.makeText(MapsActivity.this.getApplicationContext(), "Waiting for location...", Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	
	@Override
	protected void onResume()
	{
		super.onResume();
		if(point != null)
			new GetDrawsNear().execute(point);
	}
}