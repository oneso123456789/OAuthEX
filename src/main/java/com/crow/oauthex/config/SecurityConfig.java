package com.crow.oauthex.config;

import com.crow.oauthex.security.handler.LoginSuccessHandler;
import com.crow.oauthex.security.service.AuthUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Log4j2
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true , securedEnabled = true)
public class SecurityConfig {

/*    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .mvcMatchers("/node_modules/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    } 작동확인용*/
    @Autowired
    private AuthUserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


/*    @Bean
    public InMemoryUserDetailsManager user() {
        UserDetails user = User.withUsername("user1")
                //비밀번호 1111 Encode함
                .password("$2a$10$yxmlE1fKRduWxvTqrThx4OByZeY9ZUxdTRoloNn262TkcfAbotyke")
                .roles("USER").build();

        return new InMemoryUserDetailsManager(user);
    } 테스트용*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
/*        http.authorizeRequests().antMatchers("/sample/all").permitAll()
                .antMatchers("/sample/member").hasRole("USER");  Controller에 @PreAuthorize 처리*/

        http.formLogin();
        http.csrf().disable();
        http.logout();
        http.oauth2Login().successHandler(successHandler());
        http.rememberMe().tokenValiditySeconds(60*60).userDetailsService(userDetailsService); //토큰 한시간짜리


        return http.build();
    }

    @Bean
    public LoginSuccessHandler successHandler() {
        return new LoginSuccessHandler(passwordEncoder());
    }


}
