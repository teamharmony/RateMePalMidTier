package prj.resources.mgmt.services;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.Feedback;
import prj.resources.queues.MessageQueue;

public class FeedbackServiceImpl implements FeedbackService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(FeedbackServiceImpl.class);
	
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

	public void createMessage(Feedback feedback) throws ResourceError {
		try {
			SimpleJdbcCall createFeedbackJdbcCall = new SimpleJdbcCall(
					dataSource).withProcedureName("createFeedbackRequest");

			Map<String, Object> inputData = new HashMap<String, Object>();
			inputData.put("_fromUserName", feedback.getFromUserName());
			inputData.put("_subject", feedback.getSubject());
			inputData.put("_message", feedback.getMessage());
			
			SqlParameterSource in = new MapSqlParameterSource()
			.addValues(inputData);
			
			createFeedbackJdbcCall.execute(in);
			
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		
	}
}
