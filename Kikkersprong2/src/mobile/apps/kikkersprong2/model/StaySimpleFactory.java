package mobile.apps.kikkersprong2.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class StaySimpleFactory {
	public static final String dateFormat = "yyyy-MM-dd HH:mm:ss";

	public static Stay createStayFromJsonObject(JSONObject jObject) throws DomainException {
		String id = null, childId = null, arrivalString = null, leaveString = null, billId = null;
		Date arrival = null, leave = null;
		try {
			id = jObject.getString("id");
			childId = jObject.getString("childId");
			arrivalString = jObject.getJSONObject("arrival").getString("date");
			leaveString = jObject.getJSONObject("leave").getString("date");
			billId = jObject.getString("billId");
		} catch (JSONException e) {
			//Ignore, some fields might throw exceptions while they may be null
			Log.v("JSONException", e.getLocalizedMessage());
		}
		Log.v("DEBUG", "Parsing Arrival string");
		if(arrivalString != null && !arrivalString.isEmpty()){
			try {
				arrival = new SimpleDateFormat(dateFormat, Locale.GERMANY).parse(arrivalString);
			} catch (ParseException e) {
				throw new DomainException(e.getLocalizedMessage());
			}
		}
		if(id == null || childId == null || arrival == null) throw new DomainException("id, childId or arrival attributes can't be null!");
		Log.v("DEBUG", "Parsing Leave string");
		if(leaveString != null && !leaveString.isEmpty()){
			try {
				leave = new SimpleDateFormat(dateFormat, Locale.GERMANY).parse(leaveString);
			} catch (ParseException e) {
				//Ignore oddly shaped leave date string, treat as if null
			}
		}
		return new Stay(id, childId, arrival, leave, billId);
	}

}
