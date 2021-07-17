package com.alessandro_molinaro.social_network.configuration;

import com.alessandro_molinaro.social_network.filter.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static String secret;
    public static String param;
    public static String prefix;

    @Autowired
    public SecurityConfig(Environment env) {
        SecurityConfig.secret = env.getProperty("security.secret");
        SecurityConfig.param = env.getProperty("security.param");
        SecurityConfig.prefix = env.getProperty("security.prefix");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilter(new AuthorizationFilter(authenticationManager()))
        .authorizeRequests()
        .antMatchers(
            "/public/**",
            "/v3/api-docs/**",
            "/h2-console/**",
            "/configuration/**",
            "/swagger*/**",
            "/webjars/**")
        .permitAll()
        .anyRequest()
        .authenticated();
        http.headers().frameOptions().disable();
    }
}