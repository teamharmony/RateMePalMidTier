package prj.resources.queues;

import java.util.HashSet;
import java.util.Set;

public class MeetingQueue {
	
	private static Set<String> meetings = new HashSet<String>();
	
	public static void addMeeting(String userName) {
		synchronized (MessageQueue.class) {
			meetings.add(userName);
		}
		
	}

	public static boolean removeMeeting(String userName) {
		synchronized (MessageQueue.class) {
			return meetings.remove(userName);
		}
		
	}

}
