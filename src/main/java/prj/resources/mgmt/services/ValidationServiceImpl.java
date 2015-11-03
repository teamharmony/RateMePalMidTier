package prj.resources.mgmt.services;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import prj.resources.exception.ResourceError;

public class ValidationServiceImpl implements ValidationService {

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

	
	public int validateUserName(String userName) throws ResourceError {
		Integer i = 0;
		try {
			SimpleJdbcCall validateUserNameJdbcCall = new SimpleJdbcCall(
					dataSource).withProcedureName("isUserNameOccupied");

			SqlParameterSource in = new MapSqlParameterSource().addValue(
					"_username", userName);
			Map<String, Object> out = validateUserNameJdbcCall.execute(in);
			i = (Integer)out.get("_occupied");
			
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		return i == null ? 0: i.intValue();
	}
	

	public int validateUserEmail(String email) throws ResourceError{
		Integer i = 0;
		try {
			SimpleJdbcCall validateEmailJdbcCall = new SimpleJdbcCall(
					dataSource).withProcedureName("isEmailOccupied");

			SqlParameterSource in = new MapSqlParameterSource().addValue(
					"_email", email);
			Map<String, Object> out = validateEmailJdbcCall.execute(in);
			i = (Integer)out.get("_occupied");
			
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		return i == null ? 0: i.intValue();
	}

}
