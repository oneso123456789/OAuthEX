package com.crow.oauthex.security.service;

import com.crow.oauthex.entity.OAuthMember;
import com.crow.oauthex.repository.OAuthMemberRepository;
import com.crow.oauthex.security.dto.OAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final OAuthMemberRepository OAuthMemberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("-------------------");
        log.info("ClubUserDetailsService loadUserByUsername " + username);

        Optional<OAuthMember> result = OAuthMemberRepository.findByEmail(username, false);

        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Check Email or social");
        }

        OAuthMember OAuthMember = result.get();

        log.info("-------------------");
        log.info(OAuthMember);
        log.info("-------------------");

        OAuthMemberDTO clubAuthMember = new OAuthMemberDTO(
                OAuthMember.getEmail(),
                OAuthMember.getPassword(),
                OAuthMember.isFromSocial(),
                OAuthMember.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toSet())
        );

        clubAuthMember.setName(OAuthMember.getName());
        clubAuthMember.setFromSocial(OAuthMember.isFromSocial());



        return clubAuthMember;
    }
}
