package com.urop.wheelchair;
// the user enters their registered username and password to login to their account
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends ActionBarActivity {

	protected Button mLoginButton;
	protected Button mRetrievePass;
	protected Button mSignUpButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mSignUpButton = (Button) findViewById(R.id.button1); //links the signUp button to a variable
		mRetrievePass = (Button) findViewById(R.id.button2); //links the retrieve pass button to a variable
		mLoginButton = (Button) findViewById(R.id.button3); //links the login button to a variable

		mSignUpButton.setOnClickListener(new View.OnClickListener() {  

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, SignUpActivity.class); //when the signup button is pressed the screen is switched to the signup page
				startActivity(intent);
			}
		});
		
		mLoginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, DisplayActivity.class);//when the login button is pressed the usename and password should be checked for an existing account if true the display activity is launched
				startActivity(intent);
			}
		});
		
		mRetrievePass.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);//when the retrieve button is pressed the screen switches to the forgotPass activity
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
