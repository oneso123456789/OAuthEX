package com.crow.oauthex.security;

import com.crow.oauthex.entity.OAuthMember;
import com.crow.oauthex.entity.OAuthMemberRole;
import com.crow.oauthex.repository.OAuthMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class OAuthMemberTests {

    @Autowired
    private OAuthMemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies() {

        //1 - 80까지는 USER만 지정
        //81- 90까지는 USER,MANAGER
        //91- 100까지는 USER,MANAGER,ADMIN

        IntStream.rangeClosed(1,100).forEach(i -> {
            OAuthMember oAuthMember = OAuthMember.builder()
                    .email("user"+i+"@test.com")
                    .name("사용자"+i)
                    .fromSocial(false)
                    .roleSet(new HashSet<OAuthMemberRole>())
                    .password(  passwordEncoder.encode("1111") )
                    .build();

            //default role
            oAuthMember.addMemberRole(OAuthMemberRole.USER);

            if(i > 80){
                oAuthMember.addMemberRole(OAuthMemberRole.MANAGER);
            }

            if(i > 90){
                oAuthMember.addMemberRole(OAuthMemberRole.ADMIN);
            }

            repository.save(oAuthMember);

        });

    }



    @Test
    public void testRead(){

        Optional<OAuthMember> result = repository.findByEmail("user95@test.com",false);

        OAuthMember OAuthMember = result.get();

        System.out.println(OAuthMember);
        OAuthMember.addMemberRole(OAuthMemberRole.USER);
    }
}
