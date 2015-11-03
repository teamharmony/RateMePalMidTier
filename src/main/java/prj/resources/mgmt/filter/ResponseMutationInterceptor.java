package prj.resources.mgmt.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import prj.resources.queues.MeetingQueue;
import prj.resources.queues.MessageQueue;

public class ResponseMutationInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		String user = request.getParameter("username");
		
		if (!(request.getServletPath().indexOf("messages") != -1 && request
				.getMethod() == "POST")
				&& MessageQueue.removeMessage(user)) {
			response.addHeader("MessageAvailable", "yes");
		}

		if (!(request.getServletPath().indexOf("meetings") != -1 && request
				.getMethod() == "POST")
				&& MeetingQueue.removeMeeting(user)) {
			response.addHeader("MeetingAvailable", "yes");
		}
		
		super.postHandle(request, response, handler, modelAndView);
		
	}
	
	


}
