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
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.TextView;

public class GroupsController extends Activity implements
		SearchView.OnQueryTextListener {

	private static final int RESULT_SETTINGS = 1;
	private ListView listView;
	private SearchView mSearchView;
	private User userLogin;
	private String backendURL = "http://crucifix.inescporto.pt:8080";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.groups_controller);
		listView = (ListView) findViewById(R.id.listGroups);
		mSearchView = (SearchView) findViewById(R.id.search_view);
		listView.setTextFilterEnabled(true);
		setupSearchView();

		// params pass from login
		/*Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// Deserialization
			userLogin = new Gson().fromJson(extras.getString("UserLogin"),
					User.class);
		}*/
		userLogin = UserLogin.getInstance().userLogin;

		showUserSettings();

		new GetGroupTask().execute(
				String.format("%s%s", backendURL, "/group"));
		
		/*List<String> your_array_list = new ArrayList<String>();
		your_array_list.add("INESC CENAS");
		your_array_list.add("CRITICAL CENAS");
		your_array_list.add("ISEP CENAS");
		your_array_list.add("FEUP CENAS");
		your_array_list.add("PORTO CENAS");
		your_array_list.add("ERASMUS");

		// This is the array adapter, it takes the context of the activity as a
		// first parameter, the type of list view as a second parameter and your
		// array as a third parameter.
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				R.layout.black_textview, your_array_list);

		listView.setAdapter(arrayAdapter);*/

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				Group selectedFromList = (Group) (listView
						.getItemAtPosition(myItemInt));
				// String message = "From MainActivity";
				// int id= 50;
				Intent i = new Intent(GroupsController.this, MapActivity.class);
				// i.putExtra("EXTRA_MESSAGE", message);
				// i.putExtra("EXTRA_ID", id);
				startActivity(i);
			}
		});

		mSearchView.setOnSearchClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView tv = (TextView) findViewById(R.id.title);
				tv.setVisibility(View.GONE);

			}
		});

		mSearchView.setOnCloseListener(new OnCloseListener() {
			@Override
			public boolean onClose() {
				TextView tv = (TextView) findViewById(R.id.title);
				tv.setVisibility(View.VISIBLE);
				return false;
			}
		});

		ImageButton button2 = (ImageButton) findViewById(R.id.add_group);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(GroupsController.this,
						AddGroupController.class);
				startActivity(i);
			}
		});

		/*
		 * ImageButton button3 = (ImageButton) findViewById(R.id.search);
		 * button3.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * Intent i = new Intent(GroupsController.this,
		 * SearchGroupController.class); startActivity(i); } });
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_settings:
			Intent i = new Intent(this, UserSettingActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
			break;

		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SETTINGS:
			showUserSettings();
			break;

		}

	}

	private void showUserSettings() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		StringBuilder builder = new StringBuilder();

		builder.append("\n Username: "
				+ sharedPrefs.getString("prefUsername", userLogin.getUsername()));

		builder.append("\n Send report:"
				+ sharedPrefs.getBoolean("prefSendReport", false));

		builder.append("\n Sync Frequency: "
				+ sharedPrefs.getString("prefSyncFrequency", "NULL"));

		TextView settingsTextView = (TextView) findViewById(R.id.textUserSettings);

		settingsTextView.setText(builder.toString());
	}

	private void setupSearchView() {
		mSearchView.setIconifiedByDefault(true);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setSubmitButtonEnabled(true);
		mSearchView.setQueryHint("Search group");
	}

	public boolean onQueryTextChange(String newText) {
		if (TextUtils.isEmpty(newText)) {
			listView.clearTextFilter();
		} else {
			listView.setFilterText(newText.toString());
		}
		return true;
	}

	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	private class GetGroupTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				 HttpGet g = new HttpGet(uri[0]);
				/*HttpPost p = new HttpPost(uri[0]);
				p.setEntity(new StringEntity(uri[1], "UTF8"));
				p.setHeader("Content-type", "application/json");
*/
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

				List<Group> your_array_list = new ArrayList<Group>();
				your_array_list = Arrays.asList(new Gson().fromJson(result, Group[].class));
				
				//instantiate custom adapter
			    ListviewbuttonAdapter adapter = new ListviewbuttonAdapter(your_array_list, GroupsController.this, R.drawable.ic_minus);

				// This is the array adapter, it takes the context of the activity as a
				// first parameter, the type of list view as a second parameter and your
				// array as a third parameter.
				//ArrayAdapter<Group> arrayAdapter = new ArrayAdapter<Group>(GroupsController.this,R.layout.black_textview, your_array_list);

				listView.setAdapter(adapter);
			}
		}
	}
}
