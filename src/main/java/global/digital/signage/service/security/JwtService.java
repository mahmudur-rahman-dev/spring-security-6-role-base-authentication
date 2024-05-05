package global.digital.signage.service.security;

import global.digital.signage.model.user.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token);

//    String generateToken(UserDetails userDetails);

    String generateToken(User user);

    boolean isTokenValid(String token, UserDetails userDetails);
    ResponseCookie generateJwtCookie(String jwt);
    String getJwtFromCookies(HttpServletRequest request);
    ResponseCookie getCleanJwtCookie();

    String generateRefreshToken(User user);

    Claims extractAllClaims(String token);
}