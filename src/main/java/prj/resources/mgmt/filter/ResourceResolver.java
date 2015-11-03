package prj.resources.mgmt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.filter.OncePerRequestFilter;

import prj.resources.queues.MeetingQueue;
import prj.resources.queues.MessageQueue;

public final class ResourceResolver extends OncePerRequestFilter {

	private TokenStore tokenStore;
	
	public TokenStore getTokenStore() {
		return tokenStore;
	}

	public void setTokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}


	@Override
	protected void doFilterInternal(final HttpServletRequest request,
			final HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(request.getParameter("access_token"));
			final String user = tokenStore.readAuthentication(accessToken).getName();
			FilteredRequest reqWrapper = new FilteredRequest(request, user);
			
			//Need to find a way using Interceptor. For now, this is the temporary way out.
			HttpServletResponse wrappedResponse = new HttpServletResponseWrapper(response) {
				private boolean done = false;

				public void addHeader(String name, String value) {
					super.addHeader(name, value);
					if(!done)
						addCustomHeader();
				}
			
				private void addCustomHeader() {
					done = true;
				
					if (!(request.getServletPath().indexOf("messages") != -1 && request
							.getMethod() == "POST")
							&& MessageQueue.removeMessage(user)) {
						addHeader("MessageAvailable", "yes");
					}
	
					if (!(request.getServletPath().indexOf("meetings") != -1 && request
							.getMethod() == "POST")
							&& MeetingQueue.removeMeeting(user)) {
						addHeader("MeetingAvailable", "yes");
					}
				
				}
	
			};

			chain.doFilter(reqWrapper, wrappedResponse);
	
			
	}

}
