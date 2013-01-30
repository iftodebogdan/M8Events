package iftode.bogdan.m8events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class EventsListActivity extends Activity {

	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events_list);
		
		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected()) {
        	pd = ProgressDialog.show(
        			this,
        			"",
        			getString(R.string.progress_dialog_loading_message),
        			true,
        			false);
            new EventsListView().execute();
	    } else {
	    	new AlertDialog.Builder(this).setMessage(getString(R.string.alert_dialog_message)) 
	    	.setTitle(getString(R.string.alert_dialog_title)) 
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
	
	private class EventsListView extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			String xml = XmlFunctions.getXML(getString(R.string.eventslist_xml_url));
			return (Object)xml;
		}
		
		@Override
        protected void onPostExecute(Object result) {
			PopulateEventsListView((String)result);
			pd.dismiss();
       }
	}
	
	private void PopulateEventsListView(String xml) {		
		List<Map<String, String>> eventsList = getEventsList(xml);
		if(eventsList == null)
			return;
		
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
	    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	    	Toast toast = Toast.makeText(
	    			getApplicationContext(),
	    			(CharSequence)("Clicked " + ((HashMap<String,String>)parent.getItemAtPosition(position)).get("eid")),
	    			Toast.LENGTH_SHORT);
	    	toast.show();
	    }
	};
	
	private List<Map<String, String>> getEventsList(String xml) {		
		//In case of bad URL
        if(xml == null) {
            Toast toast = Toast.makeText(
            		getApplicationContext(),
            		getString(R.string.toast_xml_bad_url),
            		Toast.LENGTH_LONG);
            toast.show();
        	return null;
        }
        
      //In case of empty file
        if(xml.length() == 0) {
            Toast toast = Toast.makeText(
            		getApplicationContext(),
            		getString(R.string.toast_xml_empty),
            		Toast.LENGTH_LONG);
            toast.show();
        	return null;
        }
        
		Document doc = XmlFunctions.XMLfromString(xml);
		
		//In case of parse error
        if(doc == null) {
            Toast toast = Toast.makeText(
            		getApplicationContext(),
            		getString(R.string.toast_xml_parse_error),
            		Toast.LENGTH_LONG);
            toast.show();
        	return null;
        }
        
        NodeList nodes = doc.getElementsByTagName("Event");
        
        //In case of 0 events
        if(nodes.getLength() == 0)
		{
			Toast toast = Toast.makeText(
					getApplicationContext(),
					getString(R.string.toast_no_events),
					Toast.LENGTH_LONG);
	    	toast.show();
	    	finish();
		}

  		//fill in the list items from the XML document
		List<Map<String, String>> eventsList = new ArrayList<Map<String, String>>();
		for(int i = 0; i < nodes.getLength(); i++)
    	{
			Element e = (Element)nodes.item(i);
    		Map<String, String> event = new HashMap<String, String>(3);
    		event.put("eid", XmlFunctions.getValue(e, "EventID"));
    	    event.put("title", XmlFunctions.getValue(e, "Title"));
    	    event.put("date", XmlFunctions.getValue(e, "Date"));
    	    eventsList.add(event);
    	}
		
		return eventsList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_events_list, menu);
		return true;
	}

}
