package global.digital.signage.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class RoleUpdateRequest {
    private Long id;
    private String name;
    private String note;
    private List<Long> permissionIds;
}
