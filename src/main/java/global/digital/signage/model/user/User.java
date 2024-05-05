package global.digital.signage.model.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import global.digital.signage.model.company.Company;
import global.digital.signage.model.role.ERole;
import global.digital.signage.model.role.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "app_users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60)
    private String name;

    @Column(length = 60)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String password;

    private String address;

    private boolean isRootUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonBackReference
    private Company company;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (this.role != null) {
            authorities.addAll(this.role.getPermissions().stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getAction()))
                    .toList());
            authorities.add(new SimpleGrantedAuthority(this.role.getName()));
        }

//        else if (this.isRootUser) {
//            authorities.add(new SimpleGrantedAuthority(ERole.ROLE_ROOT.name());
//        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
