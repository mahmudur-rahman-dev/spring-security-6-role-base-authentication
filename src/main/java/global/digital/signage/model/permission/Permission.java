package global.digital.signage.model.permission;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String name;

    private Long parent;

    private Long sort;

    private Boolean active;

    @Column(length = 15)
    private String action;

    public Permission(Long id, String name, Long parent, String action) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.sort = id;
        this.active = true;
        this.action = action;
    }

    public Permission(Long id, Long sort, String action) {
        this.id = id;
        this.sort = sort;
        this.action = action;
    }
}
