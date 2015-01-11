package com.example.fracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import android.preference.PreferenceManager;
import android.widget.Toast;

public class PositionService extends Service {

	protected LocationManager locationManager;
	protected LocationListener locationListener;
	double latService;
	double lngService;
	Timer timer;

	private String backendURL = "http://crucifix.inescporto.pt:8080";

	final static long TIME = 15000;

	Context context = null;

	SharedPreferences sharedPrefs = null;

	double preferenceDistance = 10.0;

	@Override
	public void onCreate() {
		super.onCreate();
		//Toast.makeText(getApplicationContext(), "PositionService created!!!",
		//		Toast.LENGTH_SHORT).show();
		final Handler h = new Handler();

		context = getApplicationContext();

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

		final long prefSyncFrequency = Long.parseLong(sharedPrefs.getString(
				"prefSyncFrequency", "10000"));

		h.post(new Runnable() {
			@Override
			public void run() {
				LocationUpdates();
				h.postDelayed(this, prefSyncFrequency);
			}
		});

		String aux = sharedPrefs.getString("prefSyncFrequency", "NULL");
		if (aux != "NULL") {
			try {
				preferenceDistance = Double.parseDouble(aux);
			} catch (Exception e) {
				Toast.makeText(context, "error parsing double",
						Toast.LENGTH_SHORT).show();
			}
		}

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
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			try {
				Gson gson = new GsonBuilder()
						.excludeFieldsWithoutExposeAnnotation().create();

				User u1 = new User(
						UserLogin.getInstance().userLogin.getUsername(), ""
								+ lat, "" + lng);
				String json = gson.toJson(u1);

				System.out.println("json to send: " + json);

				if (canSend()) {
					new PostGroupTask()
							.execute(
									String.format(
											"%s%s",
											backendURL,
											"/user/"
													+ UserLogin.getInstance().userLogin
															.getId()), json);

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Toast.makeText(getApplicationContext(), "Your Current Position is:\n"
		// + latLongString , Toast.LENGTH_SHORT).show();

	}

	private boolean canSend() {
		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_WEEK);
		int hour = c.getTime().getHours();
		int minute = c.getTime().getMinutes();

		String start = "start_" + day;
		String end = "end_" + day;

		Long start_time = sharedPrefs.getLong(start, 0L);
		Long end_time = sharedPrefs.getLong(end, 0L);

		c.setTimeInMillis(start_time);
		int start_hour = c.getTime().getHours();
		int start_minute = c.getTime().getMinutes();

		c.setTimeInMillis(end_time);
		int end_hour = c.getTime().getHours();
		int end_minute = c.getTime().getMinutes();

		if (start_hour < hour && hour < end_hour) {
			return true;
		} else if (start_hour == hour) {
			if (start_minute < minute) {
				return true;
			} else {
				return false;
			}
		} else if (hour == end_hour) {
			if (minute < end_minute) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

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

				String aux = sharedPrefs.getString("prefSyncFrequency", "NULL");
				if (aux != "NULL") {
					try {
						preferenceDistance = Double.parseDouble(aux);
					} catch (Exception e) {
						Toast.makeText(context, "error parsing double",
								Toast.LENGTH_SHORT).show();
					}
				}

				// Toast.makeText(context,
				// "Your position was updated + " + friends,
				// Toast.LENGTH_LONG).show();
				Intent intent = new Intent(context, LoginController.class);
				PendingIntent pIntent = PendingIntent.getActivity(context, 0,
						intent, 0);

				NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

				for (int i = 0; i < friends.size(); i++) {
					if (friends.get(i).get_distance() < preferenceDistance) {
						Notification notification = new Notification.Builder(
								context)
								.setContentTitle(
										i + "/" + friends.size() + " "
												+ friends.get(i).get_name()
												+ " is within "
												+ friends.get(i).get_distance()
												+ " km.")
								// .setContentText("Subject")
								.setSmallIcon(R.drawable.ic_launcher)
								.setContentIntent(pIntent)
								.setAutoCancel(true)
								.addAction(R.drawable.ic_launcher, "Groups",
										pIntent).build();

						notificationManager.notify(i, notification);

						try {
							Uri notification_sound = RingtoneManager
									.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
							Ringtone r = RingtoneManager
									.getRingtone(getApplicationContext(),
											notification_sound);
							r.play();
						} catch (Exception e) {
							e.printStackTrace();
						}

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
		// Toast.makeText(context, "PositionService started!!!",
		// Toast.LENGTH_SHORT).show();
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
			// Toast.makeText(context, "statusChanged!!!", Toast.LENGTH_SHORT)
			// .show();
		}

	}
}
