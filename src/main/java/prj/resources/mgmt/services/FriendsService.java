package prj.resources.mgmt.services;

import java.util.List;

import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.User;

public interface FriendsService {

	public void addFriend(User user, User friend) throws ResourceError;
	
	public List<User> showFriends(User user) throws ResourceError;
	
	public List<User> showNonFriends(User user) throws ResourceError;
	
	public List<User> showInvitedFriends(User user) throws ResourceError;
	
	public List<User> showPendingFriends(User user) throws ResourceError;
	
	public void updateFriendStatus(User user, User friend, int status) throws ResourceError;
		
	public List<User> searchFriends(User user, String searchKey) throws ResourceError;
	
	public List<User> searchNonFriends(User user, String searchKey) throws ResourceError;
	
	
}
