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
import prj.resources.mgmt.domain.Rating;
import prj.resources.mgmt.domain.User;

public class RatingServiceImpl implements RatingService {


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

	
	public List<Rating> getRatingAverage(List<Parameter> params)
			throws ResourceError {
		
		String idList = "";
		
		for(Parameter p: params) {
			idList = idList.concat(Integer.toString(p.getId()) + ",");
		}
	
		idList = idList.substring(0, idList.length() - 1);
		
		Map<String, String> iParams = new HashMap<String, String>();
		iParams.put("paramList", idList);

		Map<String, Object> out = null;
		try {
			SimpleJdbcCall getRatingAverage = new SimpleJdbcCall(dataSource)
					.withProcedureName("getRatingAverage").returningResultSet("rs1",
							new RowMapper<Rating>() {
								public Rating mapRow(ResultSet rs,
										int rowCount) throws SQLException {
									int paramId = rs.getInt("paramId");
									int avgRating = rs.getInt("rating");
									
									Rating r = new Rating();
									r.setParamId(paramId);
									r.setRating(avgRating);
									return r;
								}
							});
			out = getRatingAverage.execute(new MapSqlParameterSource().addValues(iParams));
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		return (List<Rating>) out.get("rs1");	
	}



	public List<Rating> getIndividualRating(List<Rating> ratingsRequested)
			throws ResourceError {
		//String paramList = "";
		String detailList = "";
		
		for(Rating p: ratingsRequested) {
			//paramList = paramList.concat(Integer.toString(p.getParamId()) + ",");
			detailList = detailList.concat("'" + Integer.toString(p.getDetailId()) + "'" + ",");
		}
	
		//paramList = paramList.substring(0, paramList.length() - 1);
		detailList = detailList.substring(0, detailList.length() - 1);
		
		Map<String, String> iParams = new HashMap<String, String>();
		//iParams.put("paramList", paramList);
		iParams.put("detailList", detailList);
		
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall getIndividualRating = new SimpleJdbcCall(dataSource)
					.withProcedureName("getIndividualRating").returningResultSet("rs1",
							new RowMapper<Rating>() {
								public Rating mapRow(ResultSet rs,
										int rowCount) throws SQLException {
									int paramId = rs.getInt("paramId");
									int detailId = rs.getInt("detailId");
									int rating = rs.getInt("rating");
									
									Rating r = new Rating();
									r.setParamId(paramId);
									r.setDetailId(detailId);
									r.setRating(rating);
									return r;
								}
							});
			out = getIndividualRating.execute(new MapSqlParameterSource().addValues(iParams));
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
		return (List<Rating>) out.get("rs1");	
	}


	public void rateAParam(Rating rating) throws ResourceError {
		Map<String, Object> out = null;
		try {
			SimpleJdbcCall rateAParam = new SimpleJdbcCall(
					dataSource).withProcedureName("rateAParam");

			Map<String, Object> inputData = new HashMap<String, Object>();
			inputData.put("_paramId", rating.getParamId());
			inputData.put("_detailId", rating.getDetailId());
			inputData.put("rating", rating.getRating());
			
			SqlParameterSource in = new MapSqlParameterSource()
					.addValues(inputData);

			out = rateAParam.execute(in);
		} catch (DataAccessException e) {
			handleDataAcessException(e);
		}
	}

}
