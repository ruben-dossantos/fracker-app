package com.example.fracker;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.groups_controller);
		listView = (ListView) findViewById(R.id.listGroups);
		mSearchView = (SearchView) findViewById(R.id.search_view);
		listView.setTextFilterEnabled(true);
		setupSearchView();

		showUserSettings();

		List<String> your_array_list = new ArrayList<String>();
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

		listView.setAdapter(arrayAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				String selectedFromList = (String) (listView
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
				+ sharedPrefs.getString("prefUsername", "NULL"));

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

}
