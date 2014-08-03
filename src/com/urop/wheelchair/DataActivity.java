package com.urop.wheelchair;
// the user enters their registered username and password to login to their account
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class DataActivity extends ActionBarActivity {

	protected Button mLoginButton;
	protected Button mRetrievePass;
	protected Button mSignUpButton;
	ListView l;
	String[] days = {"sunday","monday"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data);
		
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
