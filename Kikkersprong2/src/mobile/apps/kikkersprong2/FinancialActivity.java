package mobile.apps.kikkersprong2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class FinancialActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_financial);
		TableLayout table = new TableLayout(this);
		String[] databaseData = new String[]{"Lotje's ouders - 350€ sinds Maart", "Tim's ouders - betaald"};
		for(String s : databaseData){
			TableRow row = new TableRow(this);
			TextView cell = new TextView(this);	cell.setText(s);
			row.addView(cell);
			if(((String)cell.getText()).contains("€")){
				Button generateBtn = new Button(this); generateBtn.setText("Genereer factuur");
				generateBtn.setWidth(generateBtn.getWidth() / 2);
				generateBtn.setHeight(generateBtn.getHeight() / 2);
				generateBtn.setTextSize(generateBtn.getTextSize() / 4);
				row.addView(generateBtn);
			}
			table.addView(row);
		}
		((RelativeLayout)findViewById(R.id.FinancialRelativeLay)).addView(table);
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
}
