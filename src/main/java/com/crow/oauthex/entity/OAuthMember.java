package com.crow.oauthex.entity;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class OAuthMember extends BaseEntity {

    @Id
    private String email;

    private String password;

    private String name;

    private boolean fromSocial;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<OAuthMemberRole> roleSet = new HashSet<>();

    public void addMemberRole(OAuthMemberRole OAuthMemberRole) {
        roleSet.add(OAuthMemberRole);
    }

}
