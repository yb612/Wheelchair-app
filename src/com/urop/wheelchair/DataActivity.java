package com.urop.wheelchair;
// the user enters their registered username and password to login to their account
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

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
		
		String[] some = new String[2];
		List<DbxFileInfo> entries =  new ArrayList<DbxFileInfo>();
		String[] test =  new String[entries.size()];
		entries.toArray(test);
		String[] fnames = new String[entries.size()];
		 DbxPath path;
		 path = new DbxPath("/"); 
		 
		 
	     ArrayList<String> result=  new ArrayList<String>();
	     DbxFileSystem fs = getDbxFS();
     	

        if(isAuthenticated()==false || fs==null){
        	Toast.makeText(this, "not switched", Toast.LENGTH_SHORT).show();

        }
        try {
        	DbxFile dbFile;
        	DbxPath dbPath = path;
        	if (mDbxFs.exists(dbPath)){
        		dbFile = mDbxFs.open(dbPath);
        	}else{
        		dbFile = mDbxFs.create(dbPath);
        	}
        	
        	String [] lines = dbFile.readString().split("\r|\n|\r\n");
            dbFile.close();
            for (String line: lines) {
                if (!line.trim().equals("")) {
                    result.add(line);
                }
                
            }
            
            int index = 0;
            for (DbxFileInfo value: entries) {
            	fnames[index] = (String) value.toString();
            	index++;
            }
        } catch (IOException e) {
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
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,test);
		l.setAdapter(adapter);
		
		Toast.makeText(this, "done loading", Toast.LENGTH_SHORT).show();
		
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
