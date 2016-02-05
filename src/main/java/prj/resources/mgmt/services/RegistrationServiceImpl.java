package prj.resources.mgmt.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.Location;
import prj.resources.mgmt.domain.User;

//TODO: Implement AOP for Logging

public class RegistrationServiceImpl implements RegistrationService {

	private static final Logger logger = LoggerFactory
			.getLogger(RegistrationServiceImpl.class);

	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Fetches User info from the back-end based on the supplied user name.
	 * 
	 * @param userName
	 * @return
	 * @throws ResourceError 
	 */
	public User getUserDetailsByName(String userName) throws ResourceError {
		User user = null;
		try {
			SimpleJdbcCall getUserDetailsJdbcCall = new SimpleJdbcCall(
					dataSource).withProcedureName("getUserDetails");

			SqlParameterSource in = new MapSqlParameterSource().addValue(
					"_username", userName);
			Map<String, Object> out = getUserDetailsJdbcCall.execute(in);

			user = new User.UserBuilder().userName(userName)
					.name((String) out.get("_name"))
					.email((String) out.get("_email"))
					.contact((String) out.get("_contact"))
					.visible((Integer) out.get("_visible"))
					.build();

			
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		return user;
	}

	/**
	 * Fetches the Email id of the user based on the supplied user name.
	 * 
	 * @param userName
	 * @return
	 * @throws ResourceError 
	 */
	public String getEmailByName(String userName) throws ResourceError {
		User user = null;
		try {
			user = getUserDetailsByName(userName);
			
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		return user.getEmail();
	}

	/**
	 * Registers a user in the database.
	 * 
	 * @param user
	 * @throws ResourceError 
	 */
	public void register(User user) throws ResourceError {
		try {
			SimpleJdbcCall registerJdbcCall = new SimpleJdbcCall(dataSource)
					.withProcedureName("registerUser");

			Map<String, Object> inputData = new HashMap<String, Object>();

			inputData.put("_username", user.getUsername());
			inputData.put("_pwd", user.getPassword());
			inputData.put("_authority", "ROLE_USER");
			inputData.put("_name", user.getName());
			inputData.put("_visible", user.getVisible());
			inputData.put("_email", user.getEmail());
			inputData.put("_contact", user.getContact());
			inputData.put("_profilePic", user.getProfilePic());
			inputData.put("_designation", user.getDesignation());
			inputData.put("_description", user.getDescription());

			SqlParameterSource in = new MapSqlParameterSource()
					.addValues(inputData);
			registerJdbcCall.execute(in);
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}

	}

	/**
	 * Registers a user in the database.
	 * 
	 * @param user
	 * @throws ResourceError 
	 */
	public void update(User user) throws ResourceError {
		try {
			SimpleJdbcCall updateJdbcCall = new SimpleJdbcCall(dataSource)
					.withProcedureName("updateUser");

			Map<String, Object> inputData = new HashMap<String, Object>();
			inputData.put("_username", user.getUsername());
			inputData.put("_name", user.getName());
			inputData.put("_visible", user.getVisible());
			inputData.put("_email", user.getEmail());
			inputData.put("_contact", user.getContact());
			inputData.put("_profilePic", user.getProfilePic());
			inputData.put("_designation", user.getDesignation());
			inputData.put("_description", user.getDescription());

			SqlParameterSource in = new MapSqlParameterSource()
					.addValues(inputData);
			updateJdbcCall.execute(in);
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
	}

	public byte[] getProfilePic(String userName) throws ResourceError {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall getUserDetailsJdbcCall = new SimpleJdbcCall(
					dataSource).withProcedureName("getUserProfilePic");

			SqlParameterSource in = new MapSqlParameterSource().addValue(
					"_username", userName);
			out = getUserDetailsJdbcCall.execute(in);
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		
		return (byte[]) out.get("profilepic");
	}
	
	public void updatePwd(String userName, String pwd, int reset) throws ResourceError {
		try {
			SimpleJdbcCall updatePasswordJdbcCall = new SimpleJdbcCall(
					dataSource).withProcedureName("updatePassword");

			Map<String, Object> inputData = new HashMap<String, Object>();
			inputData.put("_username", userName);
			inputData.put("_pwd", pwd);
			inputData.put("_resetpwd", reset);

			SqlParameterSource in = new MapSqlParameterSource()
					.addValues(inputData);
			updatePasswordJdbcCall.execute(in);

			// TODO: Need to update user status to show reset page

		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
	}

	public List<User> findUserBySkill( String searchString) throws ResourceError {
		String procName = "findUserBySkill";
		Map<String, Object> out = null;
		try {
				SimpleJdbcCall findByCity = new SimpleJdbcCall(dataSource)
					.withProcedureName(procName).returningResultSet("rs1",
							new RowMapper<User>() {
								public User mapRow(ResultSet rs, int rowCount)
										throws SQLException {
																		
									User u = new User.UserBuilder().userName(rs.getString("username"))
											.name(rs.getString("name"))
											.email(rs.getString("email"))
											.contact(rs.getString("contact"))
											.build();

									return u;
								}
							});

			SqlParameterSource in = new MapSqlParameterSource().addValue("skills", 	searchString);

			out = findByCity.execute(in);
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		return (List<User>) out.get("rs1");
	}

		
	// isPwdReset
	public int isPasswordResetNeeded(String userName) throws ResourceError{
		Integer i = 0;
		try {
			SimpleJdbcCall getUserDetailsJdbcCall = new SimpleJdbcCall(
					dataSource).withProcedureName("isPwdReset");

			SqlParameterSource in = new MapSqlParameterSource().addValue(
					"_username", userName);
			Map<String, Object> out = getUserDetailsJdbcCall.execute(in);
			i = (Integer)out.get("_resetpwd");
			
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		return i == null ? 0: i.intValue();
	}
	
	private void handleDataAcessException(DataAccessException e) throws ResourceError {
		ResourceError re = new ResourceError(e);
		re.setErrorString(e.getMessage());

		if(e.getCause() instanceof SQLException) {
			SQLException sqe = (SQLException) e.getCause();
			StackTraceElement[] trace = Thread.currentThread().getStackTrace();
			logger.error("Error While invoking <class>" + trace[1].getClassName() +
					" <method>" + trace[1].getMethodName() + " error message "
					+ sqe.getMessage() + "<sql error> " + sqe.getErrorCode());
			re.setErrorCode(sqe.getErrorCode());
			re.setErrorString(sqe.getMessage());
		}
		
		throw re;
	
	}

	public void updateLocation(String username, Location location)
			throws ResourceError {
		try {
			SimpleJdbcCall updateLocationJdbcCall = new SimpleJdbcCall(
					dataSource).withProcedureName("updateLocation");

			Map<String, Object> inputData = new HashMap<String, Object>();
			inputData.put("_username", username);
			inputData.put("_latitude", location.getLatitude());
			inputData.put("_longitude", location.getLongitude());

			SqlParameterSource in = new MapSqlParameterSource()
					.addValues(inputData);
			updateLocationJdbcCall.execute(in);

		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		
		
	}

	public List<User> findUserByName(String searchString) throws ResourceError {
		String procName = "findUserByName";
		Map<String, Object> out = null;
		try {
				SimpleJdbcCall findByName = new SimpleJdbcCall(dataSource)
					.withProcedureName(procName).returningResultSet("rs1",
							new RowMapper<User>() {
								public User mapRow(ResultSet rs, int rowCount)
										throws SQLException {
																	
									User u = new User.UserBuilder().userName(rs.getString("username"))
											.name(rs.getString("name"))
											.email(rs.getString("email"))
											.contact(rs.getString("contact"))
											.build();

									return u;
								}
							});

			SqlParameterSource in = new MapSqlParameterSource().addValue("_name", 	searchString);

			out = findByName.execute(in);
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		return (List<User>) out.get("rs1");
	}

	
		
	

}
