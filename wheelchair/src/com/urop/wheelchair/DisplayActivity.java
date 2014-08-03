package com.urop.wheelchair;
//over here the layout would be sectioned into different parts to convey data to the user
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;

public class DisplayActivity extends ActionBarActivity {
	private static final String APP_KEY = "tlfzgltz6sef00l";
    private static final String APP_SECRET = "2d47gsoisbttl84";
    private static final String TAG = "wheelchairActivity";
    
    static final int REQUEST_LINK_TO_DBX = 0;  // This value is up to you

	
	
	protected Button mCheck;
	protected Button mUnlink;

	private DbxAccountManager mDbxAcctMgr;   //create a DbxAccountManager object. This object lets you link to a Dropbox user's account

	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);

		
		
		mCheck = (Button) findViewById(R.id.button1);
		mUnlink = (Button) findViewById(R.id.button2);

		
		mCheck.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
        		printf();

			}

			
		});
		
		
		mUnlink.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
        		onClickUnlinkToDropBox();

			}

			
		});
		
	}
	
	private void printf() {//using this to check if user link carries on
		Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
		if(mDbxAcctMgr.hasLinkedAccount()){
    		Toast.makeText(this, "linked", Toast.LENGTH_SHORT).show();
    	}
		else{
    		Toast.makeText(this, "not linked", Toast.LENGTH_SHORT).show();

		}
	}
	
	private void onClickUnlinkToDropBox() {
		// TODO Auto-generated method stub
		mDbxAcctMgr.unlink();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_new) {
    		Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
