package uk.gov.justice.hmpps.datacompliance.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

@Getter
class AuthAwareAuthenticationToken extends JwtAuthenticationToken {

    private final Object principal;

    AuthAwareAuthenticationToken(final Jwt jwt,
                                 final Object principal,
                                 final Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
        this.principal = principal;
    }
}
