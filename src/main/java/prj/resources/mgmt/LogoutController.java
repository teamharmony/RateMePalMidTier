package prj.resources.mgmt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/logout")
@Controller
public class LogoutController {
	@Autowired
	private TokenStore tokenStore;
	
	
	public TokenStore getTokenStore() {
		return tokenStore;
	}


	public void setTokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}


	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> logoout(HttpServletRequest request) {
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(request.getParameter("access_token"));
		tokenStore.removeAccessToken(accessToken);
		tokenStore.removeRefreshToken(accessToken.getRefreshToken());
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

}
