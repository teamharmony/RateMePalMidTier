package prj.resources.mgmt.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import prj.resources.queues.FriendsQueue;

public class ResponseMutationInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		String user = request.getParameter("username");
		
		if(FriendsQueue.hasStatus(user, "1")) {	
			response.addHeader("invitations", "yes");
		}
		
		if(FriendsQueue.hasStatus(user, "2")) {	
			response.addHeader("acceptance", "yes");
		}

		if(FriendsQueue.hasStatus(user, "3")) {	
			response.addHeader("cancellations", "yes");
		}
		
		FriendsQueue.removeMessage(user);
			
		super.postHandle(request, response, handler, modelAndView);
		
	}
	
	


}
