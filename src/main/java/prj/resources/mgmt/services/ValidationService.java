package prj.resources.mgmt.services;

import prj.resources.exception.ResourceError;

public interface ValidationService {
	
	public int validateUserName(String userName) throws ResourceError;
	
	
	public int validateUserEmail(String email) throws ResourceError;

}
