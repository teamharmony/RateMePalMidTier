package prj.resources.mgmt.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public  final  class FilteredRequest extends HttpServletRequestWrapper {
	private String username;
	
	public FilteredRequest(HttpServletRequest request, String username) {
		super(request);
		this.username = username;
	}
	
	public String getUserName() {
		return username;
	}
	
	/**
	 * We have certain APIS like update Where multiple Filters play a role.
	 * @override
	 */
	public String getParameter(String param) {
		if(param == "username") {
			return username;
		}
		return super.getParameter(param);
	}
	
	
	
}	