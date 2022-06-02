package com.sp.fc.web.teacher;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TeacherAuthenticationToken implements Authentication {

    private Teacher principal;
    private String credentials;
    private String details;
    private boolean authenticated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return principal == null ? new HashSet<>() : principal.getRole();
    }

    @Override
    public String getName() {
        return principal == null ? "" : principal.getUsername();
    }
}
