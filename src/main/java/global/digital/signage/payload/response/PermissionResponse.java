package global.digital.signage.payload.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PermissionResponse {
    private Long id;
    private String name;
    private String action;
    private List<PermissionResponse> children;
}
