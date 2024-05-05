package global.digital.signage.config;


import global.digital.signage.service.security.JwtService;
import global.digital.signage.util.Security.CustomUserDetails;
import global.digital.signage.util.Security.CustomUserDetailsService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static global.digital.signage.util.Constants.AppConstant.API_ENDPOINTS_WHITELIST;
import static global.digital.signage.util.Constants.AppConstant.TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // try to get JWT in cookie or in Authorization Header
        String jwt = jwtService.getJwtFromCookies(request);
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        boolean isPublicApi = Arrays.asList(API_ENDPOINTS_WHITELIST).contains(request.getServletPath());
        if (isPublicApi) {
            filterChain.doFilter(request, response);
            return;
        }

        if ((jwt == null && (authHeader == null || authHeader.startsWith(TOKEN_PREFIX)))) {
            filterChain.doFilter(request, response);
            return;
        }

        // If the JWT is not in the cookies but in the "Authorization" header
        if (jwt == null && authHeader.startsWith(TOKEN_PREFIX)) {
            jwt = authHeader.substring(7);
        }

        if (jwt == null || jwt.isEmpty()) jwt = authHeader;

        final String mobileNumber = jwtService.extractUserName(jwt);

        if (StringUtils.isNotEmpty(mobileNumber)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            CustomUserDetails userDetails = this.customUserDetailsService.loadUserByUsername(mobileNumber);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }
}