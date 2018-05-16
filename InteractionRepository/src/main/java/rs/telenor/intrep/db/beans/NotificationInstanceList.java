package rs.telenor.intrep.db.beans;

import java.util.ArrayList;

public class NotificationInstanceList {
	private ArrayList<NotificationInstance> mNotificationInstanceList;

	public NotificationInstanceList() {		
		this.mNotificationInstanceList = new ArrayList<NotificationInstance>();
	}

	public ArrayList<NotificationInstance> getmNotificationInstanceList() {
		return mNotificationInstanceList;
	}

	public void setmNotificationInstanceList(ArrayList<NotificationInstance> mNotificationInstanceList) {
		this.mNotificationInstanceList = mNotificationInstanceList;
	}	
	
}
