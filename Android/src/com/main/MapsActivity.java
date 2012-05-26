package com.main;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
 
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
            	GeoPoint point = new GeoPoint(19240000,-99120000);
            	OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
            	MapView map = (MapView) findViewById(R.id.mapview);
            	MapController mc = map.getController();
            	mc.animateTo(point);
            	List<Overlay> mapOverlays = map.getOverlays();
            	//mapOverlays.add(overlayitem);
            	
           // 	Overlay a = new Overlay()
            	
            	
            	
            	
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

      
        newLocation.setLatitude(42.1405161);
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