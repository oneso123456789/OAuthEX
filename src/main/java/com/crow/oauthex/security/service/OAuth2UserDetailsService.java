package com.crow.oauthex.security.service;

import com.crow.oauthex.entity.OAuthMember;
import com.crow.oauthex.entity.OAuthMemberRole;
import com.crow.oauthex.repository.OAuthMemberRepository;
import com.crow.oauthex.security.dto.OAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class OAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final OAuthMemberRepository repository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


        log.info("--------------------------------------");
        log.info("userRequest:" + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName: " + clientName);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("=====================================");
        oAuth2User.getAttributes().forEach((k, v) -> {
            log.info(k + ":" + v);
        });

        String email = null;

        if (clientName.equals("Google")) {
            email = oAuth2User.getAttribute("email");
        }

        log.info("EMAIL: " + email);

        OAuthMember oAuthMember = saveSocialMember(email);

        OAuthMemberDTO oAuthMemberDTO = new OAuthMemberDTO(
                oAuthMember.getEmail(),
                oAuthMember.getPassword(),
                true, // fromSocial
                oAuthMember.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );
        oAuthMemberDTO.setName(oAuthMember.getName());

        return oAuthMemberDTO;




    }

    private OAuthMember saveSocialMember(String emil) {

        // 기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회만
        Optional<OAuthMember> result = repository.findByEmail(emil, true);

        if (result.isPresent()) {
            return result.get();
        }

        // 없다면 회원 추가 패스워드는 1111 이름은 그냥 이메일 주소로
        OAuthMember oAuthMember = OAuthMember.builder().email(emil)
                .name(emil)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        oAuthMember.addMemberRole(OAuthMemberRole.USER);

        repository.save(oAuthMember);

        return oAuthMember;
    }
}
