package global.digital.signage.model.role;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import global.digital.signage.model.BaseEntity;
import global.digital.signage.model.permission.Permission;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(length = 60)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
//    @JsonBackReference
    private List<Permission> permissions = new ArrayList<>();

    @Column(length = 100)
    private String note;

    private Long companyId;

    @Transient
    private List<Long> permittedList;

//    public List<SimpleGrantedAuthority> getAuthorities() {
//        List<SimpleGrantedAuthority> authorities = permissions
//                .stream()
//                .map(privilege -> new SimpleGrantedAuthority(privilege.getName()))
//                .collect(Collectors.toList());
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.getName()));
//        return authorities;
//    }
}