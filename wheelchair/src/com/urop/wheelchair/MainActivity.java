package com.urop.wheelchair;
// this is the screen that first shows up to the user containing an offline or an online option to stream and record data
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

public class MainActivity extends ActionBarActivity {
	private static final String APP_KEY = "tlfzgltz6sef00l";
    private static final String APP_SECRET = "2d47gsoisbttl84";
    private static final String TAG = "wheelchairActivity";
	
    static final int REQUEST_LINK_TO_DBX = 0;  // This value is up to you

	
	protected Button mLoginButton;
	protected Button mDisplayButton;
	protected Button mLink;

	private DbxAccountManager mDbxAcctMgr;   //create a DbxAccountManager object. This object lets you link to a Dropbox user's account
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);
		setContentView(R.layout.activity_main);

		mLoginButton = (Button) findViewById(R.id.button1); //links the login button to a variable
		mDisplayButton = (Button) findViewById(R.id.button2); // *CHANGE NAME* links the start button to a variable
		mLink = (Button) findViewById(R.id.button3);
		
		/*mLoginButton.setOnClickListener(new View.OnClickListener() { // listens for a button press

			@Override
			public void onClick(View v) {
				

				

				onClickLinkToDropbox(); // links to user dropbox acc and creates a folder 


				if(mDbxAcctMgr.hasLinkedAccount()){
					Intent intent = new Intent(MainActivity.this, LoginActivity.class); //the screen switches from the main activity to the login screen
					startActivity(intent);	
				} 

				
				if(mDbxAcctMgr.hasLinkedAccount()){
					Intent intent = new Intent(MainActivity.this, LoginActivity.class); //the screen switches from the main activity to the login screen
					startActivity(intent);	
				}

				
				
				
			}
		});*/
		
		mLink.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickLinkToDropbox();
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
	
	public void onClickLinkToDropbox() {
	    mDbxAcctMgr.startLink((Activity)this, REQUEST_LINK_TO_DBX);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_LINK_TO_DBX) {
	        if (resultCode == Activity.RESULT_OK) {
	            // ... Start using Dropbox files.
	        	
	        	if(mDbxAcctMgr.hasLinkedAccount()){
	        		Toast.makeText(this, "linked", Toast.LENGTH_SHORT).show();
	        	}
	        	/*try {
					createfile();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.i(TAG, "something");
				}*/
	        } else {
	            // ... Link failed or was cancelled by the user.
	        }
	    } else {
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	}

	private void createfile() throws IOException {
		
		// Create DbxFileSystem for synchronized file access.
        DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());

		

		DbxPath path = new DbxPath("hello.txt");
			// Create a test file only if it doesn't already exist.
		DbxFile testFile = dbxFs.create(path);

		Toast.makeText(this, "file created", Toast.LENGTH_SHORT).show();
		try {
		    testFile.writeString("Hello Dropbox!");
			Toast.makeText(this, "text added", Toast.LENGTH_SHORT).show();



			    String contents = testFile.readString();
			    Log.d("Dropbox Test", "File contents: " + contents);
				Toast.makeText(this, "text read", Toast.LENGTH_SHORT).show();




		Toast.makeText(this, "file created", Toast.LENGTH_LONG).show();
		try {
		    testFile.writeString("Hello Dropbox!");
			Toast.makeText(this, "text added", Toast.LENGTH_LONG).show();
			
			
			
			    String contents1 = testFile.readString();
			    Log.d("Dropbox Test", "File contents: " + contents1);
				Toast.makeText(this, "text read", Toast.LENGTH_LONG).show();

			
			

		} finally {
		    testFile.close();
		 }
		}
		finally {
		    testFile.close();
		 }
		
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

