package ru.kmpo.camviewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import ru.kmpo.camviewer.security.Jwt.JwtConfigurer;
import ru.kmpo.camviewer.security.Jwt.JwtTokenProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    private static final String ADMIN_ENDPOINT = "/**";
    private static final String LOGIN_ENDPOINT = "/api/v1/login";
    private static final String CAMERA_ENDPOINT_PROFILE = "/api/v1/camera/profile";
    private static final String CAMERA_ENDPOINT_UPLOAD = "/api/v1/camera/file/**";

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(ADMIN_ENDPOINT).permitAll()
                //.antMatchers(LOGIN_ENDPOINT).permitAll()
                //.antMatchers(CAMERA_ENDPOINT_PROFILE, CAMERA_ENDPOINT_UPLOAD).hasRole("CAMERA")
                //.antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                //.antMatchers(CAMERA_ENDPOINT).hasRole("CAMERA")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
