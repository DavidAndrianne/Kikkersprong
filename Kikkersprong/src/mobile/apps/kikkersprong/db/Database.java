package mobile.apps.kikkersprong.db;

import java.util.*;

import mobile.apps.kikkersprong.MainActivity;
import mobile.apps.kikkersprong.model.*;

public interface Database {
	public boolean testConnection();
	public Child registerChildArrival(String childId) throws DatabaseException;
	public Child registerChildLeave(String childId) throws DatabaseException;
	public List<Child> getAllChildren(MainActivity main) throws DatabaseException;
	public List<Bill> getAllBillsForChild(long childId) throws DatabaseException;
	public List<Stay> getAllStaysForChild(long childId) throws DatabaseException;
	public Child getChild(String id) throws DatabaseException;
}