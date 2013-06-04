/**
 * Written by Santiago Alfaro and Micah Rye
 */

package mit.edu.obmg.ioioservice;

import mit.edu.obmg.ioioservice.ioio.IOIOBGService;
import mit.edu.obmg.ioioservice.ioio.IOIOBGService.LocalBinder;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;

public class MainActivity extends Activity {

	private IOIOBGService mIOIOService;
	private boolean mBounded = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startService(new Intent(this,IOIOBGService.class));
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent mIntent = new Intent(this, IOIOBGService.class);
		bindService(mIntent, mConnection, BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(mBounded) {
			unbindService(mConnection);
			mBounded = false;
		}
	}
	
	@Override
	protected void onDestroy(){
		//stop the service on exit of program
		stopService((new Intent(this, IOIOBGService.class)));
		super.onDestroy(); 
	}

	ServiceConnection mConnection = new ServiceConnection() { 
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBounded = true;
			LocalBinder mLocalBinder = (LocalBinder)service;
			mIOIOService = mLocalBinder.getServerInstance();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBounded = false;
			mIOIOService = null;
		}
	};

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
