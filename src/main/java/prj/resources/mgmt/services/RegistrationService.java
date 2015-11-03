package prj.resources.mgmt.services;

import java.util.List;

import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.Location;
import prj.resources.mgmt.domain.User;

//TODO: DAO Exception
//TODO: Change the naming to DAO 
public interface RegistrationService {
	
	/**
	 * Fetches User info from the back-end based on the supplied user name.
	 * 
	 * @param userName
	 * @return
	 * @throws ResourceError 
	 */
	public User getUserDetailsByName(String userName) throws ResourceError;
	
	/**
	 * Fetches the Email id of the user based on the supplied user name.
	 * 
	 * @param userName
	 * @return
	 * @throws ResourceError 
	 */
	public String getEmailByName(String userName) throws ResourceError;
	
	/**
	 * Registers a user in the database.
	 * 
	 * @param user
	 * @throws ResourceError 
	 */
	public void register(User user) throws ResourceError;
	
	/**
	 * updates a user
	 * @param user
	 * @throws ResourceError 
	 */
	public void update(User user) throws ResourceError;

	
	/**
	 * gets Profile picture of a user
	 * @param userName
	 * @return
	 * @throws ResourceError 
	 */
	public byte[] getProfilePic(String userName) throws ResourceError;
	
	/**
	 * updates the password of the user to a new generated one.
	 * @param pwd
	 * @return
	 * @throws ResourceError 
	 */
	public void updatePwd(String userName, String pwd, int reset) throws ResourceError;
	
	
	/**
	 * lets the client know if the password for a given user needs to be reset.
	 * @param userName
	 * @return
	 * @throws ResourceError 
	 */
	public int isPasswordResetNeeded(String userName) throws ResourceError;
	
	/**
	 * search by skill
	 * @param key
	 * @return
	 * @throws ResourceError 
	 */
	public List<User> findUserBySkill(String searchString) throws ResourceError;
	
	/**
	 * Search by Location 
	 * 
	 * @param latitude1
	 * @param latitude2
	 * @param longitude1
	 * @param longitude2
	 * @return
	 * @throws ResourceError
	 */
	public List<User> findUserByLocation(double latitude1, double latitude2, double longitude1, double longitude2) throws ResourceError;
	
	/**
	 * update a location
	 * 
	 * @param latitude1
	 * @param latitude2
	 * @param longitude1
	 * @param longitude2
	 * @throws ResourceError
	 */
	public void updateLocation(String username, Location location) throws ResourceError;
	
	/**
	 * search by name
	 * @param key
	 * @return
	 * @throws ResourceError 
	 */
	public List<User> findUserByName(String searchString) throws ResourceError;


	
}
