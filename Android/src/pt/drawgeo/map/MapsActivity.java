package pt.drawgeo.map;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.drawgeo.utility.Configurations;
import pt.drawgeo.utility.Connection;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.facebook.android.FacebookError;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.main.R;
 
public class MapsActivity extends MapActivity 
{    
	
	private LocationManager locationManager;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
            	Toast.makeText(MapsActivity.this.getApplicationContext(), location.getLongitude()+"", Toast.LENGTH_SHORT).show();

        		MapView mapView = (MapView)findViewById(R.id.mapview); 
            	List<Overlay> mapOverlays = mapView.getOverlays();
            	
            	// a minha posição
            	Drawable drawable = MapsActivity.this.getResources().getDrawable(R.drawable.myposition);
            	MapChallenge itemizedoverlay = new MapChallenge(drawable, MapsActivity.this);
            	
            	GeoPoint point = new GeoPoint((int)(location.getLatitude() * 1E6),((int)(location.getLongitude() * 1E6)));
            	OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm here!");

            	itemizedoverlay.addOverlay(overlayitem);
            	mapOverlays.add(itemizedoverlay);
            	
            	RadiusOverlay radiusOverlay = new RadiusOverlay(MapsActivity.this, location.getLatitude(), location.getLongitude(), Configurations.AVALIABLE_RADIUS);
            	mapOverlays.add(radiusOverlay);
            	
        		// desenhos perto de mim
            	
            	Uri uri = new Uri.Builder()
                .scheme("http")
                .authority("drawgeo.herokuapp.com")
                .path("radius/getByCoordinates")
                .appendQueryParameter("lat", location.getLatitude()+"")
                .appendQueryParameter("long", location.getLongitude()+"")
                .appendQueryParameter("radius", Configurations.SEARCH_RADIUS + "")
                .appendQueryParameter("format", "json")
                .build();

            	String response = null;
            	try {
					response = Connection.getJSONLine(uri);
					JSONArray info = new JSONArray(response);
					Drawable drawable2 = MapsActivity.this.getResources().getDrawable(R.drawable.pencil);
	            	MapChallenge itemizedoverlay2 = new MapChallenge(drawable2, MapsActivity.this);
					
					for(int i = 0; i < info.length(); i++) {
						JSONObject o = info.getJSONObject(i);
		            	GeoPoint point2 = new GeoPoint((int)(o.getDouble("latitude") * 1E6),((int)(o.getDouble("longitude") * 1E6)));
		            	OverlayItem overlayitem2 = new OverlayItem(point2, o.getString("draw"), o.getString("description"));
		            	itemizedoverlay2.addOverlay(overlayitem2);
		            	mapOverlays.add(itemizedoverlay2);
					}

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FacebookError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
          };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        
        getMockLocation();

    }
 
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
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

    
    
}