package com.example.fracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.fracker.model.Friend;
import com.example.fracker.model.Group;
import com.example.fracker.model.User;
import com.example.fracker.model.UserLogin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class PositionService extends Service {

	protected LocationManager locationManager;
	protected LocationListener locationListener;
	double latService;
	double lngService;
	Timer timer;

	private String backendURL = "http://crucifix.inescporto.pt:8080";

	final static long TIME = 60000;

	Context context = null;

	@Override
	public void onCreate() {
		super.onCreate();
		// boolean enabled =
		// locationManager
		// .isProviderEnabled(LocationManager.GPS_PROVIDER);
		// Toast.makeText(getApplicationContext(),
		// "PositionService created!!! gps enabled: " + enabled,
		// Toast.LENGTH_SHORT).show();
		final Handler h = new Handler();

		//Intent intent = new Intent(this, LoginController.class);
		//PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		context = getApplicationContext();

		h.post(new Runnable() {
			@Override
			public void run() {
				LocationUpdates();
				h.postDelayed(this, TIME);
			}
		});

//		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

	//	Notification n = new Notification.Builder(this)
	//		.setContentTitle("New mail from " + "test@gmail.com")
	//			.setContentText("Subject").setSmallIcon(R.drawable.ic_launcher)
	//			.setContentIntent(pIntent).setAutoCancel(true)
	//			.addAction(R.drawable.ic_launcher, "Call", pIntent).build();

	//	notificationManager.notify(0, n);
	}

	public void LocationUpdates() {
		locListener locList = new locListener();
		locationManager = (LocationManager) getSystemService(context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locList);
		// Location l =
		// locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		Location location = locationManager
				.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		updateWithNewLocation(location);
		// Location location = getLastKnownLocation();
		// if(location != null) {
		// Toast.makeText(getApplicationContext(), "LocationUpdates lat:"
		// + location.getLatitude() + " lon: " + location.getLongitude()
		// , Toast.LENGTH_SHORT).show();
		// } else {
		// Toast.makeText(getApplicationContext(), "location is null",
		// Toast.LENGTH_SHORT).show();
		// }
	}

	private void updateWithNewLocation(Location location) {
		String latLongString = "Unknown";
		DecimalFormat df = new DecimalFormat("##.0000000");
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			latLongString = "Lat:" + df.format(lat) + "\nLong:"
					+ df.format(lng);

			try {
				Gson gson = new GsonBuilder()
						.excludeFieldsWithoutExposeAnnotation().create();

				User u1 = new User(
						UserLogin.getInstance().userLogin.getUsername(), ""
								+ lat, "" + lng);
				String json = gson.toJson(u1);

				System.out.println("json to send: " + json);

				new PostGroupTask().execute(
						String.format(
								"%s%s",
								backendURL,
								"/user/"
										+ UserLogin.getInstance().userLogin
												.getId()), json);

			} catch (Exception e) {

			}
		} else {
			latLongString += " - location is null";
		}
		// Toast.makeText(getApplicationContext(), "Your Current Position is:\n"
		// + latLongString , Toast.LENGTH_SHORT).show();

	}

	private class PostGroupTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				// HttpGet g = new HttpGet(uri[0]);
				// HttpPost p = new HttpPost(uri[0]);
				HttpPut p = new HttpPut(uri[0]);
				p.setEntity(new StringEntity(uri[1], "UTF8"));
				p.setHeader("Content-type", "application/json");

				response = httpclient.execute(p);
				StatusLine statusLine = response.getStatusLine();
				System.out.println("status code = "
						+ statusLine.getStatusCode());
				// Toast.makeText(context, "status code = " +
				// statusLine.getStatusCode(), Toast.LENGTH_LONG).show();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
					System.out.println("responseString = " + responseString);
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {

				List<Friend> friends = new ArrayList<Friend>();
				friends = Arrays.asList(new Gson().fromJson(result,
						Friend[].class));

				// Toast.makeText(context,
				// "Your position was updated + " + friends,
				// Toast.LENGTH_LONG).show();
				Intent intent = new Intent(context, LoginController.class);
				PendingIntent pIntent = PendingIntent.getActivity(context, 0,
						intent, 0);

				NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

				for (int i = 0; i < friends.size(); i++) {

					Notification notification = new Notification.Builder(context)
							.setContentTitle( i + "/" + friends.size() + " " +
									friends.get(i).get_name() + " is within " + friends.get(i).get_distance() + " km.")
							//.setContentText("Subject")
							.setSmallIcon(R.drawable.ic_launcher)
							.setContentIntent(pIntent).setAutoCancel(true)
							.addAction(R.drawable.ic_launcher, "Groups", pIntent)
							.build();

					notificationManager.notify(i, notification);
					
					try {
					    Uri notification_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification_sound);
					    r.play();
					} catch (Exception e) {
					    e.printStackTrace();
					}

				}

			}
			// Do anything with response..
		}

	}

	/*
	 * private Location getLastKnownLocation() { locationManager =
	 * (LocationManager
	 * )getApplicationContext().getSystemService(LOCATION_SERVICE); List<String>
	 * providers = locationManager.getProviders(true); Location bestLocation =
	 * null; for (String provider : providers) { Location l =
	 * locationManager.getLastKnownLocation(provider); if (l == null) {
	 * continue; } if (bestLocation == null || l.getAccuracy() <
	 * bestLocation.getAccuracy()) { // Found best last known location: %s", l);
	 * bestLocation = l; } } return bestLocation; }
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		//Toast.makeText(context, "PositionService started!!!",
		//		Toast.LENGTH_SHORT).show();
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public class locListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// Toast.makeText(getApplicationContext(), "locationChanged!! lat: "
			// + location.getLatitude() + " lon: " + location.getLongitude(),
			// Toast.LENGTH_SHORT).show();
			latService = location.getLatitude();
			lngService = location.getLongitude();
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			//Toast.makeText(context, "statusChanged!!!", Toast.LENGTH_SHORT)
			//		.show();
		}

	}
}
