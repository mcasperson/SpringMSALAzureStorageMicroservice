// src/main/java/com/matthewcasperson/azureapi/configuration/AuthSecurityConfig.java

package com.matthewcasperson.azureapi.configuration;

import com.azure.spring.aad.webapi.AADResourceServerWebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
public class AuthSecurityConfig extends AADResourceServerWebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        // @formatter:off
        http
            .authorizeRequests()
                .anyRequest()
                .authenticated();
        // @formatter:on
    }
}