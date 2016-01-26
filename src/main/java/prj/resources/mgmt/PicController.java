package prj.resources.mgmt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import prj.resources.exception.ResourceError;
import prj.resources.mgmt.services.RegistrationService;

@RequestMapping("/profilePic")
@Controller
public class PicController {

	@Autowired
	private RegistrationService registrationService;
	
	/**
	 * Get the binary data of the profile Picture representing a given user.
	 * @param userName
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{username:.+}", method = RequestMethod.GET)
	public byte[] getProfilePic(@PathVariable String username, HttpServletRequest request,
			HttpServletResponse response) throws ResourceError{
		
		//String username = request.getParameter("username");
		return  registrationService.getProfilePic(username);
	}

	
}
