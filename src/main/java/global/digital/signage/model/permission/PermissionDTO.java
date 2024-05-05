package global.digital.signage.model.permission;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PermissionDTO {

    private Long id;
    private String name;
    private List<Permission> children;

    public PermissionDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
