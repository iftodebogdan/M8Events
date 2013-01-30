package iftode.bogdan.m8events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class EventsListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events_list);
		
		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected()) {
	    	PopulateEventsListView();
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
	
	private void PopulateEventsListView() {
		List<Map<String, String>> eventsList = new ArrayList<Map<String, String>>();
    	
		for(Integer i=1;i<=5;i++)
    	{
    		Map<String, String> event = new HashMap<String, String>(3);
    		event.put("eid", i.toString());
    	    event.put("title", "Item " + i.toString());
    	    event.put("date", "Subitem " + i.toString());
    	    eventsList.add(event);
    	}
    	
		SimpleAdapter adapter = new SimpleAdapter(this, eventsList,
                android.R.layout.simple_list_item_2,
                new String[] {"title", "date"},
                new int[] {android.R.id.text1,
                           android.R.id.text2});
    	
    	ListView listView = (ListView) findViewById(R.id.eventsList);
    	listView.setAdapter(adapter);

    	listView.setOnItemClickListener(mMessageClickedHandler);
	}
	
	// Create a message handling object as an anonymous class.
	private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
	    public void onItemClick(AdapterView parent, View v, int position, long id) {
	    	Toast toast = Toast.makeText(getApplicationContext(), (CharSequence)("Clicked " + ((HashMap<String, String>)parent.getItemAtPosition(position)).get("eid")), Toast.LENGTH_SHORT);
	    	toast.show();
	    }
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_events_list, menu);
		return true;
	}

}
