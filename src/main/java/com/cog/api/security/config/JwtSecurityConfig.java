package com.cog.api.security.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import com.cog.api.security.AbstractJwtAuthenticationProvider;
import com.cog.api.security.JwtAuthenticationEntryPoint;
import com.cog.api.security.JwtAuthenticationFilter;
import com.cog.api.security.JwtTokenAuthenticationProvider;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private JwtAuthenticationEntryPoint entryPoint;
	
	@Autowired
	List<AbstractJwtAuthenticationProvider> authenticationProviders;

	@Autowired
	JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;
	
	public JwtAuthenticationFilter createJwtAuthenticationFilter(String loginUrl) {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(loginUrl);
		List<AuthenticationProvider> providers = new ArrayList<AuthenticationProvider>(authenticationProviders);
        ProviderManager manager = new ProviderManager(providers);
        jwtAuthenticationFilter.setAuthenticationManager(manager);
        return jwtAuthenticationFilter;
	}
	
	public RequestHeaderAuthenticationFilter createJwtAutorizationFilter() {
		RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
		filter.setPrincipalRequestHeader("authorization");
		filter.setCredentialsRequestHeader("authorization");
		filter.setExceptionIfHeaderMissing(false);
		ProviderManager manager = new ProviderManager(Collections.singletonList(new JwtTokenAuthenticationProvider()));
		filter.setAuthenticationManager(manager);
        return filter;
	}
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()  
        .antMatcher("/api/**")
        	.authorizeRequests()
        		.anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(createJwtAuthenticationFilter("/api/1.0/auth/login"), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(createJwtAutorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers().cacheControl();
    }
    
    
    @Override
	public void configure(WebSecurity web) throws Exception {
    	 web
         .ignoring()
            .antMatchers("/api/1.0/wechat/*");
    }
    
}
