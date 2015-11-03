package prj.resources.mgmt;

import java.util.List;

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
import prj.resources.mgmt.services.RegistrationService;

@RequestMapping("/search")
@Controller
public class SearchController {
	
	@Autowired
	RegistrationService registrationService;

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
	 * find by either skill.
	 * @param key
	 * @param searchString
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/skills", method = RequestMethod.GET)
	public List<User> findBySkill(@RequestParam(required = true, value = "skills") String searchString) throws ResourceError{
		return registrationService.findUserBySkill(searchString); 
	}

	
	/**
	 * find by either skill.
	 * @param key
	 * @param searchString
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/name", method = RequestMethod.GET)
	public List<User> findByName(@RequestParam(required = true, value = "name") String searchString) throws ResourceError{
		return registrationService.findUserByName(searchString); 
	}

	
	/**
	 * find by either skill.
	 * @param key
	 * @param searchString
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/location", method = RequestMethod.GET)
	public List<User> findByLocation(
			@RequestParam(required = true, value = "latitude1") double latitude1,
			@RequestParam(required = true, value = "latitude2") double latitude2,
			@RequestParam(required = true, value = "longitude1") double longitude1,
			@RequestParam(required = true, value = "longitude2") double longitude2
			) throws ResourceError{
		return registrationService.findUserByLocation(latitude1, latitude2, longitude1, longitude2); 
	}

}
