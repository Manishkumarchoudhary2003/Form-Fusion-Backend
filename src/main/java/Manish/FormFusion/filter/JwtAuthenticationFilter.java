//package Manish.FormFusion.Filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.StringUtils;
//
//import java.io.IOException;
//
//
////@Configuration
//public class JwtAuthenticationFilter implements Filter {
//
//    private final AuthenticationManager authenticationManager;
//
//    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        String jwt = extractJwtFromRequest((HttpServletRequest) request);
//
//        if (StringUtils.hasText(jwt) && JwtTokenUtil.validateToken(jwt)) {
//            UsernamePasswordAuthenticationToken authentication = JwtTokenUtil.getAuthentication(jwt);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        chain.doFilter(request, response);
//    }
//
//    private String extractJwtFromRequest(HttpServletRequest request) {
//        // Extract JWT from Authorization header
//        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//
//}
