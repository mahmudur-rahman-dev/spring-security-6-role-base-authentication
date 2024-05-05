package global.digital.signage.service.security;

import global.digital.signage.payload.request.AuthenticationRequest;
import global.digital.signage.payload.request.RegisterRequest;
import global.digital.signage.payload.request.UserRegistrationRequest;
import global.digital.signage.payload.response.AuthenticationResponse;
import global.digital.signage.payload.response.RegistrationResponse;

public interface AuthenticationService {
    RegistrationResponse adminRegistration(RegisterRequest request);

    RegistrationResponse userRegistration(UserRegistrationRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
