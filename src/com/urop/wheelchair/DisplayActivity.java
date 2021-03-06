package com.urop.wheelchair;

//over here the layout would be sectioned into different parts to convey data to the user
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

	private ArrayAdapter<String> mArrayAdapter;// bt

	static final int REQUEST_LINK_TO_DBX = 0; // This value is up to you
	static final int REQUEST_ENABLE_BT = 1; // This value is up to you passses
											// this number if successful

	private BroadcastReceiver mReceiver;// bt
	final List<String> mAddress = new ArrayList<String>();
	String adaddress = null; 
	protected Button mCheck;
	protected Button mUnlink;
	protected Button mConnect;
	protected Button mDisconnect;
	TextView myLabel;

	TextView Result;
	
	BluetoothDevice device = null;
	
	private BluetoothSocket btSocket = null;

	private DbxAccountManager mDbxAcctMgr; // create a DbxAccountManager object.
											// This object lets you link to a
											// Dropbox user's account
	private BluetoothAdapter mBluetoothAdapter; // object to btoot with
	public static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");//   
	DbxPath logPath = new DbxPath("/test.txt"); // log to file variables
	DbxFileSystem logDbFx = null;
	DbxFile logFile = null;
	
	volatile boolean stopWorker = false;
	Thread workerThread;
	int readBufferPosition = 0;
	byte[] readBuffer;
	int counter;
	OutputStream mmOutputStream = null;
	InputStream mmInputStream = null;
	int testing = 0; 
	BluetoothDevice gDevice;

	private ListView listViewPairedDevices = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(),
				APP_KEY, APP_SECRET);

		mCheck = (Button) findViewById(R.id.button1);
		mUnlink = (Button) findViewById(R.id.button2);
		mConnect = (Button) findViewById(R.id.button3);
		mDisconnect = (Button) findViewById(R.id.button4);
		
		myLabel = (TextView)findViewById(R.id.textView1);

		// start bluetooth stuff

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Toast.makeText(this, "phone has no btoot", Toast.LENGTH_SHORT)
					.show();
		}

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}

		listViewPairedDevices = (ListView) findViewById(R.id.listViewPairedDevices);
		mArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1); // bt
		String hey = "00:18:96:B0:02:87";
		final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		
	  //  final BluetoothDevice adatest = mBluetoothAdapter//******************************
		//		.getRemoteDevice(hey);
		
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {

				// Add the name and address to an array adapter to show in a
				// ListView
				mArrayAdapter
						.add(device.getName() + "\n" + device.getAddress());
				mAddress.add(device.getAddress()); // the address array
				
			}
		} else {
			mArrayAdapter.add("No Devices");
		}

		listViewPairedDevices.setAdapter(mArrayAdapter);
		Log.d("actaul btoot", String.valueOf(pairedDevices.size()));
		Log.d("actaul btoot",pairedDevices.toString());

		mBluetoothAdapter.startDiscovery();

		// Create a BroadcastReceiver for ACTION_FOUND
		mReceiver = new BroadcastReceiver() {
			@SuppressLint("NewApi")
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// Add the name and address to an array adapter to show in a
					// ListView
					mArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
					mAddress.add(device.getAddress()); // the address array
					//device.createBond(); iffy solvent
				//	adatest.createBond();//***********************************************
					
				//	Log.d("contents of device", adatest.toString());
				}
			}
		};
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister
												// during onDestroy

		// making a clickable list
		listViewPairedDevices.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TextView temp = (TextView) arg1;
				Toast.makeText(DisplayActivity.this, temp.getText(),
						Toast.LENGTH_SHORT).show();
				String surgery = temp.getText().toString();
				//String secondline = surgery.substring(22);
				String secondline = mAddress.get(arg2);
				adaddress = mAddress.get(arg2);
				Log.d("commencing surgery", surgery);
				Log.d("first round", adaddress);
				
				 BluetoothDevice letssee = mBluetoothAdapter.getRemoteDevice(adaddress);
				 device = letssee;
				 letssee.createBond();
				

			}

		});
		// bt ends

		mCheck.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				printf();

			}

		});

		mConnect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(DisplayActivity.this, "connect pressed",Toast.LENGTH_SHORT).show();  // attempting to connect
				Connect();
			}

		});
		
		mDisconnect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(DisplayActivity.this, "disconnect pressed",Toast.LENGTH_SHORT).show();  // attempting to connect
				DisConnect();
				logFile.close();
			}

		});
		
		mUnlink.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickUnlinkToDropBox();

			}

		});

		// test

	}

	@SuppressLint("NewApi")
	protected void DisConnect() {
		// TODO Auto-generated method stub
		try {
			btSocket.close();
			Log.d("disconnect", "successfully disconnected");
			Toast.makeText(DisplayActivity.this, "disconnected :(",Toast.LENGTH_SHORT).show();  // attempting to connect

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@SuppressLint("NewApi")
	protected void Connect() {
		// TODO Auto-generated method stub
		//BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(adaddress);
		Log.d("connect", "Connecting to ... " + device);
		Log.d("connecting address", adaddress);

		
		mBluetoothAdapter.cancelDiscovery();
		Log.d("discovery canceled", "canceled discoverey");
		
		try {
		    BluetoothDevice mDevice = mBluetoothAdapter.getRemoteDevice(adaddress);
		    Method m = mDevice.getClass().getMethod("createRfcommSocket",
		                new Class[] { int.class });
		    btSocket = (BluetoothSocket) m.invoke(mDevice, Integer.valueOf(1));
		    btSocket.connect();
		    Log.d("connect", "Connection made.");
			Toast.makeText(DisplayActivity.this, "connected :)",Toast.LENGTH_SHORT).show();  // attempting to connect
		} catch (NoSuchMethodException e) {
		    Log.d("isuue", e.toString());

		} catch (SecurityException e2) {
		    Log.d("isuue", e2.toString());

		} catch (IllegalArgumentException e3) {
		    Log.d("isuue", e3.toString());

		} catch (IllegalAccessException e4) {
		    Log.d("isuue", e4.toString());

		} catch (InvocationTargetException e5) {
		    Log.d("isuue", e5.toString());

		} catch (Exception e6) {
		    Log.d("isuue", e6.toString());

		}
		   
					Log.d("socket", btSocket.toString());
					Log.d("the device", btSocket.getRemoteDevice().toString());
					
/* Here is the part the connection is made, by asking the device to create a RfcommSocket (Unsecure socket I guess), It map a port for us or something like that */
		 			
        // 	btSocket.connect();
		
		Toast.makeText(DisplayActivity.this, "connection made :)",Toast.LENGTH_SHORT).show();  // attempting to connect
		try {
			mmOutputStream = btSocket.getOutputStream();
			
			mmInputStream = btSocket.getInputStream();
			myLabel.setText("Bluetooth Opened");
			beginListenForData();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	    
        /* this is a method used to read what the Arduino says for example when you write Serial.print("Hello world.") in your Arduino code */

	}
	
	
	//listeeeen
		void beginListenForData() {
		     
		    final byte delimiter = 10; //This is the ASCII code for a newline character
		    Log.d("sigh", "begin's night was entered");
		    Log.d("sigh", handler.toString());

		    stopWorker = false;
		    readBufferPosition = 0;
		    readBuffer = new byte[16384];
		    int readMessageLimit = 512;
		    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		    workerThread = new Thread(new Runnable() {
		      public void run() {
		  	    Log.d("cool", "entered first run");
		  	    int begin = 0;
		  	    int bytes = 0; 
		         while(!stopWorker) {
		          try {
		            //int bytesAvailable = mmInputStream.available();    
		           /// int bytesAvailable = mmInputStream.read(readBuffer); //trying a blocking call instead    
					//Log.d("bytes challenge", String.valueOf(bytesAvailable));
		        	  if((readBuffer.length-bytes) <= 38){
		        		  readBuffer = null;
		        		  readBuffer = new byte[8192];
		      		    bytes = 0;
				  	    Log.d("hawa", "this is new read: "+String.valueOf(mmInputStream.read(readBuffer, bytes, readBuffer.length - bytes)));

		        	  }
				    Log.d("available", "this is available: "+String.valueOf(mmInputStream.available()));
					bytes += mmInputStream.read(readBuffer, bytes, readBuffer.length - bytes); 
					testing = bytes;
			  	    Log.d("Termes", "this is bytes: "+String.valueOf(bytes));
			  	    Log.d("condition", "this is condition: "+String.valueOf(readBuffer.length-bytes));
			  	    Log.d("Kam?", "this is buffer: "+readBuffer.toString());

		             // byte[] packetBytes = new byte[bytesAvailable];
		           //   byte[] logArray = new byte[bytesAvailable];
					//	Log.d("sing", "up to here ok");
		             // System.arraycopy(readBuffer, 0, logArray, 0, bytesAvailable); //test
		           //   Log.d("things", "Read Hex: " + getHex(logArray)+ "Read Dec: "+getDec(logArray));  //test
		              for(int i = begin; i < bytes; i++) {            //int i=0;i<bytesAvailable;i++
		               //   final byte a = packetBytes[i];
		            	  final byte b = readBuffer[i]; //test
		            	  
		            	  
		            	  if(readBuffer[i] == "#".getBytes()[0]) {
		            		  handler.obtainMessage(1, begin, i, readBuffer).sendToTarget(); 
		            		  begin = i + 1; 
		            		  if(i == bytes - 1) { 
		            		  bytes = 0; 
		            		  begin = 0; 
		            		  } 

		            	  }
		            	 // Log.d("entering for", "i should be reading"+ b);
		            	  
		            	 
		            	 // Log.d("old for", "i should be reading"+ a);
		            	  
		              /*  if(b == delimiter) {
		                  byte[] encodedBytes = new byte[readBufferPosition];
		                  System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
		                  final String data = new String(encodedBytes, "US-ASCII");
		                  readBufferPosition = 0;

		                  handler.post(new Runnable() {
		                    public void run() {
		                      myLabel.setText("some thing");
		                    }
		                  });
		                } */
		                
		              } 
		            
		          } 
		          catch (IOException ex) {
		            stopWorker = true;
		          }
		          
		         }
		      }
		    });

		    workerThread.start();
		    
		    
		  }

		Handler handler = new Handler(new Handler.Callback() 
		{       
		    @Override
		    public boolean handleMessage(Message msg) 
		    {   
		    	byte[] writeBuf = (byte[]) msg.obj; 
		    	int begin = (int)msg.arg1; 
		    	int end = (int)msg.arg2; 
		    	final String data;
		    	switch(msg.what) { 
		    	case 1: 
		    	String writeMessage = new String(writeBuf); 
		    	writeMessage = writeMessage.substring(begin, end); 
		    	Log.d("yakhalasi freska", writeMessage);

				data = writeMessage;    	
					
				    	
				    	try {
				    	
						logFile.appendString(writeMessage);
				    	Log.d("shee2 gameel ", writeMessage);
				  	    Log.d("TERMES", "this is final bytes: "+String.valueOf(testing));
				  	    runOnUiThread(new Runnable() {
				    	    public void run() {                     
				    	        // your code to update the UI thread here   
				    	    	myLabel.setText(data); //testing display
				    	    }
				    	});
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				    
		    	break; 
		    	} 
		        return false;      // RETURN VALUE ????
		    }
		});

	/*	private static String getDec(byte[] bytes){
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (byte b : bytes) {
				sb.append((b & 0xFF) + ", ");
			}
			sb.delete(sb.length()-2, sb.length());
			sb.append("]");
			return sb.toString();
		}

		private static String getHex(byte[] bytes){
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (byte b : bytes) {
				sb.append("0x" + String.format("%02X, ", b));
			}
			sb.delete(sb.length()-2, sb.length());
			sb.append("]");
			return sb.toString();
		}

*/

	private void printf() {// using this to check if user link carries on
		Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
		if (mDbxAcctMgr.hasLinkedAccount()) {
			Toast.makeText(this, "linked", Toast.LENGTH_SHORT).show();
		} else {
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

			// test dialog
			final EditText filenameInput = new EditText(this);
			filenameInput.setHint(R.string.new_note_name_hint);
			filenameInput.setInputType(EditorInfo.TYPE_CLASS_TEXT
					| EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

			new AlertDialog.Builder(this)
					.setTitle(R.string.new_note_dialog_title)
					.setView(filenameInput)
					.setPositiveButton(R.string.new_note_confirm,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// onclick creates a new file {not finished}
									String filename = filenameInput.getText()
											.toString();
									if (TextUtils.isEmpty(filename)) {
										filename = filenameInput.getHint()
												.toString();
									}
									if (!filename.endsWith(".txt")) {
										filename += ".txt";
									}

									DbxPath p;
									try {
										if (filename.contains("/")) {
											Toast.makeText(
													DisplayActivity.this,
													"invalid filename",
													Toast.LENGTH_LONG).show();

											return;
										}
										p = new DbxPath("/" + filename);
										logPath = p; //logging
									} catch (DbxPath.InvalidPathException e) {
										// TODO: build a custom dialog that
										// won't even allow invalid filenames
										Toast.makeText(DisplayActivity.this,
												"invalid filename",
												Toast.LENGTH_LONG).show();
										return;
									}

									DbxFileSystem dbxFs;
									DbxFile testFile = null;
									try {
										dbxFs = DbxFileSystem
												.forAccount(mDbxAcctMgr
														.getLinkedAccount());
										logDbFx = dbxFs; // logging
										testFile = dbxFs.create(p);
										logFile = testFile; //logging
										Toast.makeText(DisplayActivity.this,
												"file created",
												Toast.LENGTH_LONG).show();

									} catch (Unauthorized e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (DbxException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										testFile.close();
										logFile.close();
									}

								}
							}).create().show();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void onPause() {
	    super.onPause();

	    unregisterReceiver(mReceiver);
	}

	@Override
	protected void onResume() {
	    super.onResume();
	    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	    registerReceiver(mReceiver, filter); 
	}


}


