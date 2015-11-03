package prj.resources.mgmt.services;

import java.util.List;

import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.Message;

public interface MessageService {
	
	public void createMessage(Message message) throws ResourceError;
	
	
	public List<Message> showMessages(String userName) throws ResourceError;
	
	
	public void updateMessage(String userName, int id, int status) throws ResourceError;
	

}
