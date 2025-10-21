//package com.example.Xtrend_Ai.config;
//
//
//import com.example.Xtrend_Ai.service.UserService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Configuration
//@RequiredArgsConstructor
//@Slf4j
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final UserService userService;
//
//
//    /**
//     * this filter gets checked once keycloak verifies the token, its set here to be centralized
//     * the subject of the claims and the extra claims being email are validated and saved to the database
//     * @param request - the request being sent
//     * @param response - the response to be delegated to the controller
//     * @param filterChain - set of filters applied to the incoming request
//     * @throws ServletException
//     * @throws IOException
//     */
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        var auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if(auth!=null && auth.isAuthenticated() && auth.getPrincipal() instanceof JwtAuthenticationToken jwtAuth){
//            Jwt jwt = jwtAuth.getToken();
//            String keyCloakId = jwt.getSubject();
//            String email = jwt.getClaimAsString("email");
//
//            userService.ensureUserProvisioned(keyCloakId, email);
//        }
//
//        filterChain.doFilter(request, response);
//
//
//    }
//
//
//
//}
