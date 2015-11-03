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
import prj.resources.mgmt.domain.Meeting;
import prj.resources.mgmt.services.MeetingService;

@RequestMapping("/meetings")
@Controller
public class MeetingController {

	@Autowired
	private MeetingService meetingService;

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
	public void createMeeting(@RequestBody MultiValueMap<String,String> body, 
			HttpServletRequest request) throws ResourceError {
		String fromUserName = request.getParameter("username");
		String ToUserName = body.getFirst("toUserName");
		String agenda = body.getFirst("agenda");
		String details = body.getFirst("details");
		long dateTime = Long.parseLong(body.getFirst("dateTime"));
		String venue = body.getFirst("venue");
		
		Meeting meeting  = new Meeting.MeetingBuilder()
										.fromUserName(fromUserName)
										.toUserName(ToUserName)
										.agenda(agenda)
										.details(details)
										.venue(venue)
										.dateTime(dateTime)
										.build();
		
		meetingService.createMeeting(meeting);
	}
	
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public List<Meeting> getMeetings(HttpServletRequest request) throws ResourceError {
			
		return meetingService.showMeetings(request.getParameter("username"));

	}


	@ResponseBody
	@RequestMapping(value="/{meetingId}", method = RequestMethod.PUT)
	public void updateMeeting(@PathVariable Integer meetingId, 
			@RequestBody MultiValueMap<String,String> body, 
			HttpServletRequest request) throws ResourceError {
			
		int status = Integer.parseInt(body.getFirst("status"));
		meetingService.updateMeeting(request.getParameter("username"), meetingId.intValue(), status);

	}
	
}
