package mobile.apps.kikkersprong.model;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class Stay {
	private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	private String id;
	private String childId;
	private String billId;
	private Date arrival;
	private Date leave;
	
	public Stay(String id, String childId, Date arrival){
		setId(id);
		setChildId(childId);
		setArrival(arrival);
	}
	
	public Stay(String id, String childId, Date arrival, Date leave, String billId) {
		setId(id);
		setChildId(childId);
		setArrival(arrival);
		setLeave(arrival);
		setBillId(billId);
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id.trim(); }
	
	public String getChildId() { return childId; }	
	public void setChildId(String childId) { this.childId = childId.trim(); }
	
	public String getBillId() {	return billId; }
	public void setBillId(String billId) { this.billId = billId.trim(); }
	
	public Date getArrival() { return arrival; }
	public void setArrival(Date arrival) { this.arrival = arrival; }
	public void setArrival(String s) throws DomainException { 
		try { this.arrival = new SimpleDateFormat(dateFormat).parse(s); } 
		catch (ParseException pe) { throw new DomainException(pe.getLocalizedMessage());}
	}

	public Date getLeave() { return leave; }
	public void setLeave(Date leave) { this.leave = leave; }
	public void setLeave(String s) throws DomainException { 
		try { this.leave = new SimpleDateFormat(dateFormat).parse(s); } 
		catch (ParseException pe) { throw new DomainException(pe.getLocalizedMessage());}
	}
}