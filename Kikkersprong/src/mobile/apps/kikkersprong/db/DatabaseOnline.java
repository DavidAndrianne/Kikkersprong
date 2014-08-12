package mobile.apps.kikkersprong.db;

import java.util.List;
import android.util.Log;
import mobile.apps.kikkersprong.model.*;

public class DatabaseOnline implements Database {
	
	@Override
	public boolean testConnection() {
		return false;
	}

	@Override
	public void registerChildArrival(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerChildLeave(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Child> getAllChildren() {
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

}