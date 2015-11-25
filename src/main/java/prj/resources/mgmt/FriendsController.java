package prj.resources.mgmt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import prj.resources.exception.ClientErrorInfo;
import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.User;
import prj.resources.mgmt.domain.Parameter;
import prj.resources.mgmt.domain.ParameterType;
import prj.resources.mgmt.domain.User.UserBuilder;
import prj.resources.mgmt.services.FriendsService;
import prj.resources.mgmt.services.ParameterService;

@RequestMapping("/friends")
@Controller
public class FriendsController {

	@Autowired
	private FriendsService friendsService;

	@ExceptionHandler()
	public ResponseEntity<ClientErrorInfo> errorHandle(Exception e) {
		ClientErrorInfo c;
		String info = e.getMessage();
		ClientErrorInfo.ErrorType type = ClientErrorInfo.ErrorType.GENERIC;
		if(e instanceof ResourceError)
		{
			info = ((ResourceError)e).getErrorString();
			type = ClientErrorInfo.ErrorType.DATA_ACESS;
		}
		c = new ClientErrorInfo(info, type);
		return new ResponseEntity<ClientErrorInfo>(c, HttpStatus.CONFLICT);
	}

	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public void addFriend(@RequestBody MultiValueMap<String,String> body, 
			HttpServletRequest request) throws ResourceError {
		String creator = request.getParameter("username");
		String friendUserName = body.getFirst("friendUserName");
		
		User self = new User.UserBuilder().userName(creator).build();
		User friend = new User.UserBuilder().userName(friendUserName).build();
		
		friendsService.addFriend(self, friend);
	}
	
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public List<User> showFriends(HttpServletRequest request) throws ResourceError {
		return friendsService.showFriends(new User.UserBuilder().userName(request.getParameter("username")).build());
	}


	@ResponseBody
	@RequestMapping(value="/notInvited", method = RequestMethod.GET)
	public List<User> showNonFriends(HttpServletRequest request) throws ResourceError {
		return friendsService.showNonFriends(new User.UserBuilder().userName(request.getParameter("username")).build());
	}


	@ResponseBody
	@RequestMapping(value="/invited", method = RequestMethod.GET)
	public List<User> showInvitedFriends(HttpServletRequest request) throws ResourceError {
		return friendsService.showInvitedFriends(new User.UserBuilder().userName(request.getParameter("username")).build());
	}


	@ResponseBody
	@RequestMapping(value="/pending", method = RequestMethod.GET)
	public List<User> showPendingFriends(HttpServletRequest request) throws ResourceError {
		return friendsService.showPendingFriends(new User.UserBuilder().userName(request.getParameter("username")).build());
	}

	
	@ResponseBody
	@RequestMapping(value="/updateStatus", method = RequestMethod.PUT)
	public void updateFriendStatus(@RequestBody MultiValueMap<String,String> body, 
			HttpServletRequest request) throws ResourceError {
		String username = request.getParameter("username");
		String friendname = body.getFirst("friendUserName");
		int status = Integer.parseInt(body.getFirst("status"));
		
		User self = new User.UserBuilder().userName(username).build();
		User friend = new User.UserBuilder().userName(friendname).build();
		
		friendsService.updateFriendStatus(self, friend, status);
	}	
}
