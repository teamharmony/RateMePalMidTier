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
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.DataRequest;
import prj.resources.mgmt.domain.Parameter;
import prj.resources.mgmt.domain.User;

public class RequestServiceImpl implements RequestService {

	
	private static final Logger logger = LoggerFactory
			.getLogger(RegistrationServiceImpl.class);

	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
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

	private int addDataRequest(String requestName, int friendCreated) throws ResourceError {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall addDataRequest = new SimpleJdbcCall(
					dataSource).withProcedureName("addDataRequest");

			Map<String, Object> inputData = new HashMap<String, Object>();
			inputData.put("requestName", requestName);
			inputData.put("friendCreated", friendCreated);
			
			SqlParameterSource in = new MapSqlParameterSource()
					.addValues(inputData);

			out = addDataRequest.execute(in);
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		
		return (Integer)out.get("requestid");
	}
	
	
	public void addDataRequest(DataRequest request) throws ResourceError {
		try {

			int requestId = this.addDataRequest(request.getRequestName(), request.getFriendCreated());
			
			String paramList = "";
			String friendList = "";
			
			for(Parameter p : request.getParamIds()) {
				paramList = paramList.concat(p.getId() + ",");
			}
			paramList = paramList.substring(0, paramList.length() - 1);
			
			for(User p : request.getFriends()) {
				friendList = friendList.concat(p.getUsername() + ",");
			}
			friendList = friendList.substring(0, friendList.length() - 1);
			
						
			SimpleJdbcCall addDataRequestDetails = new SimpleJdbcCall(dataSource)
					.withProcedureName("addDataRequestDetails");

			Map<String, Object> inputData = new HashMap<String, Object>();

			inputData.put("requestId", requestId);
			inputData.put("paramList", paramList);
			inputData.put("friendList", friendList);
			
			SqlParameterSource in = new MapSqlParameterSource()
					.addValues(inputData);
			addDataRequestDetails.execute(in);
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
	}

	public List<DataRequest> getRatingRequestsByUser(String username)
			throws ResourceError {
		Map<String, String> params = new HashMap<String, String>();
		params.put("user", username);
		
		return this.getRatingRequests(new MapSqlParameterSource().addValues(params), "getRatingRequestsByUser");
	}

	public List<DataRequest> getRatingRequestsToUser(String username)
			throws ResourceError {
		Map<String, String> params = new HashMap<String, String>();
		params.put("user", username);
		
		return this.getRatingRequests(new MapSqlParameterSource().addValues(params), "getRatingRequestsToUser");
	}

	
	
	private List<DataRequest> getRatingRequests(SqlParameterSource in, String procedure) throws ResourceError {
		final String procName = procedure;
		
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall getRequests = new SimpleJdbcCall(dataSource)
					.withProcedureName(procName).returningResultSet("rs1",
							new RowMapper<DataRequest>() {
								public DataRequest mapRow(ResultSet rs,
										int rowCount) throws SQLException {
									int requestId = rs.getInt("requestId");
									String requestName = rs.getString("requestName");
									int friendCreated = rs.getInt("friendCreated");
									int detailId = rs.getInt("detailId");
									int paramId = rs.getInt("paramId");
									String paramName = rs.getString("paramName");
									String paramText = rs.getString("paramText");
									String displayName = rs.getString("displayName");
									String designation = rs.getString("designation");
									
									DataRequest d = new DataRequest();
									d.setRequestId(requestId);
									d.setRequestName(requestName);
									d.setFriendCreated(friendCreated);
									d.setDetailId(detailId);
									
									Parameter p = new Parameter();
									p.setId(paramId);
									p.setName(paramName);
									p.setText(paramText);
									
									User u = new User.UserBuilder().name(displayName).designation(designation).build();
									
									Parameter[] pArray = new Parameter[1]; 
									pArray[0] = p;
									
									User[] uArray = new User[1]; 
									uArray[0] = u;
									
									d.setParamIds(pArray);
									d.setFriends(uArray);
								
									return d;
								}
							});
			out = getRequests.execute(in);
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		return (List<DataRequest>) out.get("rs1");
	}
		
	
}
