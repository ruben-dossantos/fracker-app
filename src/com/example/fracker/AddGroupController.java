package com.example.fracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddGroupController extends Activity {
	private String groupName, password, reconfirmPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_group_controller);

		Button createGroup = (Button) findViewById(R.id.submit_group);
		createGroup.setOnClickListener(new OnClickListener() {
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
		if (!reconfirmPassword.isEmpty()  && !password.isEmpty()) {
			if (password.equals(reconfirmPassword)) {
				pstatus = true;
			}
		}
		return pstatus;
	}
}
