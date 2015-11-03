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
import prj.resources.mgmt.domain.Meeting;
import prj.resources.mgmt.domain.Message;
import prj.resources.queues.MessageQueue;

public class MessageServiceImpl implements MessageService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(MeetingServiceImpl.class);
	
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


	public void createMessage(Message message) throws ResourceError {
		try {
			SimpleJdbcCall createMeetJdbcCall = new SimpleJdbcCall(
					dataSource).withProcedureName("createMessageRequest");

			Map<String, Object> inputData = new HashMap<String, Object>();
			inputData.put("_fromUserName", message.getFromUserName());
			inputData.put("_toUserName", message.getToUserName());
			inputData.put("_subject", message.getSubject());
			inputData.put("_message", message.getMessage());
			
			SqlParameterSource in = new MapSqlParameterSource()
			.addValues(inputData);
			
			createMeetJdbcCall.execute(in);
			
			MessageQueue.addMessage(message.getToUserName());
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}

	}

	public List<Message> showMessages(String userName) throws ResourceError {
		String procName = "showMessageRequests";
		Map<String, Object> out = null;
		try {
				SimpleJdbcCall showMeets = new SimpleJdbcCall(dataSource)
					.withProcedureName(procName).returningResultSet("rs1",
							new RowMapper<Message>() {
								public Message mapRow(ResultSet rs, int rowCount)
										throws SQLException {
									
								
									Message meet = new Message.MessageBuilder().id(rs.getInt("id"))
											.fromUserName(rs.getString("fromUserName"))
											.toUserName(rs.getString("toUserName"))
											.name(rs.getString("name"))
											.subject(rs.getString("subject"))
											.message(rs.getString("message"))
											.fromStatus(rs.getInt("fromStatus"))
											.toStatus(rs.getInt("toStatus"))
											.build();

									return meet;
								}
							});

			SqlParameterSource in = new MapSqlParameterSource().addValue("_userName", 	userName);

			out = showMeets.execute(in);
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		return (List<Message>) out.get("rs1");

	}

	public void updateMessage(String userName, int id, int status) throws ResourceError {
		try {
			SimpleJdbcCall updateJdbcCall = new SimpleJdbcCall(dataSource)
					.withProcedureName("updateMessageRequestStatus");

			Map<String, Object> inputData = new HashMap<String, Object>();
			inputData.put("_username", userName);
			inputData.put("_id", id);
			inputData.put("_status", status);
			
			SqlParameterSource in = new MapSqlParameterSource()
					.addValues(inputData);
			updateJdbcCall.execute(in);
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
	}

}
