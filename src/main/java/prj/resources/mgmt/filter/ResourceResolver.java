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

import prj.resources.queues.FriendsQueue;

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
			
			chain.doFilter(reqWrapper, response);
	
			
	}

}
