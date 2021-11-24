package com.google.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;

import com.google.controller.CustomAuthorizationRequestResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	 private static List<String> clients = Arrays.asList("google");
	 
	 private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

	@Resource
	private Environment env;
	

	private ClientRegistration getRegistration(String client) {

		
	    String clientId = env.getProperty(CLIENT_PROPERTY_KEY + "."+client + ".client-id");

	    if (clientId == null) {
	        return null;
	    }

	    String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");
	    String redirectURI = env.getProperty(CLIENT_PROPERTY_KEY + client + ".redirect-uri");
	    if (client.equals("google")) {
	    	
	    	Builder objBuilder = CommonOAuth2Provider.GOOGLE.getBuilder(client);
	    	
	    	objBuilder.clientId(clientId);
	    	objBuilder.redirectUri(redirectURI);
	    	CommonOAuth2Provider.GOOGLE.getBuilder(client)
	          .clientId(clientId).redirectUri(redirectURI);
	        return objBuilder.build();
	    }
	   
	    return null;
	}
	
	@Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream().map(c -> getRegistration(c))
          .filter(registration -> registration != null)
          .collect(Collectors.toList());
        
        return new InMemoryClientRegistrationRepository(registrations);
    }
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http 
	     // only disable these during testing or for non-browser clients
	        .cors().disable()
	        .csrf().disable()
	        .authorizeRequests()
	        .anyRequest().authenticated()
	        .and()
	        .oauth2Login()
	          .loginPage("/oauth2/authorization/google")
	          .authorizationEndpoint()
	          .authorizationRequestResolver(
	            new CustomAuthorizationRequestResolver(
	              clientRegistrationRepository(), "/oauth2/authorization"));
    }

}
