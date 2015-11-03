package prj.resources.mgmt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import prj.resources.exception.ClientErrorInfo;
import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.Feedback;
import prj.resources.mgmt.services.FeedbackService;
import prj.resources.mgmt.services.MailNotifier;
import prj.resources.mgmt.services.RegistrationService;

@RequestMapping("/feedback")
@Controller
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	MailNotifier mailNotifier;

	
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
	public void createFeedback(@RequestBody MultiValueMap<String,String> body, 
			HttpServletRequest request) throws ResourceError {
		String fromUserName = request.getParameter("username");
		String subject = body.getFirst("subject");
		String message = body.getFirst("message");
		
		Feedback feedback  = new Feedback.FeedbackBuilder()
										.fromUserName(fromUserName)
										.subject(subject)
										.message(message)
										.build();
		
		feedbackService.createMessage(feedback);
		String from = registrationService.getEmailByName(fromUserName);
		mailNotifier.sendEmail(from, "feedback4mmp@gmail.com", message, subject);//todo: define the to param in config.
	}
	
	
	
}
