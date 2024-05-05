package global.digital.signage.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    @NotBlank(message = "email is required")
    @Email(message = "email format is not valid")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotNull(message = "role id is required")
    private Long roleId;
}
