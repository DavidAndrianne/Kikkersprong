package mobile.apps.kikkersprong2.db.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import mobile.apps.kikkersprong2.db.DatabaseException;
import mobile.apps.kikkersprong2.model.Child;
import mobile.apps.kikkersprong2.model.ChildSimpleFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

//source : http://stackoverflow.com/questions/13196234/simple-parse-json-from-url-on-android
public class GetChildAsynchTask extends AsyncTask<String, String, Child> {
    public InputStream inputStream = null;
    public String result = "";
	
    /* Makes connections and receives responses from the server
     * (Do NOT try to assign response values to GUI elements, this is a common mistake,
     * that cannot be done in a background thread). */ 
    @Override 
    protected Child doInBackground(String... params) {
    	Log.v("JSON", "Contacting host: "+params[0]);
        String url_select = params[0];

        ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("id", params[1]));
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
            Log.v("JSON", "Received child: " + result.toString());
            if(result instanceof String && result.indexOf("ERROR") != -1){ 
            	throw new DatabaseException(result.toString()); 
            } else {
	            JSONObject jObject = new JSONObject(result);
	            return ChildSimpleFactory.createChildFromJsonObject(jObject);
            }
        } catch (Exception e) {
            Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
        }
		return null; //Something went wrong, connection failed!
    } // protected Void doInBackground(String... params)
	
}