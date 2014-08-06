package com.urop.wheelchair;
// the user enters their registered username and password to login to their account
import java.io.IOException;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFileSystem;

public class DataActivity extends ActionBarActivity {

	
	private static final String APP_KEY = "tlfzgltz6sef00l";
    private static final String APP_SECRET = "2d47gsoisbttl84";
    private static final String TAG = "wheelchairActivity";
    
    static final int REQUEST_LINK_TO_DBX = 0;  // This value is up to you
	
	protected Button mLoginButton;
	protected Button mRetrievePass;
	protected Button mSignUpButton;
	private DbxFileSystem mDbxFs;
	
	private DbxAccountManager mDbxAcctMgr;   //create a DbxAccountManager object. This object lets you link to a Dropbox user's account


	ListView l;
	String[] days = {"sunday","monday"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data);
		mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);
		
		
		 
		    
		 String[] fnames = null;

		 DbxPath path;
		 path = new DbxPath("/");
		 
         
        if(isAuthenticated()==false || fs==null){
 		Toast.makeText(this, "switched", Toast.LENGTH_SHORT).show();
 		ArrayList<String> result = new ArrayList<String>();
 		DbxFileSystem dbxFs;
        }
        Toast.makeText(this, "notswitched", Toast.LENGTH_SHORT).show();
		 try {
			 
			 DbxFile dbFile;
			 
			 dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
		     ArrayList<DbxFileInfo> entries=  (ArrayList<DbxFileInfo>) dbxFs.listFolder(path);
		     
		     int i=0;
		     for (Entry ent: entries.contents) 
	            {
	                files.add(ent);// Add it to the list of thumbs we can choose from                       
	                //dir = new ArrayList<String>();
	                dir.add(new String(files.get(i++).path));
	            }
	            i=0;
		     //fnames=entries.toArray(new String[entries.size()]);
		} catch (Unauthorized e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 

		/*
		String[] fnames = null;
		int i=0;
        Object mApi;
		Entry dirent = mApi.metadata("/", 1000, null, true, null);
        ArrayList<Entry> files = new ArrayList<Entry>();
        ArrayList<String> dir=new ArrayList<String>();
        for (Entry ent: dirent.contents) 
        {
            files.add(ent);// Add it to the list of thumbs we can choose from                       
            //dir = new ArrayList<String>();
            dir.add(new String(files.get(i++).path));
        }
        i=0;
        fnames=dir.toArray(new String[dir.size()]);
        */

       
		
		l = (ListView)findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,days);
		l.setAdapter(adapter);
		
		
		
		//testing the list
		
		/*File dir = new File(dirPath);
		File[] filelist = dir.listFiles();
		String[] theNamesOfFiles = new String[filelist.length];
		for (int i = 0; i < theNamesOfFiles.length; i++) {
		   theNamesOfFiles[i] = filelist[i].getName();
		}*/
		

		
	}
	
	 private DbxFileSystem getDbxFS () {
	        if (mDbxFs!=null) {
	            return mDbxFs;
	        }
	        if (isAuthenticated()) {
	            try {
	                this.mDbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
	                mDbxFs.awaitFirstSync();
	                return mDbxFs;
	            } catch (IOException e) {
	                e.printStackTrace();
	               
	            }
	        }
	        return null;
	    }
	    static public String getDefaultPath() {
	        return "/todo/todo.txt";
	    }
	    public boolean isAuthenticated() {
	        if(mDbxAcctMgr!=null) {
	            return mDbxAcctMgr.hasLinkedAccount();
	        } else {
	            return false;
	        }
	    }
	    
	    
	    private DbxFileSystem getDbxFS () {
	        if (mDbxFs!=null) {
	            return mDbxFs;
	        }
	        if (isAuthenticated()) {
	            try {
	                this.mDbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
	                mDbxFs.awaitFirstSync();
	                return mDbxFs;
	            } catch (IOException e) {
	                e.printStackTrace();
	                throw new TodoException("Dropbox", e);
	            }
	        }
	        return null;
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
