package prj.resources.queues;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FriendsQueue {
	
	private static Set<String> messages = new HashSet<String>();
	
	public static void addMessage(String fromUserName, String status) {
		synchronized (FriendsQueue.class) {
			messages.add(fromUserName + ":" + status);
		}
		
	}

	public static boolean hasStatus(String userName, String status) {
		synchronized (FriendsQueue.class) {
			Iterator<String> i = messages.iterator();
			while(i.hasNext()) {
				if(i.next().indexOf(userName + ":" + status) != -1) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public static void removeMessage(String userName) {
		synchronized (FriendsQueue.class) {
			Iterator<String> i = messages.iterator();
			while(i.hasNext()) {
				if(i.next().indexOf(userName + ":") != -1) {
					i.remove();
				}
			}
		}
	}
	

}
