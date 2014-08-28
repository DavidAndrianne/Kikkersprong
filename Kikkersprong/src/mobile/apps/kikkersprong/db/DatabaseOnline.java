package mobile.apps.kikkersprong.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import mobile.apps.kikkersprong.MainActivity;
import mobile.apps.kikkersprong.db.JSON.*;
import mobile.apps.kikkersprong.model.*;

@SuppressLint("SimpleDateFormat")
public class DatabaseOnline implements Database {
	private final String hostname = "http://192.168.0.191:81/";
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
		Child child = null;
		RegisterArrivalChildAsynchTask kikkeronline = 
				 new RegisterArrivalChildAsynchTask();
		String scriptname = "registerArrivalChild.php";
		String date = new SimpleDateFormat(dateFormat).format(new Date());
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

	@Override
	public Child registerChildLeave(String id) {
		// TODO Auto-generated method stub
		return null;
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
		GetChildAsynchTask kikkeronline = new GetChildAsynchTask();
		String scriptname = "getChild.php";
		AsyncTask<String, String, Child> task = kikkeronline.execute(this.buildUrl(scriptname), id);
		try {
			return task.get();
		} catch (InterruptedException e) {
		} catch (ExecutionException e) { }
		throw new DatabaseException("An error occured while fetching user with id:"+id+"!");
	}

}