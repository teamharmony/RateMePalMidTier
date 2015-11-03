package prj.resources.mgmt.services;

import java.util.List;

import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.Meeting;

public interface MeetingService {
	
	public void createMeeting(Meeting meeting) throws ResourceError;
	
	
	public List<Meeting> showMeetings(String userName) throws ResourceError;
	
	
	public void updateMeeting(String userName, int id, int status) throws ResourceError;
	
	

}
