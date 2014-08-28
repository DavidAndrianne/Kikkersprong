package mobile.apps.kikkersprong2.model;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;

import android.util.Log;

import mobile.apps.kikkersprong2.MainActivity;
import mobile.apps.kikkersprong2.db.*;

public class KikkersprongModel extends Observable implements Serializable{
	private static final long serialVersionUID = -697328477487886008L;
	private Database db = new DatabaseOffline();
	private boolean isOutOfSynch = false;

	private Child loggedInUser = null;
	private boolean authenticationModeOn = false;
	
	public KikkersprongModel(){}
	
	public String registerArrivalChild(String childId, String childName) throws DomainException{
		String name;
		if(!(childName != null && !childName.equals("")))
			throw new DomainException("ERROR: Invalid parameters found for arriving child '"+childName+"' with childId:"+childId+"!");
		try{
			loggedInUser = db.registerChildArrival(childId);
			return (loggedInUser.getNickname() != null && !loggedInUser.getNickname().isEmpty()) ? loggedInUser.getNickname() : loggedInUser.getFirstname();
		} catch (DatabaseException dbe){
			if(isUsingOnlineDb() && dbe.getLocalizedMessage().indexOf("Connection") != -1){
				setDb(new DatabaseOffline());
				name = this.registerArrivalChild(childId, childName); //Try again with OfflineDB
				setOutOfSynch(!isUsingOnlineDb()); //Set unsynchedChanges flag
				return name;
			} else {
				throw new DomainException(childName +", "+dbe.getLocalizedMessage());
			}
		}
	}
	
	private void setDb(Database database) {
		this.db = database;
		this.setChanged();
		this.notifyObservers(); //Let the controller know we are now in online/offline mode
	}

	public String registerLeaveChild(String childId, String childName) throws DomainException{
		String name;
		if(!(childName != null && !childName.equals("")))
			throw new DomainException("ERROR: Invalid parameters found for leaving child '"+childName+"' with childId:"+childId+"!");
		try{
			loggedInUser = db.registerChildLeave(childId);
			return (loggedInUser.getNickname() != null && !loggedInUser.getNickname().isEmpty()) ? loggedInUser.getNickname() : loggedInUser.getFirstname();
		} catch (DatabaseException dbe){
			if(isUsingOnlineDb() && dbe.getLocalizedMessage().indexOf("Connection") != -1){
				setDb(new DatabaseOffline()); //Switch to offline mode
				name = this.registerLeaveChild(childId, childName); //Try again with OfflineDB
				isOutOfSynch = !isUsingOnlineDb(); //Set unsynchedChanges flag
				return name;
			} else {
				throw new DomainException(childName +", "+dbe.getLocalizedMessage());
			}
		}
	}

	public void tryOnlineDbConnection() throws DomainException{
		try {
			DatabaseOnline dbo = new DatabaseOnline();
			if(!isUsingOnlineDb() && dbo.testConnection()){ //If we are using the offline DB and the connection now works
				if(isOutOfSynch){ //Synchronize if necessary
					Log.v("DEBUG", "Synchronizing sequence initated");
					((DatabaseOffline)db).flushChanges(dbo);
					isOutOfSynch = false;
				}
				setDb(dbo); //then set the DB back to online mode
			} else if(isUsingOnlineDb() && !dbo.testConnection()) { //If we are using the online DB and the connection doesn't work
				setDb(new DatabaseOffline()); //Then switch to offline mode
			}
		} catch (DatabaseException e) {
			if(isUsingOnlineDb()){ this.setDb(new DatabaseOffline());}
			throw new DomainException(e.getLocalizedMessage());
		}
	}
	
	public List<Child> getAllChildren(MainActivity main) throws DomainException {
		try {
			return db.getAllChildren(main);
		} catch (DatabaseException dbe) {
			if(isUsingOnlineDb()){
				setDb(new DatabaseOffline());
				return this.getAllChildren(main); //Try again with OfflineDB
			} else {
				throw new DomainException(dbe.getMessage());
			}
		}
	}
	
	public boolean isUsingOnlineDb() { return db instanceof DatabaseOnline;	}

	public void setAuthenticationMode(boolean b) { this.authenticationModeOn = b; }
	public boolean getAuthenticationMode() { return authenticationModeOn; }

	public void login(String id) throws DomainException {
		if(id.equals("-1")){
			this.loggedInUser = new Child("-1", "Admin", "Admin");
		} else {
			try {
				this.loggedInUser = db.getChild(id);
			} catch (DatabaseException dbe) {
				throw new DomainException(dbe.getMessage());
			}
		}
	}

	public Child getUser() { return this.loggedInUser; }

	public boolean isOutOfSynch() { return isOutOfSynch; }
	public void setOutOfSynch(boolean isOutOfSynch) { this.isOutOfSynch = isOutOfSynch;	}

	public Child getChild(String id) throws DomainException{
		try {
			return db.getChild(id);
		} catch (DatabaseException e) {
			throw new DomainException(e.getLocalizedMessage());
		}
	}

	public void logout() { this.loggedInUser = null; }
}