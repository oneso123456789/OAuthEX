package com.crow.oauthex.controller;

import com.crow.oauthex.security.dto.OAuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {

    @GetMapping("/all")
    public void exAll() {
        log.info("exAll...............");
    }

    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal OAuthMemberDTO oAuthMemberDTO) {

        log.info("exMember...............");

        log.info("------------------------");
        log.info(oAuthMemberDTO);
    }

    @GetMapping("/admin")
    public void exAdmin() {
        log.info("exAdmin..................");
    }
}
