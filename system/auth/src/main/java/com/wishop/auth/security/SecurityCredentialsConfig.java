/*
The MIT License

Copyright (c) 2019-2020 kong <congcoi123@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.wishop.auth.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wishop.auth.configurations.TokenConfig;
import com.wishop.auth.services.CredentialService;
import com.wishop.auth.services.TokenService;

//Enable security configuration. This annotation denotes configuration for spring security.
@EnableWebSecurity
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private CredentialService credentialService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// Don't need CSRF for this time
				.csrf().disable()

				// Make sure we use state-less session; Session won't be used to store user's
				// state.
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and()
				// Add a filter to validate the tokens with every request
				.addFilterAfter(new TokenAuthenticationFilter(tokenConfig(), tokenService()),
						UsernamePasswordAuthenticationFilter.class)
				// Add a filter to validate user credentials and add token in the response
				.addFilter(new UsernameAndPasswordAuthenticationFilter(credentialService, authenticationManager(),
						tokenConfig(), tokenService()))

				// Allow ping request (the order is important)
				.authorizeRequests().antMatchers("/ping**").permitAll()

				.and()
				// Allow POST requests for the authentication
				.authorizeRequests().antMatchers(HttpMethod.POST, tokenConfig().getUri()).permitAll()

				// Other requests need be authenticated
				.anyRequest().authenticated()

				// Default response if the client wants to get a resource unauthorized
				.and().exceptionHandling()
				.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED));
	}

	// Spring has UserDetailsService interface, which can be overriden to provide
	// our implementation for fetching user from database (or any other source).
	// The UserDetailsService object is used by the auth manager to load the user
	// from database.
	// In addition, we need to define the password encoder also. So, auth manager
	// can compare and verify passwords.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public TokenConfig tokenConfig() {
		return new TokenConfig();
	}

	@Bean
	public TokenService tokenService() {
		return new TokenService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
