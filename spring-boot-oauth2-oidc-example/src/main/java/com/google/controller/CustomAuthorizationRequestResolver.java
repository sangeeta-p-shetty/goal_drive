package com.google.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class CustomAuthorizationRequestResolver  implements OAuth2AuthorizationRequestResolver {

	
	private OAuth2AuthorizationRequestResolver defaultResolver;

    public CustomAuthorizationRequestResolver(
      ClientRegistrationRepository repo, String authorizationRequestBaseUri) {
    	System.out.println(" SANGEEauthorizationRequestBaseUri "+authorizationRequestBaseUri);
        defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, authorizationRequestBaseUri);
    }

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		// TODO Auto-generated method stub
		OAuth2AuthorizationRequest req = defaultResolver.resolve(request);
        if(req != null) {
            req = customizeAuthorizationRequest(req);
        }
        return req;
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
		// TODO Auto-generated method stub
		OAuth2AuthorizationRequest req = defaultResolver.resolve(request, clientRegistrationId);
        if(req != null) {
            req = customizeAuthorizationRequest(req);
        }
        return req;
	}
	
	private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest req) {
		
			    return OAuth2AuthorizationRequest.from(req).state("xyz").build();
			}
}
