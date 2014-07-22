package com.urop.wheelchair;
// this is the screen that first shows up to the user containing an offline or an online option to stream and record data
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

	protected Button mLoginButton;
	protected Button mDisplayButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mLoginButton = (Button) findViewById(R.id.button1); //links the login button to a variable
		mDisplayButton = (Button) findViewById(R.id.button2); // *CHANGE NAME* links the start button to a variable

		mLoginButton.setOnClickListener(new View.OnClickListener() { // listens for a button press

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, LoginActivity.class); //the screen switches from the main activity to the login screen
				startActivity(intent);
			}
		});

		mDisplayButton.setOnClickListener(new View.OnClickListener() {  // listens for a button press *change it to a switch case to avoid onclick listener twice*

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, DisplayActivity.class); //the screen switches from the main activity to the display screen(where the values are then shown)
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
