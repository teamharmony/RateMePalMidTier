package prj.resources.mgmt;


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
import org.springframework.web.bind.annotation.RequestParam;

import prj.resources.exception.ClientErrorInfo;
import prj.resources.exception.ResourceError;
import prj.resources.mgmt.filter.FilteredRequest;
import prj.resources.mgmt.services.MailNotifier;
import prj.resources.mgmt.services.RandomGenerator;
import prj.resources.mgmt.services.RegistrationService;

@RequestMapping("/password")
@Controller
public class PasswordGenerationController {

	@Autowired
	RegistrationService registrationService;

	@Autowired
	MailNotifier mailNotifier;

	@Autowired
	RandomGenerator pwdGenerator;

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
	 * sets a temporary password for the user and sends an email.
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/forgot", method = RequestMethod.PUT)
	public ResponseEntity<?> forgotPassword(	@RequestBody MultiValueMap<String,String> body) throws ResourceError{
			String email = registrationService.getEmailByName(body.getFirst("username"));
			String newPassword = pwdGenerator.generatePwd();
			registrationService.updatePwd(body.getFirst("username"), newPassword, 1);
			String subject = "Password for InstaMeet";
			String message = "Dear Sir \n\n" +
					"The new password is " + newPassword + "\n\n" +
					"Thank you.";
			mailNotifier.sendEmail("system@localhost", email, message, subject);
			return new ResponseEntity<Object>(HttpStatus.OK);

	}

	/**
	 * resets the user's password. This functionality is activated post re-login after after forgot password.
	 * @param username
	 * @param pwd
	 * @return
	 */
	@RequestMapping(value = "/reset", method = RequestMethod.PUT)
	public ResponseEntity<?> resetPassword(@RequestBody MultiValueMap<String,String> body, HttpServletRequest request) throws ResourceError{
		String username = request.getParameter("username");
		registrationService.updatePwd(username, body.getFirst("password"), 0);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	/**
	 * Is used to determine if the user's password needs to be reset.
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public ResponseEntity<?> isResetNeeded(HttpServletRequest request) throws ResourceError {
		int reset = 0;
		String username = request.getParameter("username");
		reset = registrationService.isPasswordResetNeeded(username);
		return new ResponseEntity<Object>(Integer.valueOf(reset), HttpStatus.OK);
	}

}
