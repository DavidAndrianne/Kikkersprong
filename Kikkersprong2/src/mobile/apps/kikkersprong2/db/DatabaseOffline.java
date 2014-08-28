package mobile.apps.kikkersprong2.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;

import mobile.apps.kikkersprong2.MainActivity;
import mobile.apps.kikkersprong2.model.Bill;
import mobile.apps.kikkersprong2.model.Child;
import mobile.apps.kikkersprong2.model.Stay;

public class DatabaseOffline implements Database {
	private List<Child> childrenToRegisterArriving = new ArrayList<Child>();
	private List<Date> timeStampsArrivals = new ArrayList<Date>();
	
	private List<Child> childrenToRegisterLeaving = new ArrayList<Child>();
	private List<Date> timeStampsLeaves = new ArrayList<Date>();
	
	@Override
	public boolean testConnection() {
		return true;
	}

	
	//The offline DB stores the toRegisterChild()
	@Override
	public Child registerChildArrival(String id) {
		Child c = createUnsychedChild();
		c.setId(id);
		childrenToRegisterArriving.add(c);
		timeStampsArrivals.add(new Date());
		return c;
	}

	@Override
	public Child registerChildLeave(String id) {
		Child c = createUnsychedChild();
		c.setId(id);
		childrenToRegisterLeaving.add(c);
		timeStampsLeaves.add(new Date());
		return c;
	}

	@Override
	public List<Child> getAllChildren(MainActivity main) {
		return new ArrayList<Child>();
	}

	@Override
	public List<Bill> getAllBillsForChild(long childId) {
		return new ArrayList<Bill>();
	}

	@Override
	public List<Stay> getAllStaysForChild(long childId) {
		return new ArrayList<Stay>();
	}

	@Override
	public Child getChild(String id) throws DatabaseException {
		Child c = createUnsychedChild();
		c.setId(id);
		return c;
	}
	
	private Child createUnsychedChild(){
		Child c = new Child();
		c.setFirstname("Database Offline!");
		c.setSynched(false);
		return c;
	}


	public void flushChanges(DatabaseOnline dbo) throws DatabaseException {
		for(int i = 0; i < childrenToRegisterArriving.size(); i++){
			Child c = childrenToRegisterArriving.get(i);
			Date d = timeStampsArrivals.get(i);
			Log.v("DEBUG", "registering arrival of child with ID : "+c.getId());
			dbo.registerChildArrival(c.getId(), d);
		}
		for(int i = 0; i < childrenToRegisterLeaving.size(); i++){
			Child c = childrenToRegisterLeaving.get(i);
			Date d = timeStampsLeaves.get(i);
			Log.v("DEBUG", "registering leaving of child with ID : "+c.getId());
			dbo.registerChildLeave(c.getId(), d);
		}
	}

}