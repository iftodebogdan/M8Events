package iftode.bogdan.m8events;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;

public class EventsListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events_list);
		
		ConnectivityManager connMgr = (ConnectivityManager) 
	        getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected()) {
	        // fetch data
	    } else {
	    	new AlertDialog.Builder(this).setMessage("Verificati conexiunea la Internet") 
	    	.setTitle("Eroare de retea") 
	    	.setCancelable(true) 
	    	.setNeutralButton(android.R.string.ok, 
	    	new DialogInterface.OnClickListener() { 
	    	public void onClick(DialogInterface dialog, int whichButton){
	    	finish();
	    	} 
	    	}) 
	    	.show();
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_events_list, menu);
		return true;
	}

}
