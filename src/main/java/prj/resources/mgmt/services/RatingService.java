package prj.resources.mgmt.services;

import java.util.List;

import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.Parameter;
import prj.resources.mgmt.domain.Rating;

//TODO: DAO Exception
//TODO: Change the naming to DAO 
public interface RatingService {
	
	/**
	 * Gets average rating for each of the parameters passed as input.
	 * 
	 * @param params
	 * @return
	 * @throws ResourceError 
	 */
	public List<Rating> getRatingAverage(List<Parameter>params) throws ResourceError;
	 
	/**
	 * Fetches the Rating for each individual ratings organized by parameterId and detailId.
	 * 
	 * @param ratingsRequested
	 * @return
	 * @throws ResourceError 
	 */
	public List<Rating> getIndividualRating(List<Rating>ratingsRequested) throws ResourceError;
	
	/**
	 * Rates a parameter in the dataRequest
	 * 
	 * @param rating
	 * @throws ResourceError 
	 */
	public void rateAParam(Rating rating) throws ResourceError;
	
	
}
