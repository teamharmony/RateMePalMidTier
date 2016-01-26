package prj.resources.mgmt.services;

import java.util.List;

import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.DataRequest;

//TODO: DAO Exception
//TODO: Change the naming to DAO 
public interface RequestService {
	
	/**
	 * Creates a new data Request to get n parameters rated by m friends
	 * 
	 * @param request
	 * @return
	 * @throws ResourceError 
	 */
	public void addDataRequest(DataRequest request) throws ResourceError;
	
	/**
	 * Fetches the DataRequests created by User.
	 * 
	 * @param userName
	 * @return
	 * @throws ResourceError 
	 */
	public List<DataRequest> getRatingRequestsByUser(String userName) throws ResourceError;
	
	/**
	 * Fetches DataRequests submitted to the user.
	 * 
	 * @param username
	 * @throws ResourceError 
	 */
	public List<DataRequest> getRatingRequestsToUser(String username) throws ResourceError;
	
	
}
