package mobile.apps.kikkersprong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    public void viewHistory(View v){
    	Intent intent = new Intent(this, HistoryActivity.class);
		Bundle b = new Bundle();
		//b.putSerializable(SelectDBActivity.DATABASE_KEY, db);
		intent.putExtras(b);
		startActivity(intent);
    }
    
    public void welcome(View v){
    	Toast.makeText(v.getContext(), "Welkom, Lotje!", Toast.LENGTH_SHORT).show();
    	findViewById(R.id.button1).setVisibility(View.VISIBLE);
    }
    
    public void viewFinancial(View v){
    	Intent intent = new Intent(this, FinancialActivity.class);
		Bundle b = new Bundle();
		intent.putExtras(b);
		startActivity(intent);
    }
}