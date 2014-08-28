package mobile.apps.kikkersprong.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Bill {
	private Child child;
	private String id;
	private List<Stay> period = new ArrayList<Stay>();
	private boolean isPaid = false;
	private double total;
	
	public Bill(Child child){
		setChild(child);
	}
	
	public File generateReminder(){
		return new File("MyReminder.txt");
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	
	public List<Stay> getPeriod() {	return period; }
	public void setPeriod(List<Stay> period) { this.period = period; }

	public Child getChild() { return child;	}
	public void setChild(Child child) {	this.child = child;	}

	public double getTotal() { return total; }
	public void setTotal(double total) { this.total = total; }
	
	public boolean isPaid() { return isPaid; }
	public void setPaid(boolean isPaid) { this.isPaid = isPaid;	}
}