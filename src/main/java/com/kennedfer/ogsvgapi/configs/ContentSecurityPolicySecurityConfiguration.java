
package com.kennedfer.ogsvgapi.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ContentSecurityPolicySecurityConfiguration {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers(headers -> headers
                .contentSecurityPolicy( csp -> csp
                    .policyDirectives("img-src * data:; default-src 'self"))
            );
        return http.build();
    }
}
