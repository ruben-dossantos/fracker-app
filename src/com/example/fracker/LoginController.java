package com.example.fracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class LoginController extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_controller);
		
		Button button1 = (Button)findViewById(R.id.login_button); 
		button1.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				
				//String message = "From MainActivity";
				//int id= 50;
				Intent i=new Intent(LoginController.this,GroupsController.class);
				//i.putExtra("EXTRA_MESSAGE", message);
				//i.putExtra("EXTRA_ID", id);
				startActivity(i);
			}
		});
		Button button2 = (Button)findViewById(R.id.sign_up_button); 
		button2.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				
				Intent i=new Intent(LoginController.this,SignupController.class);
				startActivity(i);
			}
		});
		
	}
}
