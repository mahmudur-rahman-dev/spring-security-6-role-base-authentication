package global.digital.signage.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import global.digital.signage.model.permission.Permission;
import global.digital.signage.model.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private  Long userId;
    private String username;
    private Long companyId;

    @JsonProperty("roles_permissions")
    private List<String> roles;

    private String accessToken;
    private String refreshToken;
}