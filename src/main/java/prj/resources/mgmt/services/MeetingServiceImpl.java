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
import prj.resources.mgmt.domain.Location;
import prj.resources.mgmt.domain.Meeting;
import prj.resources.mgmt.domain.User;
import prj.resources.queues.MeetingQueue;

public class MeetingServiceImpl implements MeetingService {

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

		
		public void createMeeting(Meeting meeting) throws ResourceError {
			try {
				SimpleJdbcCall createMeetJdbcCall = new SimpleJdbcCall(
						dataSource).withProcedureName("createMeetRequest");

				Map<String, Object> inputData = new HashMap<String, Object>();
				inputData.put("_fromUserName", meeting.getFromUserName());
				inputData.put("_toUserName", meeting.getToUserName());
				inputData.put("_agenda", meeting.getAgenda());
				inputData.put("_details", meeting.getDetails());
				inputData.put("_dateTime", meeting.getDatetime());
				inputData.put("_venue", meeting.getVenue());
				
				SqlParameterSource in = new MapSqlParameterSource()
				.addValues(inputData);
				
				createMeetJdbcCall.execute(in);
				
				MeetingQueue.addMeeting(meeting.getToUserName());
				
			} catch (DataAccessException e) {
				handleDataAcessException(e);
			}
			
		}
		

		public List<Meeting> showMeetings( String userName) throws ResourceError {
			String procName = "showMeetRequests";
			Map<String, Object> out = null;
			try {
					SimpleJdbcCall showMeets = new SimpleJdbcCall(dataSource)
						.withProcedureName(procName).returningResultSet("rs1",
								new RowMapper<Meeting>() {
									public Meeting mapRow(ResultSet rs, int rowCount)
											throws SQLException {
										
									
										Meeting meet = new Meeting.MeetingBuilder().id(rs.getInt("id"))
												.fromUserName(rs.getString("fromUserName"))
												.toUserName(rs.getString("toUserName"))
												.name(rs.getString("name"))
												.agenda(rs.getString("agenda"))
												.details(rs.getString("details"))
												.dateTime(rs.getInt("meetDateTime"))
												.venue(rs.getString("venue"))
												.status(rs.getInt("status"))
												.build();

										return meet;
									}
								});

				SqlParameterSource in = new MapSqlParameterSource().addValue("_userName", 	userName);

				out = showMeets.execute(in);
			} catch (DataAccessException e) {
				handleDataAcessException(e);
			}
			return (List<Meeting>) out.get("rs1");
		}
		
		public void updateMeeting(String userName, int id, int status) throws ResourceError {
			try {
				SimpleJdbcCall updateJdbcCall = new SimpleJdbcCall(dataSource)
						.withProcedureName("updateMeetRequestStatus");

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
