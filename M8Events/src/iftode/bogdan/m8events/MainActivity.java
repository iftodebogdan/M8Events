package iftode.bogdan.m8events;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.SoundEffectConstants;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
*/
	
	// Called when the user clicks the Events button
	public void startEventsListActivity(View view) {
		view.playSoundEffect(SoundEffectConstants.CLICK);
		Intent intent = new Intent(this, EventsListActivity.class);
	    startActivity(intent);
	}
	
	public static Intent getOpenFacebookIntent(Context context) {

	   try {
	    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
	    return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/319964834769696"));
	   } catch (Exception e) {
	    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/319964834769696"));
	   }
	}
	
	// Called when the user clicks the Facebook button
	public void openFacebookPage(View view) {
		view.playSoundEffect(SoundEffectConstants.CLICK);
		startActivity(getOpenFacebookIntent(this));
	}
	
	// Called when the user clicks the Contact Us button
	public void contactUs(View view) {
		view.playSoundEffect(SoundEffectConstants.CLICK);
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[]{"m8office@gmail.com"});
		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Alege un client de Email:"));
	}
	
	// Called when the user clicks the About Us button
	public void aboutUs(View view) {
		view.playSoundEffect(SoundEffectConstants.CLICK);
		//Show an AlertDialog with info about M8 Events
    	new AlertDialog.Builder(this).setMessage(getString(R.string.alert_dialog_message_about_us)) 
    	.setTitle(getString(R.string.alert_dialog_title_about_us))
    	.setCancelable(true) 
    	.setNeutralButton(android.R.string.ok, 
    	new DialogInterface.OnClickListener() { 
	    	public void onClick(DialogInterface dialog, int whichButton){
	    		
	    	} 
    	}) 
    	.show();
	}
}
