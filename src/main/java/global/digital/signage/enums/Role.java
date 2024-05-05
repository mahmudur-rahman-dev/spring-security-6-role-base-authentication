package global.digital.signage.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static global.digital.signage.enums.Privilege.*;

public enum Role {
    USER("001", Set.of(READ_PRIVILEGE, WRITE_PRIVILEGE)),
    ADMIN("002", Set.of(READ_PRIVILEGE, WRITE_PRIVILEGE, UPDATE_PRIVILEGE, DELETE_PRIVILEGE)),
    OPERATOR("003", Set.of(READ_PRIVILEGE, WRITE_PRIVILEGE, UPDATE_PRIVILEGE)),
    COUNSELOR("004", Set.of(READ_PRIVILEGE, WRITE_PRIVILEGE, UPDATE_PRIVILEGE));

    private final String code;
    private final Set<Privilege> privileges;

    Role(String code, Set<Privilege> privileges) {
        this.code = code;
        this.privileges = privileges;
    }

    public String getCode() {
        return code;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public static Role fromCode(String code) {
        for (Role role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid Role code: " + code);
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = privileges
                .stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.name()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
