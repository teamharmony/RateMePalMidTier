package prj.resources.mgmt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import prj.resources.exception.ClientErrorInfo;
import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.Location;
import prj.resources.mgmt.domain.User;
import prj.resources.mgmt.filter.FilteredRequest;
import prj.resources.mgmt.services.RegistrationService;

@RequestMapping("/resources")
@Controller
public class ResourceController {

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
	
	
	// TODO: Need a common validator end-point Which can be utilized by both client and mid tier.

	//Currently we rely on multi-part form data  request and each form element is separated by a boundary.
	//Going forward we can have just 2 parameters in the multi-part request, one representing
	//the user object and the second the associated Image.
	
	/**
	 * Registers a new user
	 * 
	 * @param fName
	 * @param mName
	 * @param lName
	 * @param city
	 * @param state
	 * @param country
	 * @param zipCode
	 * @param contact
	 * @param username
	 * @param email
	 * @param password
	 * @param confirmPass
	 * @param profilPic
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<User> registerNewUser(
			@RequestParam(required = true, value = "name") String name,
			@RequestParam(required = true, value = "contact") String contact,
			@RequestParam(required = true, value = "visible", defaultValue="0") String visible,
			@RequestParam(required = true, value = "username") String username,
			@RequestParam(required = true, value = "email") String email,
			@RequestParam(required = true, value = "password") String password,
			@RequestParam(required = false, value = "profilePic") MultipartFile profilePic) throws ResourceError, IOException {

			User user = new User.UserBuilder()
					.name(name)
					.contact(contact)
					.userName(username)
					.email(email)
					.password(password)
					.visible(Integer.parseInt(visible))
					.profilePic(
							profilePic != null ? profilePic.getBytes() : null).build();
			registrationService.register(user);
			return new ResponseEntity<User>(HttpStatus.CREATED);
	}

	
	/**
	 * Fetches the details of a given user based on the user-name.
	 * @param username
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/fetch", method = RequestMethod.GET)
	public User getUser(HttpServletRequest request) throws ResourceError{
		User user = registrationService.getUserDetailsByName(((FilteredRequest)request).getUserName());
		return user;
	}

	
	/**
	 * Updates the details for a given user
	 * 
	 * @param username
	 * @param fName
	 * @param mName
	 * @param lName
	 * @param city
	 * @param state
	 * @param country
	 * @param zipCode
	 * @param contact
	 * @param profilPic
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<User> updateUser(
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "zipCode") String zipCode,
			@RequestParam(required = true, value = "visible", defaultValue="0") String visible,
			@RequestParam(required = false, value = "email") String email,
			@RequestParam(required = false, value = "contact") String contact,
			@RequestParam(required = false, value = "profilePic") MultipartFile profilePic,
			HttpServletRequest request)  throws ResourceError, IOException{
			
			String username = request.getParameter("username");

			User user = new User.UserBuilder()
					.name(name)
					.contact(contact)
					.userName(username)
					.email(email)
					.visible(Integer.parseInt(visible))
					.userName(username)
					.profilePic(
							profilePic != null ? profilePic.getBytes() : null)
					.build();
			
			registrationService.update(user);
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

	
	@RequestMapping(value = "/updateLocation", method = RequestMethod.PUT)
	public ResponseEntity<User> updateLocation(
			@RequestBody MultiValueMap<String,String> body, 
			HttpServletRequest request) throws ResourceError{
			
			String username = request.getParameter("username");
			double latitude = Double.parseDouble(body.getFirst("latitude"));
			double longitude = Double.parseDouble(body.getFirst("longitude"));
			
			Location loc = new Location();
			loc.setLatitude(latitude);
			loc.setLongitude(longitude);
			
			registrationService.updateLocation(username, loc);
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}
	
	
	
	

}
