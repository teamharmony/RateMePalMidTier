package prj.resources.mgmt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import prj.resources.exception.ClientErrorInfo;
import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.User;
import prj.resources.mgmt.services.FriendsService;

@RequestMapping("/search")
@Controller
public class SearchController {
	
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
	
	
	/**
	 * find by Name in Friends List.
	 * @param key
	 * @param searchString
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/friends", method = RequestMethod.GET)
	public List<User> findFriendByName(@RequestParam(required = true, value = "searchKey") String searchString, HttpServletRequest request) throws ResourceError{
		return  friendsService.searchFriends(new User.UserBuilder().name(request.getParameter("username")).build(), searchString);
	}

	
	
	/**
	 * find by Name in Non-Friends List.
	 * @param key
	 * @param searchString
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/nonFriends", method = RequestMethod.GET)
	public List<User> findNonFriendByName(@RequestParam(required = true, value = "searcKey") String searchString, HttpServletRequest request) throws ResourceError{
		return  friendsService.searchNonFriends(new User.UserBuilder().name(request.getParameter("username")).build(), searchString);
	}

}
