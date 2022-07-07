package com.crow.oauthex.security.handler;

import com.crow.oauthex.security.dto.OAuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private PasswordEncoder passwordEncoder;


    public LoginSuccessHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("------------------");
        log.info("onAuthenticationSuccess");

        OAuthMemberDTO oAuthMemberDTO = (OAuthMemberDTO) authentication.getPrincipal();

        boolean fromsocial = oAuthMemberDTO.isFromSocial();

        log.info("Need Modify Member?" + fromsocial);

        boolean passwordResult = passwordEncoder.matches("1111", oAuthMemberDTO.getPassword());

        if (fromsocial && passwordResult) {
            redirectStrategy.sendRedirect(request, response, "/sample/member?from=social");
            // 리다이렉트 일단 member.html로 보냄

        }
    }
}
