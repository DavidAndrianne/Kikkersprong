package mobile.apps.kikkersprong2;

import mobile.apps.kikkersprong2.model.KikkersprongModel;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class HistoryActivity extends Activity {
	private KikkersprongModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_history);
        model = (KikkersprongModel) this.getIntent().getExtras().get(MainActivity.KIKKERSRPONG_MODEL);
        ((TextView)findViewById(R.id.textView2)).setText(model.getUser().getFirstname()+"'s historiek");
        fillBillsList();
        fillWeekSchedule();
    }
    
    private void fillBillsList() {
    	// TODO Auto-generated method stub
    	
    }

    private void fillWeekSchedule() {
		// TODO Auto-generated method stub
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
}