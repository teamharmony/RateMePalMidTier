package prj.resources.mgmt;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import prj.resources.exception.ClientErrorInfo;
import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.Parameter;
import prj.resources.mgmt.domain.Rating;
import prj.resources.mgmt.services.RatingService;

@RequestMapping("/rating")
@Controller
public class RatingController {

	@Autowired
	private RatingService ratingService;
	
	
	@ExceptionHandler()
	public ResponseEntity<ClientErrorInfo> errorHandle(Exception e) {
		ClientErrorInfo c;
		String info = e.getMessage();
		ClientErrorInfo.ErrorType type = ClientErrorInfo.ErrorType.GENERIC;
		if(e instanceof ResourceError)
		{
			info = ((ResourceError)e).getErrorString();
			type = ClientErrorInfo.ErrorType.DATA_ACESS;
		}
		c = new ClientErrorInfo(info, type);
		return new ResponseEntity<ClientErrorInfo>(c, HttpStatus.CONFLICT);
	}

	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Rating> rateAParam(@RequestBody Rating rating, 
			HttpServletRequest request) throws ResourceError {
		
		ratingService.rateAParam(rating);
		return new ResponseEntity<Rating>(HttpStatus.CREATED);
	}
	
	@ResponseBody
	@RequestMapping(value="/averageForParams", method = RequestMethod.GET)
	public List<Rating> getRatingAverage(@RequestParam("paramIds") List<Integer> paramIds,
			HttpServletRequest request) throws ResourceError{
		
		List<Parameter> l = new ArrayList<Parameter>();
		for(Integer id: paramIds) {
			Parameter p =new Parameter();
			p.setId(id);
			l.add(p);
		}
		
		List<Rating> ratings = ratingService.getRatingAverage(l);
		return ratings;
	}

	
	@ResponseBody
	@RequestMapping(value="/individualRating", method = RequestMethod.GET)
	public List<Rating> getIndividualRating(@RequestParam ("detailIds") List<Integer> iRatings, HttpServletRequest request) throws ResourceError{
		List<Rating> rList = new ArrayList<Rating>();
		for(Integer detailId: iRatings) {
			Rating r = new Rating();
			r.setDetailId(detailId);
			rList.add(r);
		}
		
		List<Rating> ratings = ratingService.getIndividualRating(rList);
		return ratings;
	}

		
	
}
