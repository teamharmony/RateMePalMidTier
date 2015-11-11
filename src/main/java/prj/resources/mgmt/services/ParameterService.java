package prj.resources.mgmt.services;

import java.util.List;

import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.Parameter;

public interface ParameterService {
	
	public void addParameter(Parameter parameter) throws ResourceError;
	
	public void removeParameter(Parameter parameter) throws ResourceError;
	
	public List<Parameter> showParameters(String username) throws ResourceError;
	

}
