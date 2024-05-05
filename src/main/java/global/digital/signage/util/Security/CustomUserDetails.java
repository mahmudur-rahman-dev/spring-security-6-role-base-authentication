package global.digital.signage.util.Security;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Builder
@Data
public class CustomUserDetails implements UserDetails {
    private String username;
    private String password;
    private Long userId;
    private Long companyId;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String username, String password, Long userId, Long companyId, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.companyId = companyId;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}