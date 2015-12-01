package prj.resources.mgmt.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.User;
import prj.resources.queues.FriendsQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FriendsServiceImpl  implements FriendsService{
	
	private static final Logger logger = LoggerFactory
			.getLogger(FriendsServiceImpl.class);

	
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private void handleDataAcessException(DataAccessException e)
			throws ResourceError {
		ResourceError re = new ResourceError(e);
		re.setErrorString(e.getMessage());

		if (e.getCause() instanceof SQLException) {
			SQLException sqe = (SQLException) e.getCause();
			StackTraceElement[] trace = Thread.currentThread().getStackTrace();
			logger.error("Error While invoking <class>"
					+ trace[1].getClassName() + " <method>"
					+ trace[1].getMethodName() + " error message "
					+ sqe.getMessage() + "<sql error> " + sqe.getErrorCode());
			re.setErrorCode(sqe.getErrorCode());
			re.setErrorString(sqe.getMessage());
		}

		throw re;

	}

	public void addFriend(User user, User friend) throws ResourceError {
		try {
			SimpleJdbcCall addParamJdbcCall = new SimpleJdbcCall(dataSource)
					.withProcedureName("addFriend");

			Map<String, Object> inputData = new HashMap<String, Object>();
			inputData.put("_user1", user.getUsername());
			inputData.put("_user2", friend.getUsername());
			
			SqlParameterSource in = new MapSqlParameterSource()
					.addValues(inputData);

			addParamJdbcCall.execute(in);

		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		
		FriendsQueue.addMessage(friend.getUsername(), "1");
	}

	
	public void updateFriendStatus(User user, User friend, int status) throws ResourceError {
		try {
			SimpleJdbcCall addParamJdbcCall = new SimpleJdbcCall(dataSource)
					.withProcedureName("updateFriendStatus");

			Map<String, Object> inputData = new HashMap<String, Object>();
			inputData.put("_user1", user.getUsername());
			inputData.put("_user2", friend.getUsername());
			inputData.put("_status", status);
			
			SqlParameterSource in = new MapSqlParameterSource()
					.addValues(inputData);

			addParamJdbcCall.execute(in);

		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
	
		FriendsQueue.addMessage(friend.getUsername(), Integer.toString(status));
	}
		
	private List<User> getFriends(User user, String procedure) throws ResourceError {
		final String procName = procedure;
		
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall getFriends = new SimpleJdbcCall(dataSource)
					.withProcedureName(procName).returningResultSet("rs1",
							new RowMapper<User>() {
								public User mapRow(ResultSet rs,
										int rowCount) throws SQLException {
									User u = null;
									if(procName.equals("showNonFriends")){
										u = new User.UserBuilder().name(rs.getString("userName")).userName(rs.getString("displayName")).status(rs.getString("status")).build();
									} else {
										u = new User.UserBuilder().name(rs.getString("userName")).userName(rs.getString("displayName")).build();
									}
									return u;
								}
							});

			SqlParameterSource in = new MapSqlParameterSource().addValue(
					"_user1", user.getUsername());

			out = getFriends.execute(in);
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		return (List<User>) out.get("rs1");
	}
	
	public List<User> showFriends(User user) throws ResourceError {
		return this.getFriends(user, "showFriends");
	}
	
	public List<User> showInvitedFriends(User user) throws ResourceError {
		return this.getFriends(user, "showInvitedFriends");
	}

	public List<User> showPendingFriends(User user) throws ResourceError {
		return this.getFriends(user, "showPendingFriends");
	}

	public List<User> showNonFriends(User user) throws ResourceError {
		return this.getFriends(user, "showNonFriends");
	}

}
