package mobile.apps.kikkersprong;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import com.google.zxing.integration.android.*;

import mobile.apps.kikkersprong.model.DomainException;
import mobile.apps.kikkersprong.model.KikkersprongModel;
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
import android.widget.Toast;

public class MainActivity extends Activity implements Observer {
	private KikkersprongModel model = new KikkersprongModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model.addObserver(this);
        setContentView(R.layout.activity_main);
        
        if(hasWifiConnection()){ Log.v("databaseTest", "Wifi's enabled!"); if(!model.isUsingOnlineDb())model.tryOnlineDbConnection();}
        Log.v("databaseTest", "connection established: "+model.isUsingOnlineDb()+"");
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
		//b.putSerializable(MainActivity.MODEL_KEY, model);
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
    				  Toast.makeText(this, "Authentication successful: "+model.getUser().getFirstname() +" "+model.getUser().getName(), Toast.LENGTH_LONG).show();
    			  } catch (DomainException e) {
    				  Toast.makeText(this, "ERROR:"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    				  Log.e("DomainException", e.getMessage());
    			  }
    		  } else { //Else check-in/checkout
	    		  try {
	    			  if(new Date().getHours() > 15){ //If afternoon
	    				  String nickname = model.registerLeaveChild(id, firstName + " " + name);
	    				  if(new Date().getDay() > 4){ //If weekend
	    					  Toast.makeText(this, "Fijn weekend, "+nickname+"!", Toast.LENGTH_SHORT).show();
	    				  } else { //If normal day
	    					  Toast.makeText(this, "Fijne avond, "+nickname+"!", Toast.LENGTH_SHORT).show();
	    				  }
	    			  } else { //else if morning/noon
	    				  String nickname = model.registerArrivalChild(id, firstName + " " + name);
	    				  Toast.makeText(this, "Welkom, "+nickname+"!", Toast.LENGTH_SHORT).show();    				  
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
    	IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
		model.setAuthenticationMode(true);
    }

	@Override //Called when database switches from online to offline and offline to online mode
	public void update(Observable arg0, Object arg1) {
		ImageView v = (ImageView)findViewById(R.id.db_conn);
		if(model.isUsingOnlineDb()){ //DB Ok
			v.setImageResource(R.drawable.db_conn_ok);
		} else if (model.isOutOfSynch()){
			v.setImageResource(R.drawable.db_conn_out_of_synch);
		} else {
			v.setImageResource(R.drawable.db_conn_dead);
		}
	}
}