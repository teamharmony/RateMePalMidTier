package prj.resources.mgmt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import prj.resources.mgmt.services.ValidationService;

@RequestMapping("/validate")
@Controller
public class ValidationController {

	@Autowired
	private ValidationService validationService;

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
	@RequestMapping(value = "/username", method = RequestMethod.POST)
	public int isUserNameOccupied(
			@RequestParam(required = true, value = "username") String userName,
			HttpServletRequest request) throws ResourceError {

		return validationService.validateUserName(userName);
		
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/email", method = RequestMethod.POST)
	public int isEmailOccupied(
			@RequestParam(required = true, value = "email") String email,
			HttpServletRequest request) throws ResourceError {

		return validationService.validateUserEmail(email);
	}


}
