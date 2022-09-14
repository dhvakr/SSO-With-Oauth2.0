package me.dhvakr.sso.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfiguration {

    private static final String[] AUTH_WHITELIST = {
            "/",
            "/getemail/**",
            "/swagger-ui/**",
            "/adduser/**",
            "/date/**",
            "/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and().oauth2Login().defaultSuccessUrl("/welcome",true);

        return http.build();
    }
}
