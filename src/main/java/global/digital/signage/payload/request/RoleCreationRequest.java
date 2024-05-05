package global.digital.signage.payload.request;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class RoleCreationRequest {
    private String name;
    private String note;
    private List<Long> permissionIds;
}
