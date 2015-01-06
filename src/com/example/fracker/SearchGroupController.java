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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.fracker.model.Group;
import com.example.fracker.model.User;
import com.example.fracker.model.UserLogin;
import com.google.gson.Gson;

public class SearchGroupController extends Activity implements
		SearchView.OnQueryTextListener {

	private ListView listView;
	private SearchView mSearchView;
	private User userLogin;
	private String backendURL = "http://crucifix.inescporto.pt:8080";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.search_group_controller);
		listView = (ListView) findViewById(R.id.listGroups);
		mSearchView = (SearchView) findViewById(R.id.search_view);
		listView.setTextFilterEnabled(true);
		setupSearchView();

		userLogin = UserLogin.getInstance().userLogin;

		List<String> your_array_list = new ArrayList<String>();

		// This is the array adapter, it takes the context of the activity as a
		// first parameter, the type of list view as a second parameter and your
		// array as a third parameter.
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				R.layout.black_textview, your_array_list);

		listView.setAdapter(arrayAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				String selectedFromList = (String) (listView
						.getItemAtPosition(myItemInt));
				// String message = "From MainActivity";
				// int id= 50;
				Intent i = new Intent(SearchGroupController.this,
						MapActivity.class);
				// i.putExtra("EXTRA_MESSAGE", message);
				// i.putExtra("EXTRA_ID", id);
				startActivity(i);
			}
		});

	}

	private void setupSearchView() {
		mSearchView.setIconifiedByDefault(false);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setSubmitButtonEnabled(true);
		mSearchView.setQueryHint("Search group");
	}

	public boolean onQueryTextChange(String newText) {
		if (TextUtils.isEmpty(newText)) {
			listView.clearTextFilter();
			List<String> your_array_list = new ArrayList<String>();

			// This is the array adapter, it takes the context of the activity as a
			// first parameter, the type of list view as a second parameter and your
			// array as a third parameter.
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
					R.layout.black_textview, your_array_list);

			listView.setAdapter(arrayAdapter);
			
		} else {
			new GetGroupTask().execute(String.format("%s%s%s", backendURL,
					"/group?name=", mSearchView.getQuery()));
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
				/*
				 * HttpPost p = new HttpPost(uri[0]); p.setEntity(new
				 * StringEntity(uri[1], "UTF8")); p.setHeader("Content-type",
				 * "application/json");
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
				your_array_list = Arrays.asList(new Gson().fromJson(result,
						Group[].class));
				// This is the array adapter, it takes the context of the
				// activity as a
				// first parameter, the type of list view as a second parameter
				// and your
				// array as a third parameter.
				ArrayAdapter<Group> arrayAdapter = new ArrayAdapter<Group>(
						SearchGroupController.this, R.layout.black_textview,
						your_array_list);

				listView.setAdapter(arrayAdapter);
			}
		}
	}
}