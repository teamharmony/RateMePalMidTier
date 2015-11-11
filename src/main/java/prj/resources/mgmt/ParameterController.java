package prj.resources.mgmt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import prj.resources.exception.ClientErrorInfo;
import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.Parameter;
import prj.resources.mgmt.services.ParameterService;

@RequestMapping("/parameters")
@Controller
public class ParameterController {

	@Autowired
	private ParameterService parameterService;

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
	public void addParameter(@RequestBody MultiValueMap<String,String> body, 
			HttpServletRequest request) throws ResourceError {
		String creator = request.getParameter("username");
		String text = body.getFirst("text");
		String name = body.getFirst("name");
		
		Parameter p = new Parameter();
		p.setCreator(creator);
		p.setName(name);
		p.setText(text);
		parameterService.addParameter(p);
	}
	
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public List<Parameter> showParameters(HttpServletRequest request) throws ResourceError {
		return parameterService.showParameters(request.getParameter("username"));
	}


	@ResponseBody
	@RequestMapping(value="/{parameterId}", method = RequestMethod.DELETE)
	public void removeParameter(@PathVariable Integer parameterId, HttpServletRequest request) throws ResourceError {
		Parameter p = new Parameter();
		p.setCreator(request.getParameter("username"));
		p.setId(parameterId);
		parameterService.removeParameter(p);

	}
	
}
