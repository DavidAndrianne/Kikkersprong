package mobile.apps.kikkersprong2.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import mobile.apps.kikkersprong2.MainActivity;
import mobile.apps.kikkersprong2.db.JSON.*;
import mobile.apps.kikkersprong2.model.*;

@SuppressLint("SimpleDateFormat")
public class DatabaseOnline implements Database {
	private static final long serialVersionUID = 5096170700767213761L;
	private final String hostname = "http://192.168.225.1:81/";//"http://david-toshi:81/";//"http://192.168.0.192:81/"; 
	private final String applicationSource = "KikkersprongRemoteDB/";
	private final String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	@Override
	public boolean testConnection() {
		TestConnectionAsynchTask kikkeronline = new TestConnectionAsynchTask();
		String scriptname = "test.php";
		AsyncTask<String, String, Boolean> task = kikkeronline.execute(this.buildUrl(scriptname));
		try {
			return task.get();
		} catch (InterruptedException e) {
		} catch (ExecutionException e) { }
		return false;
	}

	@Override
	public Child registerChildArrival(String id) throws DatabaseException {
		return registerChildArrival(id, new Date());
	}

	@Override
	public Child registerChildLeave(String id) throws DatabaseException {
		return registerChildLeave(id, new Date());
	}

	@Override
	public List<Child> getAllChildren(MainActivity main) {
		List<Child> childList = new ArrayList<Child>();
		GetAllChildrenAsynchTask kikkeronline = 
				 new GetAllChildrenAsynchTask(main);
		String scriptname = "getAllChildren.php";
		AsyncTask<String, String, List<Child>> task = kikkeronline.execute(this.buildUrl(scriptname));
		try {
			childList = task.get();
		} catch (InterruptedException e) {
		} catch (ExecutionException e) { }

		return childList;
	}

	private String buildUrl(String scriptname) { //I.E. return : localhost:81/KikkersprongRemoteDB/getAllChildren.php
		return hostname + applicationSource + scriptname;
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

	public void setChildren(List<Child> children) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Child getChild(String id) throws DatabaseException {
		Log.v("DEBUG", "Preparing JSON statement");
		GetChildAsynchTask kikkeronline = new GetChildAsynchTask();
		String scriptname = "getChild.php";
		AsyncTask<String, String, Child> task = kikkeronline.execute(this.buildUrl(scriptname), id);
		try {
			return task.get();
		} catch (InterruptedException e) {
		} catch (ExecutionException e) { }
		throw new DatabaseException("An error occured while fetching user with id:"+id+"!");
	}

	public Child registerChildArrival(String id, Date d) throws DatabaseException {
		Child child = null;
		RegisterArrivalChildAsynchTask kikkeronline = new RegisterArrivalChildAsynchTask();
		String scriptname = "registerArrivalChild.php";
		String date = new SimpleDateFormat(dateFormat).format(d);
		AsyncTask<String, String, Object[]> task = kikkeronline.execute(this.buildUrl(scriptname), id, date);
		try {
			Object[] result = task.get();
			child = (Child) result[0];
			@SuppressWarnings("unused")
			Stay stay = (Stay) result[1]; //Might be useful later
		} catch (InterruptedException e) {
		} catch (ExecutionException e) { 
		} catch (NullPointerException e){ 
			/* Two likely Scenario's 
			 * Scenario 1:the database server / wifi connection has been compromised */
			if(!this.testConnection()){
				throw new DatabaseException("Connection error!");
			} else { // Scenario 2: the child has already been checked in.
				throw new DatabaseException("You've already checked in!");
			}
		}
		return child;
	}

	public Child registerChildLeave(String id, Date d) throws DatabaseException {
		Child child = null;
		RegisterArrivalChildAsynchTask kikkeronline = 
				 new RegisterArrivalChildAsynchTask();
		String scriptname = "registerLeaveChild.php";
		String date = new SimpleDateFormat(dateFormat).format(d);
		AsyncTask<String, String, Object[]> task = kikkeronline.execute(this.buildUrl(scriptname), id, date);
		try {
			Object[] result = task.get();
			child = (Child) result[0];
			@SuppressWarnings("unused")
			Stay stay = (Stay) result[1]; //Might be useful later
		} catch (InterruptedException e) {
		} catch (ExecutionException e) { 
		} catch (NullPointerException e){ 
			/* Two likely Scenario's 
			 * Scenario 1:the database server / wifi connection has been compromised */
			if(!this.testConnection()){
				throw new DatabaseException("Connection error!");
			} else { // Scenario 2: the child has already been checked out.
				throw new DatabaseException("You've already checked in!");
			}
		}
		return child;
	}

}