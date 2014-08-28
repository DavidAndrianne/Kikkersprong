package mobile.apps.kikkersprong.db.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mobile.apps.kikkersprong.MainActivity;
import mobile.apps.kikkersprong.model.Child;
import mobile.apps.kikkersprong.model.ChildSimpleFactory;
import mobile.apps.kikkersprong.model.DomainException;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

//source : http://stackoverflow.com/questions/13196234/simple-parse-json-from-url-on-android
public class GetAllChildrenAsynchTask extends AsyncTask<String, String, List<Child>> {
	private ProgressDialog progressDialog;
    public InputStream inputStream = null;
    public String result = "";
    private MainActivity activity;
	private List<Child> children = new ArrayList<Child>();
    
    public GetAllChildrenAsynchTask (MainActivity a){
    	activity = a;
    	progressDialog = new ProgressDialog(activity);
    }

	//most commonly used for setting up and starting a progress dialog
    protected void onPreExecute() { 
        progressDialog.setMessage("Downloading your data...");
        progressDialog.show();
        progressDialog.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
            	GetAllChildrenAsynchTask.this.cancel(true);
            }
        });
    }
	
    /* Makes connections and receives responses from the server
     * (Do NOT try to assign response values to GUI elements, this is a common mistake,
     * that cannot be done in a background thread). */ 
    @Override 
    protected List<Child> doInBackground(String... params) {

    	Log.v("JSON", "Contacting host: "+params[0]);
        String url_select = params[0];

        ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        try {
            // Set up HTTP post

            // HttpClient is more then less deprecated. Need to change to URLConnection
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(url_select);
            httpPost.setEntity(new UrlEncodedFormEntity(param));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

            // Read content & Log
            inputStream = httpEntity.getContent();
        } catch (UnsupportedEncodingException e1) {
            Log.e("UnsupportedEncodingException", e1.toString());
            e1.printStackTrace();
        } catch (ClientProtocolException e2) {
            Log.e("ClientProtocolException", e2.toString());
            e2.printStackTrace();
        } catch (IllegalStateException e3) {
            Log.e("IllegalStateException", e3.toString());
            e3.printStackTrace();
        } catch (IOException e4) {
            Log.e("IOException", e4.toString());
            e4.printStackTrace();
        }
        // Convert response to string using String Builder
        try {
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sBuilder = new StringBuilder();

            String line = null;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }

            inputStream.close();
            result = sBuilder.toString();
            extractChildrenFromResult();
        } catch (Exception e) {
            Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
        }
		return children;
    } // protected Void doInBackground(String... params)
    
    /* Here we are out of the background thread, 
     * so we can do user interface manipulation with the response data, 
     * or simply assign the response to specific variable types. */
    protected void onPostExecute(List<Child> v) {
        extractChildrenFromResult();
    } // protected void onPostExecute(List<Child> v)

	private void extractChildrenFromResult() {
		//parse JSON data
        try {
            JSONArray jArray = new JSONArray(result);    
            for(int i=0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                children.add(ChildSimpleFactory.createChildFromJsonObject(jObject));
            } // End Loop
            this.progressDialog.dismiss();
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
		} catch (DomainException e) {
			Log.e("DomainException", "DomainError: " + e.toString());
		}// End try-catch
 
	}
	
}