package mobile.apps.kikkersprong.model;

import java.util.List;
import java.util.Observable;

import mobile.apps.kikkersprong.MainActivity;
import mobile.apps.kikkersprong.db.*;

public class KikkersprongModel extends Observable{
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
			if(isUsingOnlineDb() && dbe.getLocalizedMessage().indexOf("connection") != -1){
				setDb(new DatabaseOffline());
				this.notifyObservers(); //Let the controller know we are now in offline mode
				name = this.registerArrivalChild(childId, childName); //Try again with OfflineDB
				setOutOfSynch(!isUsingOnlineDb()); //Set unsynchedChanges flag
				return name;
			} else {
				throw new DomainException(childName +", "+dbe.getMessage());
			}
		}
	}
	
	private void setDb(Database database) {
		this.db = database;
		this.notifyObservers();
	}

	public String registerLeaveChild(String childId, String childName) throws DomainException{
		String name;
		if(!(childName != null && !childName.equals("")))
			throw new DomainException("ERROR: Invalid parameters found for leaving child '"+childName+"' with childId:"+childId+"!");
		try{
			loggedInUser = db.registerChildLeave(childId);
			return (loggedInUser.getNickname() != null && !loggedInUser.getNickname().isEmpty()) ? loggedInUser.getNickname() : loggedInUser.getFirstname();
		} catch (DatabaseException dbe){
			if(isUsingOnlineDb() && dbe.getLocalizedMessage().indexOf("connection") != -1){
				setDb(new DatabaseOffline()); //Switch to offline mode
				this.notifyObservers(); //Let the controller know we are now in offline mode
				name = this.registerLeaveChild(childId, childName); //Try again with OfflineDB
				isOutOfSynch = !isUsingOnlineDb(); //Set unsynchedChanges flag
				return name;
			} else {
				throw new DomainException(childName +", "+dbe.getMessage());
			}
		}
	}

	public void tryOnlineDbConnection() {
		if(new DatabaseOnline().testConnection()){
			setDb(new DatabaseOnline());
			if(isOutOfSynch){
				//TODO: Submit unsynched local changes to the DB
				//unsynchedChanges = false;
			}
		}
	}
	
	public List<Child> getAllChildren(MainActivity main) throws DomainException {
		try {
			return db.getAllChildren(main);
		} catch (DatabaseException dbe) {
			if(isUsingOnlineDb()){
				setDb(new DatabaseOffline());
				this.notifyObservers(); //Let the controller know we are now in offline mode
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
		}
		try {
			this.loggedInUser = db.getChild(id);
		} catch (DatabaseException dbe) {
			throw new DomainException(dbe.getMessage());
		}
	}

	public Child getUser() { return this.loggedInUser; }

	public boolean isOutOfSynch() {
		return this.isOutOfSynch;
	}
	public void setOutOfSynch(boolean isOutOfSynch) {
		this.isOutOfSynch = isOutOfSynch;
		this.notifyObservers();
	}
}