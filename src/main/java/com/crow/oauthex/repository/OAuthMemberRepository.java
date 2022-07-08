package com.crow.oauthex.repository;

import com.crow.oauthex.entity.OAuthMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OAuthMemberRepository extends JpaRepository<OAuthMember, String> {

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from OAuthMember m where m.fromSocial = :social and m.email =:email")
    Optional<OAuthMember> findByEmail(String email, boolean social);


}
