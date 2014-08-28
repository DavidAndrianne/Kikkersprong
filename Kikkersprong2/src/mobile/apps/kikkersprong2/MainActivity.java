package mobile.apps.kikkersprong2;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import com.google.zxing.integration.android.*;

import mobile.apps.kikkersprong2.model.DomainException;
import mobile.apps.kikkersprong2.model.KikkersprongModel;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements Observer {
	private KikkersprongModel model = new KikkersprongModel();
	//public final static int openingTime = 8;
	public final static int closingTime = 15;
	public static final String KIKKERSRPONG_MODEL = "MAIN_MODEL";

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("General", "Launch!");
        model.addObserver(this);
        setContentView(R.layout.activity_main);
        if(new Date().getHours() > closingTime){
        	((TextView)findViewById(R.id.textView1)).setText("");
        }
        try{
	        if(hasWifiConnection()){ Log.v("databaseTest", "Wifi's enabled!"); if(!model.isUsingOnlineDb())model.tryOnlineDbConnection();}
	        Log.v("databaseTest", "connection established: "+model.isUsingOnlineDb());
        } catch (DomainException e) {
        	Toast.makeText(this, "ERROR: "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        	Log.e("DomainException", e.getMessage());
        }
        this.update(model, null);
    }
    
    @Override
	public void	onSaveInstanceState	(Bundle bundle){
		super.onSaveInstanceState(bundle);
		bundle.putSerializable(KIKKERSRPONG_MODEL, model);
	}
    
    @Override
    public void onRestoreInstanceState (Bundle bundle){
    	super.onRestoreInstanceState(bundle);
    	this.model = (KikkersprongModel) bundle.getSerializable(KIKKERSRPONG_MODEL);
    	update(model, null); //Check wifi connection etc again
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	this.update(model,  null);
    }
    
    private boolean hasWifiConnection(){
    	ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void viewHistory(View v){
    	Intent intent = new Intent(this, HistoryActivity.class);
		Bundle b = new Bundle();
		b.putSerializable(MainActivity.KIKKERSRPONG_MODEL, model);
		intent.putExtras(b);
		startActivity(intent);
    }
    
    public void scanCode(View v){
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
    }
    
    @SuppressWarnings("deprecation")
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	  if (scanResult != null) {// handle scan result
    		  String[] qrCodeArgs = scanResult.getContents().toString().split("\n", 3);
    		  String id = qrCodeArgs[0];		  Log.v("id", id);
    		  String firstName = qrCodeArgs[1];	  Log.v("firstName", firstName);
    		  String name = qrCodeArgs[2];		  Log.v("name", name);
    		  if(model.getAuthenticationMode()){ //If login mode
    			  try {
    				  model.login(id); //Login
    				  model.setAuthenticationMode(false);
    				  if(model.getUser().getName().equals("Admin")){
    					  findViewById(R.id.historyBtn).setVisibility(View.INVISIBLE);
    					  this.viewFinancial();
    				  } else {
    					  findViewById(R.id.historyBtn).setVisibility(View.VISIBLE);
    				  }
    				  ((TextView)findViewById(R.id.authenticatedUser)).setText(model.getUser().getFirstname() + " [Logout]");
    				  Toast.makeText(this, "Authentication successful: "+model.getUser().getFirstname() +" "+model.getUser().getName(), Toast.LENGTH_LONG).show();
    			  } catch (DomainException e) {
    				  Toast.makeText(this, "ERROR:"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    				  Log.e("DomainException", e.getMessage());
    			  }
    		  } else { //Else check-in/checkout
	    		  try {
	    			  if(new Date().getHours() > closingTime){ //If afternoon then checkout
	    				  String nickname = model.registerLeaveChild(id, firstName + " " + name);
	    				  if(new Date().getDay() > 4){ //If weekend
	    					  Toast.makeText(this, "Fijn weekend, "+nickname+"!", Toast.LENGTH_SHORT).show();
	    				  } else { //If normal day
	    					  Toast.makeText(this, "Fijne avond, "+nickname+"!", Toast.LENGTH_SHORT).show();
	    				  }
	    			  } else { //else if morning/noon then checkin
	    				  String nickname = model.registerArrivalChild(id, firstName + " " + name);
	    				  Date d = model.getChild(id).getBirthday();
	    				  if(d != null && d.getDay() == new Date().getDay() && d.getMonth() == new Date().getMonth()){
	    					  Toast.makeText(this, "Gelukkige verjaardag, "+nickname+"!", Toast.LENGTH_LONG).show();    				  
	    				  } else { 
	    					  Toast.makeText(this, "Welkom, "+nickname+"!", Toast.LENGTH_SHORT).show();
	    				  }
	    			  }
	    		  } catch (DomainException e) {
	    			  Toast.makeText(this, "ERROR:"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
	    			  Log.e("DomainException", e.getMessage());
	    		  } //End try-catch
    		  } //End else (check-in/checkout)
    	  }
    	  // else continue with any other code you need in the method...
    	}
    
    private void viewFinancial(){
    	Intent intent = new Intent(this, FinancialActivity.class);
		Bundle b = new Bundle();
		intent.putExtras(b);
		startActivity(intent);
    }
    
    public void updateUser(View v){
    	model.setAuthenticationMode(true);
    	IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
    }

	@Override //Called when database switches from online to offline and offline to online mode
	public void update(Observable arg0, Object arg1) {
		if(arg0.equals(model)){
			try {
				ImageView v = (ImageView)findViewById(R.id.db_conn);
				if(!this.hasWifiConnection()){
					Toast.makeText(this, "The Wifi's out! Please address this issue ASAP!", Toast.LENGTH_LONG).show();
					v.setImageResource(R.drawable.db_conn_dead); 
				} else { //Wifi Ok
					model.tryOnlineDbConnection();
					if(model.isOutOfSynch()){
						Toast.makeText(this, "WARNING! System is out of synch! Please tap the database icon...", Toast.LENGTH_LONG).show();
						v.setImageResource(R.drawable.db_conn_out_of_synch);
					} else if (model.isUsingOnlineDb()) {
						Toast.makeText(this, "The connection's ok, data is synchronized!", Toast.LENGTH_LONG).show();
						v.setImageResource(R.drawable.db_conn_ok);
					} else { //Not out of synch and not online
						Toast.makeText(this, "ERROR: Wifi's good but we could not get a response from the server...", Toast.LENGTH_LONG).show();
						v.setImageResource(R.drawable.db_conn_dead);
					}
				}
			} catch (DomainException e) {
				Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void checkConnection(View v){
		this.update(model, null);
	}
	
	public void logout(View v){
		findViewById(R.id.historyBtn).setVisibility(View.INVISIBLE);
		((TextView)findViewById(R.id.authenticatedUser)).setText("");
		Toast.makeText(this, "Bye, "+model.getUser().getFirstname(), Toast.LENGTH_SHORT).show();
		model.logout();
	}
}