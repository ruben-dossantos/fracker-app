package com.example.fracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.fracker.model.Group;
import com.example.fracker.model.UserLogin;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListviewbuttonAdapter extends BaseAdapter implements ListAdapter {

	private String backendURL = "http://crucifix.inescporto.pt:8080";
	private List<Group> list = new ArrayList<Group>();
	private Context context;
	private int imgId;

	public ListviewbuttonAdapter(List<Group> list, Context context, int imgId) {
		this.list = list;
		this.context = context;
		this.imgId = imgId;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Group getItem(int pos) {
		return list.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return list.get(pos).getId();
		// just return 0 if your list items do not have an Id variable.
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listview_button, parent, false);
		}

		// Handle TextView and display string from your list
		TextView listItemText = (TextView) view
				.findViewById(R.id.list_item_string);
		listItemText.setText(list.get(position).toString());

		// Handle buttons and add onClickListeners
		ImageButton actionBtn = (ImageButton) view
				.findViewById(R.id.action_btn);

		actionBtn.setImageResource(imgId);
		actionBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// do something
				Group g = getItem(position);
				if (imgId == R.drawable.ic_minus) {

					new DeleteGroupTask().execute(String.format(
							"%s/user/%s/group/%s", backendURL,
							UserLogin.getInstance().userLogin.getId(),
							g.getId()));
					list.remove(position);
					notifyDataSetChanged();

				} else if (imgId == R.drawable.ic_plus) {

					JSONObject jsonObj = new JSONObject();

					try {
						jsonObj.put("user",
								UserLogin.getInstance().userLogin.getId());
						jsonObj.put("group", g.getId());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					new PutGroupTask().execute(String.format(
							"%s/user/%s/group", backendURL,
							UserLogin.getInstance().userLogin.getId()), jsonObj
							.toString());
				}

			}
		});

		return view;
	}

	private class DeleteGroupTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {

				HttpDelete p = new HttpDelete(uri[0]);

				response = httpclient.execute(p);
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					// throw new IOException(statusLine.getReasonPhrase());
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
				Toast.makeText(context, "You leave the group",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private class PutGroupTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				// HttpGet g = new HttpGet(uri[0]);

				HttpPut p = new HttpPut(uri[0]);
				p.setEntity(new StringEntity(uri[1], "UTF8"));
				p.setHeader("Content-type", "application/json");

				response = httpclient.execute(p);
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					// throw new IOException(statusLine.getReasonPhrase());
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
				if (result.contains("Duplicate entry")) {
					Toast.makeText(context, "You are already in this group",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, "Join the group", Toast.LENGTH_LONG)
							.show();
				}
			}
		}
	}

}
