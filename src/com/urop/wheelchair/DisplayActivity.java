package com.urop.wheelchair;
//over here the layout would be sectioned into different parts to convey data to the user
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

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
    		
    		//test dialog
    		final EditText filenameInput = new EditText(this);
            filenameInput.setHint(R.string.new_note_name_hint);
            filenameInput.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

            new AlertDialog.Builder(this)
            .setTitle(R.string.new_note_dialog_title)
            .setView(filenameInput)
            .setPositiveButton(R.string.new_note_confirm, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// onclick creates a new file  {not finished}
					String filename = filenameInput.getText().toString();
                    if (TextUtils.isEmpty(filename)) {
                        filename = filenameInput.getHint().toString();
                    }
                    if (!filename.endsWith(".txt")) {
                        filename += ".txt";
                    }

                    DbxPath p;
                    try {
                        if (filename.contains("/")) {
                            Toast.makeText(DisplayActivity.this, "invalid filename", Toast.LENGTH_LONG).show();
                    	
                            return;
                        }
                        p = new DbxPath("/" + filename);
                    } catch (DbxPath.InvalidPathException e) {
                        // TODO: build a custom dialog that won't even allow invalid filenames
                        Toast.makeText(DisplayActivity.this, "invalid filename", Toast.LENGTH_LONG).show();
                        return;
                    }
                  
                    DbxFileSystem dbxFs;
                    DbxFile testFile = null;
					try {
						dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
						testFile = dbxFs.create(p);
                        Toast.makeText(DisplayActivity.this, "file created", Toast.LENGTH_LONG).show();

					} catch (Unauthorized e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DbxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						testFile.close();
					}
                     
                    
                    
				}
			})
            .create()
            .show();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}