package mobile.apps.kikkersprong.db;

import java.util.List;

import mobile.apps.kikkersprong.MainActivity;
import mobile.apps.kikkersprong.model.Bill;
import mobile.apps.kikkersprong.model.Child;
import mobile.apps.kikkersprong.model.Stay;

public class DatabaseOffline implements Database {

	@Override
	public boolean testConnection() {
		return true;
	}

	@Override
	public Child registerChildArrival(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Child registerChildLeave(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Child> getAllChildren(MainActivity main) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bill> getAllBillsForChild(long childId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Stay> getAllStaysForChild(long childId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Child getChild(String id) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

}
