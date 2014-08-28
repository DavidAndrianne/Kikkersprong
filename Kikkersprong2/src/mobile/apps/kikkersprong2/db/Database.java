package mobile.apps.kikkersprong2.db;

import java.io.Serializable;
import java.util.*;

import mobile.apps.kikkersprong2.MainActivity;
import mobile.apps.kikkersprong2.model.*;

public interface Database extends Serializable {
	public boolean testConnection();
	public Child registerChildArrival(String childId) throws DatabaseException;
	public Child registerChildLeave(String childId) throws DatabaseException;
	public List<Child> getAllChildren(MainActivity main) throws DatabaseException;
	public List<Bill> getAllBillsForChild(long childId) throws DatabaseException;
	public List<Stay> getAllStaysForChild(long childId) throws DatabaseException;
	public Child getChild(String id) throws DatabaseException;
}