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
import prj.resources.mgmt.domain.Message;
import prj.resources.mgmt.services.MessageService;

@RequestMapping("/messages")
@Controller
public class MessageController {

	@Autowired
	private MessageService messageService;

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
		String subject = body.getFirst("subject");
		String message = body.getFirst("message");
		
		Message messageReq  = new Message.MessageBuilder()
										.fromUserName(fromUserName)
										.toUserName(ToUserName)
										.subject(subject)
										.message(message)
										.build();
		
		messageService.createMessage(messageReq);
	}
	
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public List<Message> getMessages(HttpServletRequest request) throws ResourceError {
			
		return messageService.showMessages(request.getParameter("username"));

	}


	@ResponseBody
	@RequestMapping(value="/{messageId}", method = RequestMethod.PUT)
	public void updateMessage(@PathVariable Integer messageId, 
			@RequestBody MultiValueMap<String,String> body, 
			HttpServletRequest request) throws ResourceError {
			
		int status = Integer.parseInt(body.getFirst("status"));
		messageService.updateMessage(request.getParameter("username"), messageId.intValue(), status);

	}
	
}
