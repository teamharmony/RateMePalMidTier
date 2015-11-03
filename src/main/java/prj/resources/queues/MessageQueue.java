package prj.resources.queues;

import java.util.HashSet;
import java.util.Set;

public class MessageQueue {
	
	private static Set<String> messages = new HashSet<String>();
	
	public static void addMessage(String userName) {
		synchronized (MessageQueue.class) {
			messages.add(userName);
		}
		
	}

	public static boolean removeMessage(String userName) {
		synchronized (MessageQueue.class) {
			return messages.remove(userName);
		}
	}
	

}
