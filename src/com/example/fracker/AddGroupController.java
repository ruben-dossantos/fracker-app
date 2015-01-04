package com.example.fracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.fracker.model.Group;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddGroupController extends Activity {
	private String groupName, password, reconfirmPassword;

	private String backendURL = "http://crucifix.inescporto.pt:8080";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_group_controller);

		Button createGroup = (Button) findViewById(R.id.submit_group);
		createGroup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				groupName = ((TextView) findViewById(R.id.group_name))
						.getText().toString();
				password = ((TextView) findViewById(R.id.group_password))
						.getText().toString();
				reconfirmPassword = ((TextView) findViewById(R.id.retype_group_password))
						.getText().toString();

				if (checkPassWordAndConfirmPassword(password, reconfirmPassword)
						&& !groupName.isEmpty()) {
					// valid
					Toast.makeText(getApplicationContext(),
							"Valid password and groupName", Toast.LENGTH_LONG)
							.show();

					Gson gson = new GsonBuilder()
							.excludeFieldsWithoutExposeAnnotation().create();
					Group g1 = new Group(groupName, password);
					// List<User> users = new ArrayList<User>();
					// users.add(new User("asd","asd","asds"));
					// g1.set_users(users);
					// Serialization
					String json = gson.toJson(g1);

					// Deserialization
					// Group g = new Gson().fromJson(json, Group.class);

					// simular o enviar e o receber porque nao existe
					// BACKOFFICE!!
					/*
					 * Toast.makeText(getApplicationContext(), json,
					 * Toast.LENGTH_LONG) .show();
					 * Toast.makeText(getApplicationContext(), g.toString(),
					 * Toast.LENGTH_LONG) .show();
					 */

					new PostGroupTask().execute(
							String.format("%s%s", backendURL, "/group"), json);
				}
				if (!checkPassWordAndConfirmPassword(password,
						reconfirmPassword) && groupName.isEmpty()) {
					// both password and groupName invalid
					Toast.makeText(getApplicationContext(),
							"Invalid password and groupName", Toast.LENGTH_LONG)
							.show();
				} else {
					if (!checkPassWordAndConfirmPassword(password,
							reconfirmPassword)) {
						// invalid password
						Toast.makeText(getApplicationContext(),
								"Invalid password", Toast.LENGTH_LONG).show();
					} else if (groupName.isEmpty()) {
						Toast.makeText(getApplicationContext(),
								"Invalid groupName", Toast.LENGTH_LONG).show();
					}
				}
			}
		});

	}

	private boolean checkPassWordAndConfirmPassword(String password,
			String reconfirmPassword) {
		boolean pstatus = false;
		if (!reconfirmPassword.isEmpty() && !password.isEmpty()) {
			if (password.equals(reconfirmPassword)) {
				pstatus = true;
			}
		}
		return pstatus;
	}

	private class PostGroupTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				// HttpGet g = new HttpGet(uri[0]);
				HttpPost p = new HttpPost(uri[0]);
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
				Toast.makeText(getApplicationContext(), "Your group was be created ",
						Toast.LENGTH_LONG).show();
				Intent i = new Intent(AddGroupController.this,
						GroupsController.class);
				//i.putExtra("UserLogin", result);

				startActivity(i);
			}
			// Do anything with response..
		}
	}
}
