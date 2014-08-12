package mobile.apps.kikkersprong.model;

import java.util.Date;

public class Stay {
	private long child;
	private Date arrival;
	private Date leave;
	
	public Stay(long childId){
		setChild(childId);
	}
	
	public long getChild() { return child; }	
	public void setChild(long child) { this.child = child; }
	
	public Date getArrival() { return arrival; }
	public void setArrival(Date arrival) { this.arrival = arrival; }

	public Date getLeave() { return leave; }
	public void setLeave(Date leave) { this.leave = leave; }
}