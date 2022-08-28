package com.example.keycloak_220827.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.io.InputStream;


//spring cloud에 keycloak연동하기
//1. - 스프링 시큐리티 권한 설정은 기존 스프링 시큐리티처럼
//configure(HttpSecurity http)함수가 담당함.
// keycloak role에 대해 인증이 필요하다고 설정
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class KeyCloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

//    registers the keycloakauthenticationprovider with the authentication manager.
//    KeycloakAuthenticationToken을 이용하여 인증정보를 구성하는 메소드
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

//    defines the session authentication strategy
//    토큰을 이용하여 인증에 성공하면, SessionAuthenticationStrategy에 새로 로그인 했음을 통보
//    로그인 성공 후 Redirect-uri를 호출해 주게 되는데
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(
                new SessionRegistryImpl());
    }
// Http 보안을 구성하는 메소드
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("this is Auth Request");
        System.out.println(http.authorizeRequests());
        super.configure(http);

        http.authorizeRequests()
//                .antMatchers("/customers*").hasRole("USER")
//                .antMatchers("/admin*").hasRole("ADMIN")
                .anyRequest().permitAll();
//                .antMatchers("/app*").permitAll()
//                .antMatchers("/tester/**").hasAnyRole("TESTER")
//                .antMatchers("/user/**").hasAnyRole("USER")
//                //.mvcMatchers("/tester*").hasAnyRole("TESTER")
//                .antMatchers("/manager/**").hasAnyRole("MANAGER")
//                .anyRequest().authenticated();

        http.csrf().disable();
    }

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        //return new KeycloakSpringConfigResolverWrapper();
        return new KeycloakConfigResolver() {
            private KeycloakDeployment keycloakDeployment;
            @Override
            public KeycloakDeployment resolve(HttpFacade.Request facade) {
                if (keycloakDeployment != null) {
                    return keycloakDeployment;
                }
                InputStream configInputStream = getClass().getResourceAsStream("/keycloak.json");
                return KeycloakDeploymentBuilder.build(configInputStream);
            }
        };
    }
}

