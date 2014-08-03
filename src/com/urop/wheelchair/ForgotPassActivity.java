package com.urop.wheelchair;
// a user enters their email to retrieve a forgotten password
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ForgotPassActivity extends ActionBarActivity {
	
    static final int REQUEST_LINK_TO_DBX = 0;  // This value is up to you


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_pass);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgot_pass, menu);
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


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast.makeText(this, "linked", Toast.LENGTH_LONG).show();
	    if (requestCode == REQUEST_LINK_TO_DBX) {
	        if (resultCode == Activity.RESULT_OK) {
	            // ... Start using Dropbox files.
	    		Toast.makeText(this, "linked", Toast.LENGTH_LONG).show();

	        } else {
	            // ... Link failed or was cancelled by the user.
	    		Toast.makeText(this, "not linked", Toast.LENGTH_LONG).show();

	        }
	    } else {
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	}

}


