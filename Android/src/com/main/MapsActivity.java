package com.main;

import com.google.android.maps.MapActivity;

import android.app.Activity;
import android.os.Bundle;
 
public class MapsActivity extends MapActivity 
{    
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
    }
 
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}