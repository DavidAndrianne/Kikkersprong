package mobile.apps.kikkersprong2.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

public class ChildSimpleFactory {
	private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	public static Child createChildFromJsonObject(JSONObject jObject) throws DomainException{
		String id = null, firstname = null, name = null, nickname = null, dateString = null, email = null;
		try {
			id = jObject.getString("id");
			firstname = jObject.getString("firstname");
			name = jObject.getString("name");
			nickname = jObject.getString("nickname");
			dateString = jObject.getJSONObject("birthday").getString("date");
			email = jObject.getString("email");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Date birthday = null;
		try {
			if(dateString != null && !dateString.isEmpty())
				birthday = new SimpleDateFormat(dateFormat, Locale.GERMANY).parse(dateString);
		} catch (ParseException e) {
			// Ignore odd dateString formats, treat as null
		}
		if(id == null || firstname == null || name == null){ throw new DomainException("id, firstname or name attributes can't be null!");}
		return new Child(id, firstname, name, nickname, birthday, email);
	}
}
