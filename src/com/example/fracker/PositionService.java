package com.example.fracker;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class PositionService extends Service{

	protected LocationManager locationManager;
	protected LocationListener locationListener;
	double latService;
    double lngService;
    Timer timer;
    final static long TIME = 5000;
	
	@Override
	public void onCreate(){
		super.onCreate();			
		//boolean enabled = 
		//		locationManager
		//		  .isProviderEnabled(LocationManager.GPS_PROVIDER);
		//Toast.makeText(getApplicationContext(), "PositionService created!!! gps enabled: " + enabled, Toast.LENGTH_SHORT).show();
		final Handler h = new Handler();
	    h.post(new Runnable() {
	        @Override
	        public void run() {
	            LocationUpdates();
	            h.postDelayed(this, TIME);
	        }
	    });
	}
	
	public void LocationUpdates(){
        locListener locList = new locListener();
        locationManager = (LocationManager)getSystemService(getApplicationContext().LOCATION_SERVICE);   
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locList);
        //Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        
        Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        updateWithNewLocation(location);
        //Location location = getLastKnownLocation();
        //if(location != null) {
        //	Toast.makeText(getApplicationContext(), "LocationUpdates lat:" 
        //			+ location.getLatitude() + " lon: " + location.getLongitude()
        //			, Toast.LENGTH_SHORT).show();
        //} else {
        //	Toast.makeText(getApplicationContext(), "location is null", Toast.LENGTH_SHORT).show();
        //}
    }
	
	private void updateWithNewLocation(Location location) {
    	String latLongString = "Unknown";    	
    	DecimalFormat df = new DecimalFormat("##.00");      	
    	if (location != null) {
    		double lat = location.getLatitude();
    		double lng = location.getLongitude();
    		latLongString = "Lat:" +  df.format(lat) + "\nLong:" +  df.format(lng);
    	} else {
    		latLongString += " - location is null";
    	}
    	Toast.makeText(getApplicationContext(), "Your Current Position is:\n" + latLongString , Toast.LENGTH_SHORT).show();
    }
	/*
	private Location getLastKnownLocation() {
	    locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
	    List<String> providers = locationManager.getProviders(true);
	    Location bestLocation = null;
	    for (String provider : providers) {
	        Location l = locationManager.getLastKnownLocation(provider);
	        if (l == null) {
	            continue;
	        }
	        if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
	            // Found best last known location: %s", l);
	            bestLocation = l;
	        }
	    }
	    return bestLocation;
	}
	*/
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		super.onStartCommand(intent, flags, startId);
		Toast.makeText(getApplicationContext(), "PositionService started!!!", Toast.LENGTH_SHORT).show();
		return START_STICKY;
	}
	
	@Override 
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public class locListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
        	Toast.makeText(getApplicationContext(), "locationChanged!! lat: " + location.getLatitude() + " lon: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            latService = location.getLatitude();
            lngService = location.getLongitude();
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
    		Toast.makeText(getApplicationContext(), "statusChanged!!!", Toast.LENGTH_SHORT).show();
        }


    }
}
