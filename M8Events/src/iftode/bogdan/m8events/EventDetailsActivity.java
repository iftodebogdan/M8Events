package iftode.bogdan.m8events;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetailsActivity extends Activity {

	//The ProgressDialog and the references to the background tasks
	private ProgressDialog pd;
	private AsyncTask<?, ?, ?> DrawEventDetailsImageViewTask = null;
	private AsyncTask<?, ?, ?> GetEventDetailsTask = null;
	private String EventName = null;
	private String EventSumDesc = null;
	private Date EventDate = null;
	private String EventLocName = null;
	private String EventLocCoords = null;
	
	//This code is executed when the Activity loses focus 
	@Override
	public void onStop() {
	    super.onStop();  // Always call the superclass method first

		//Dismiss the ProgressDialog and kill the background tasks
	    if(pd != null)
	    	if(pd.isShowing())
	    		pd.dismiss();
	    if(DrawEventDetailsImageViewTask != null)
	    	DrawEventDetailsImageViewTask.cancel(true);
	    if(GetEventDetailsTask != null)
	    	GetEventDetailsTask.cancel(true);
	}
		
	//This code is executed after the Activity is first created or when it comes back to the foreground
	@Override
	public void onStart() {
	    super.onStart();  // Always call the superclass method first
	    
	    drawUI();
	}
	
	//This code is executed when the Activity is first created
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);
	}
	
	//This method draws the UI
	private void drawUI() {
		//Check if we have network connection
	    ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected()) {
	    	//If we have then show a progress dialog and run a background thread which attempts to
	    	//connect to the remote server and populate our Views with data from an XML file
        	pd = ProgressDialog.show(
        			this,
        			"",
        			getString(R.string.progress_dialog_loading_message),
        			true,
        			true);
        	pd.setOnCancelListener(new DialogInterface.OnCancelListener(){
                @Override
                public void onCancel(DialogInterface dialog){
                	if(DrawEventDetailsImageViewTask != null)
        		    	DrawEventDetailsImageViewTask.cancel(true);
                	if(GetEventDetailsTask != null)
            	    	GetEventDetailsTask.cancel(true);
                	finish();
                }
            });
        	
        	GetEventDetailsTask = new GetEventDetailsAsyncTask().execute();
			DrawEventDetailsImageViewTask = new DrawEventDetailsImageViewAsyncTask((ImageView) findViewById(R.id.eventDetailsImageView)).execute();
	    } else {
	    	//If not then show an AlertDialog and finish() the Activity
	    	new AlertDialog.Builder(this).setMessage(getString(R.string.alert_dialog_message_network_error)) 
	    	.setTitle(getString(R.string.alert_dialog_title_network_error)) 
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

	//Background task for downloading and displaying of the event image
	private class DrawEventDetailsImageViewAsyncTask extends AsyncTask<Object, Void, Bitmap> {
	    ImageView eventDetailsImageView;

	    public DrawEventDetailsImageViewAsyncTask(ImageView bmImage) {
	        this.eventDetailsImageView = bmImage;
	    }

	    protected Bitmap doInBackground(Object... params) {
	        String url = getApplicationContext().getString(R.string.url_event_details_image) + getIntent().getStringExtra("Event_ID") + ".jpg";
	        Bitmap eventDetailsImage = null;
	        try {
	            InputStream in = new java.net.URL(url).openStream();
	            eventDetailsImage = BitmapFactory.decodeStream(in);
	        } catch (MalformedURLException e) {
	        	Toast toast = Toast.makeText(
	            		getApplicationContext(),
	            		getString(R.string.error_toast_image_view_MalformedURLException),
	            		Toast.LENGTH_LONG);
	                    toast.show();
	        } catch (IOException e) {
	        	Toast toast = Toast.makeText(
	            		getApplicationContext(),
	            		getString(R.string.error_toast_image_view_IOException),
	            		Toast.LENGTH_LONG);
	                    toast.show();
	        }
	        return eventDetailsImage;
	    }

	    protected void onPostExecute(Bitmap result) {
	    	if(result != null)
	    		eventDetailsImageView.setImageBitmap(result);
	    }
	}
	
	//Background task to download the XML which we will use to populate our various views
	private class GetEventDetailsAsyncTask extends AsyncTask<Object, Object, Object> {

		//This code is executed when execute() is called on this object
		@Override
		protected Object doInBackground(Object... params) {
			//Attempt to get the XML file from the remote server
			String xml = XmlFunctions.getXML(getString(R.string.url_events_list_xml) + "?eid=" + getIntent().getStringExtra("Event_ID"));
			return (Object)xml;
		}
		
		//This code is executed after execute() has finished
		@Override
        protected void onPostExecute(Object result) {
			//Attempt to populate the Views using the XML file we got earlier
			populateEventDetailsTextViews((String)result);
			findViewById(R.id.eventDetailsButtonLayout).setVisibility(View.VISIBLE);
			findViewById(R.id.eventDetailsTextLayout).setVisibility(View.VISIBLE);
			//The ProgressDialog can now be dismissed
			if(pd.isShowing())
				pd.dismiss();
        }
	}
	
	//This method attempts to populate our Views using the XML file it gets as a parameter
	private void populateEventDetailsTextViews(String xml) {
		//In case of bad URL
        if(xml == null) {
            Toast toast = Toast.makeText(
            		getApplicationContext(),
            		getString(R.string.error_toast_xml_bad_url),
            		Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        
        //In case of empty file
        if(xml.length() == 0) {
            Toast toast = Toast.makeText(
            		getApplicationContext(),
            		getString(R.string.error_toast_xml_empty),
            		Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        
		Document doc = XmlFunctions.XMLfromString(xml);
		
		//In case of parse error
        if(doc == null) {
            Toast toast = Toast.makeText(
            		getApplicationContext(),
            		getString(R.string.error_toast_xml_parse_error),
            		Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        
        NodeList nodes = doc.getElementsByTagName("Event");
        Element e = (Element)nodes.item(0);
        
        //Try to convert string from XML to Date object
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("ro_RO")); 
        try {
        	date = dateFormat.parse(XmlFunctions.getValue(e, "Date") + " " + XmlFunctions.getValue(e, "Time"));
		} catch (ParseException e1) {
			Toast toast = Toast.makeText(
    		getApplicationContext(),
    		getString(R.string.error_toast_date_parse_exception),
    		Toast.LENGTH_LONG);
            toast.show();
            finish();
		}
        //The formats we will use to display the date and time
        SimpleDateFormat postDateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("ro_RO")); 
        SimpleDateFormat postTimeFormat = new SimpleDateFormat("HH:mm", new Locale("ro_RO")); 
        
        //Save values for the methods which will broadcasts intents to Maps and Calendar
        EventName = XmlFunctions.getValue(e, "Name");
        EventSumDesc = XmlFunctions.getValue(e, "SumDesc");
        EventDate = date;
        EventLocName = XmlFunctions.getValue(e, "LocName");
        EventLocCoords = XmlFunctions.getValue(e, "LocCoords");
        
        //Populate our various views with data collected from the XML file
        TextView NameTextView = (TextView) findViewById(R.id.eventDetailsNameTextView);
        NameTextView.setText(XmlFunctions.getValue(e, "Name"));
        TextView DescriptionTextView = (TextView) findViewById(R.id.eventDetailsDescriptionTextView);
        DescriptionTextView.setText(XmlFunctions.getValue(e, "DetDesc"));
        TextView LocationTextView = (TextView) findViewById(R.id.eventDetailsLocationTextView);
        LocationTextView.setText("Locatia: " + XmlFunctions.getValue(e, "LocName"));
        TextView DateTextView = (TextView) findViewById(R.id.eventDetailsDateTextView);
        DateTextView.setText("Data: " + postDateFormat.format(date));
        TextView TimeTextView = (TextView) findViewById(R.id.eventDetailsTimeTextView);
        TimeTextView.setText("Ora: " + postTimeFormat.format(date));
	}
	
	//Method which handles mapsIcon button clicks
	public void showLocationOnMap(View view) {
		view.playSoundEffect(SoundEffectConstants.CLICK);
		showLocationOnMap();
	}
	
	//Method which handles calendarIcon button clicks
	public void addToCalendar(View view) {
		view.playSoundEffect(SoundEffectConstants.CLICK);
		addToCalendar();
	}
	
	//Method which broadcasts an intent to Maps app
	private void showLocationOnMap() {
		Intent MapsIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + EventLocCoords));
  	  	try {
  	  		startActivity(MapsIntent);
  	  	} catch(android.content.ActivityNotFoundException e) {
  	  		Toast toast = Toast.makeText(
        		getApplicationContext(),
        		getString(R.string.error_toast_maps_activity_not_found_exception),
        		Toast.LENGTH_LONG);
            toast.show();
  	  	}
	}
	
	//Method which broadcasts an intent to Calendar app
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void addToCalendar() { 
		Intent CalendarIntentICS = null, CalendarIntentPreICS = null;
		
		CalendarIntentPreICS = new Intent(Intent.ACTION_EDIT)
			.setType("vnd.android.cursor.item/event")
			.putExtra("beginTime", EventDate.getTime())
			.putExtra("endTime", EventDate.getTime()+60*1000)
			.putExtra("title", EventName)
			.putExtra("description", EventSumDesc)
			.putExtra("eventLocation", EventLocName);
		     
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		    	 CalendarIntentICS = new Intent(Intent.ACTION_INSERT)
			         .setData(Events.CONTENT_URI)
			         .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, EventDate.getTime())
			         .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, EventDate.getTime()+60*1000)
			         .putExtra(Events.TITLE, EventName)
			         .putExtra(Events.DESCRIPTION, EventSumDesc)
			         .putExtra(Events.EVENT_LOCATION, EventLocName)
			         .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_FREE);

	try {
		startActivity(CalendarIntentICS);
	} catch (Exception e) {
		try {
			startActivity(CalendarIntentPreICS);
		} catch (android.content.ActivityNotFoundException e1) {
			Toast toast = Toast.makeText(
        		getApplicationContext(),
        		getString(R.string.error_toast_calendar_activity_not_found_exception),
        		Toast.LENGTH_LONG);
            toast.show();
		}
	}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_event_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_refresh:
	            drawUI();
	            return true;
	        case R.id.menu_maps:
	        	showLocationOnMap();
	            return true;
	        case R.id.menu_calendar:
	        	addToCalendar();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
