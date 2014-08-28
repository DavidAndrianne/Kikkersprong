package mobile.apps.kikkersprong2.model;

import java.io.Serializable;

public abstract class DBSynchedObject implements Serializable {
	private static final long serialVersionUID = -6212380418790172527L;
	private boolean isSynched = true;

	public boolean isSynched() { return isSynched; }
	public void setSynched(boolean isSynched) {	this.isSynched = isSynched;	}
}
