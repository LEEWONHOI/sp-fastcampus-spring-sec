package com.sp.fc.web.teacher;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Teacher {

    private String id;
    private String username;
    private Set<GrantedAuthority> role;

}
