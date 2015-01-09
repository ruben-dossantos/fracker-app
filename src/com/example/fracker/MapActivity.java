package com.example.fracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.fracker.model.Group;
import com.example.fracker.model.User;
import com.example.fracker.model.UserLogin;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MapActivity extends Activity {

	// Google Map
	private GoogleMap googleMap;
	private User userLogin;
	private String backendURL = "http://crucifix.inescporto.pt:8080";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		// get group id
		Intent intent = getIntent();
		String groupId = intent.getStringExtra("GROUP_ID");

		userLogin = UserLogin.getInstance().userLogin;

		try {
			// Loading map
			initilizeMap();

			new GetFriendsLocation()
					.execute(String.format("%s%s%s%s%s", backendURL, "/user/",
							userLogin.getId(), "/group/", groupId));

			googleMap
					.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

						@Override
						public void onInfoWindowClick(Marker marker) {
							LatLng position = marker.getPosition();

							if (Double.parseDouble(userLogin.getLat()) == position.latitude
									&& Double.parseDouble(userLogin.getLon()) == position.longitude) {
								Toast.makeText(getApplicationContext(),
										"You do not need to drive to yourself :P", Toast.LENGTH_LONG)
										.show();

							} else {

								Intent intent = new Intent(
										android.content.Intent.ACTION_VIEW,
										Uri.parse("http://maps.google.com/maps?saddr="
												+ userLogin.getLat()
												+ ","
												+ userLogin.getLon()
												+ "&daddr="
												+ position.latitude
												+ "," + position.longitude));
								startActivity(intent);
							}

						}
					});

			googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
				@Override
				public View getInfoWindow(Marker arg0) {
					return null;
				}

				@Override
				public View getInfoContents(Marker marker) {
					View myContentView = getLayoutInflater().inflate(
							R.layout.custommarker, null);
					TextView tvTitle = ((TextView) myContentView
							.findViewById(R.id.title));
					tvTitle.setText(marker.getTitle());
					return myContentView;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	private void addMarker(double latitude, double longitude, String userName) {
		// create marker
		MarkerOptions marker = new MarkerOptions().position(
				new LatLng(latitude, longitude)).title(userName);

		// adding marker
		googleMap.addMarker(marker);
	}

	private class GetFriendsLocation extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				HttpGet g = new HttpGet(uri[0]);
				response = httpclient.execute(g);
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
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

				/*
				 * String users = "users"; int index = result.indexOf(users); if
				 * (index != -1) { result = "[".concat(result.substring(index+8
				 * , result.length()-1)); }
				 * 
				 * List<User> your_array_list = new ArrayList<User>();
				 * your_array_list = Arrays.asList(new Gson().fromJson(result,
				 * User[].class));
				 */
				Group g = new Gson().fromJson(result, Group.class);
				List<User> your_array_list = g.getUsers();
				for (User user : your_array_list) {
					addMarker(Double.parseDouble(user.getLat()),
							Double.parseDouble(user.getLon()),
							user.getFirst_name() + " " + user.getLast_name());
				}
			}
		}
	}

}