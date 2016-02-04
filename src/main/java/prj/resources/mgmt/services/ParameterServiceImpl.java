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
import prj.resources.mgmt.domain.Parameter;
import prj.resources.mgmt.domain.ParameterType;

public class ParameterServiceImpl implements ParameterService {

	private static final Logger logger = LoggerFactory
			.getLogger(ParameterServiceImpl.class);

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

	public void removeParameter(Parameter parameter) throws ResourceError {
		try {
			SimpleJdbcCall addParamJdbcCall = new SimpleJdbcCall(dataSource)
					.withProcedureName("removeParameter");

			Map<String, Object> inputData = new HashMap<String, Object>();
			inputData.put("_id", parameter.getId());
			inputData.put("_creator", parameter.getCreator());

			SqlParameterSource in = new MapSqlParameterSource()
					.addValues(inputData);

			addParamJdbcCall.execute(in);

		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}

	}

	public void addParameter(Parameter parameter) throws ResourceError {
		try {
			SimpleJdbcCall addParamJdbcCall = new SimpleJdbcCall(dataSource)
					.withProcedureName("addParameter");

			Map<String, Object> inputData = new HashMap<String, Object>();
			inputData.put("_type", parameter.getType().toString());
			inputData.put("_creator", parameter.getCreator());
			inputData.put("_text", parameter.getText());
			inputData.put("_name", parameter.getName());
			
			SqlParameterSource in = new MapSqlParameterSource()
					.addValues(inputData);

			addParamJdbcCall.execute(in);

		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}

	}

	public List<Parameter> showParameters(String userName) throws ResourceError {
		String procName = "showParameters";
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall showMeets = new SimpleJdbcCall(dataSource)
					.withProcedureName(procName).returningResultSet("rs1",
							new RowMapper<Parameter>() {
								public Parameter mapRow(ResultSet rs,
										int rowCount) throws SQLException {

									Parameter p = new Parameter();
									p.setId(rs.getInt("id"));
									p.setCreator(rs.getString("creator"));
									p.setType(ParameterType.valueOf(rs.getInt("type")));
									p.setName(rs.getString("name"));
									p.setText(rs.getString("text"));
									return p;
								}
							});

			SqlParameterSource in = new MapSqlParameterSource().addValue(
					"_creator", userName);

			out = showMeets.execute(in);
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		return (List<Parameter>) out.get("rs1");
	}

}
