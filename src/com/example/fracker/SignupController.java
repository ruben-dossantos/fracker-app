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

import com.example.fracker.model.User;
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


public class SignupController extends Activity {

	private String username, password, first_name, last_name;

	private String backendURL = "http://crucifix.inescporto.pt:8080";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup_controller);

		Button button1 = (Button) findViewById(R.id.create_button);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				username = ((TextView) findViewById(R.id.input_username))
						.getText().toString();
				password = ((TextView) findViewById(R.id.input_password))
						.getText().toString();
				first_name = ((TextView) findViewById(R.id.input_first_name))
						.getText().toString();
				last_name = ((TextView) findViewById(R.id.input_last_name))
						.getText().toString();

				User user = new User(username, password, first_name, last_name);
				Gson gson = new GsonBuilder()
						.excludeFieldsWithoutExposeAnnotation().create();
				String json = gson.toJson(user);
				new PostSignupTask().execute(
						String.format("%s%s", backendURL, "/signup"), json);
			}
		});

		Button button2 = (Button) findViewById(R.id.cancel_button);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(SignupController.this,
						LoginController.class);
				startActivity(i);
			}
		});

	}

	private class PostSignupTask extends AsyncTask<String, String, String> {

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

				Intent i = new Intent(SignupController.this,
						LoginController.class);
				startActivity(i);
			}
		}
	}

}
