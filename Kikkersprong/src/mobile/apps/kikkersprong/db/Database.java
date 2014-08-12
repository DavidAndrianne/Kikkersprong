package mobile.apps.kikkersprong.db;

import java.util.*;
import mobile.apps.kikkersprong.model.*;

public interface Database {
	public boolean testConnection();
	public void registerChildArrival(long id);
	public void registerChildLeave(long id);
	public List<Child> getAllChildren();
	public List<Bill> getAllBillsForChild(long childId);
	public List<Stay> getAllStaysForChild(long childId);
}