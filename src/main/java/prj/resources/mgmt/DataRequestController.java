package prj.resources.mgmt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import prj.resources.exception.ClientErrorInfo;
import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.DataRequest;
import prj.resources.mgmt.domain.User;
import prj.resources.mgmt.filter.FilteredRequest;
import prj.resources.mgmt.services.NotificationsUtil;
import prj.resources.mgmt.services.RequestService;

@RequestMapping("/dataRequest")
@Controller
public class DataRequestController {

	@Autowired
	private RequestService requestService;
	
	
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
	public ResponseEntity<List<DataRequest>> addDataRequest(@RequestBody DataRequest dataRequest, 
			HttpServletRequest request) throws ResourceError {
		
		List<DataRequest> detailIds  = requestService.addDataRequest(dataRequest);
		
		String[] usernames = new String[dataRequest.getFriends().length];
		int i=0;
		for(User u : dataRequest.getFriends()) {
			usernames[i++] = u.getUsername();
		}
		
		if(dataRequest.getFriendCreated() == 0) {
			String template = request.getParameter("username") + " sent you a rating request.";
			NotificationsUtil.sendNotification(template, usernames);
		}
		
		return new ResponseEntity<List<DataRequest>>(detailIds, HttpStatus.CREATED);
	}
	
	@ResponseBody
	@RequestMapping(value="/byMe", method = RequestMethod.GET)
	public List<DataRequest> getRatingRequestsByUser(HttpServletRequest request) throws ResourceError{
		List<DataRequest> requests = requestService.getRatingRequestsByUser(((FilteredRequest)request).getUserName());
		return requests;
	}

	
	@ResponseBody
	@RequestMapping(value="/toMe", method = RequestMethod.GET)
	public List<DataRequest> getRatingRequestsToUser(HttpServletRequest request) throws ResourceError{
		List<DataRequest> requests = requestService.getRatingRequestsToUser(((FilteredRequest)request).getUserName());
		return requests;
	}

		
	
}
