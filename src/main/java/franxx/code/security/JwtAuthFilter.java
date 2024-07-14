package franxx.code.security;

import franxx.code.security.model.MyUserDetailService;
import franxx.code.security.web.token.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  @Autowired
  private JwtService jwtService;

  @Autowired
  private MyUserDetailService detailService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String jwt = authHeader.substring(7);

    String extractUsername = jwtService.extractUsername(jwt);
    if (extractUsername != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = detailService.loadUserByUsername(extractUsername);
      if (userDetails != null && jwtService.isValidToken(jwt)) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            extractUsername,
            userDetails.getPassword(),
            userDetails.getAuthorities()
        );

        token.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(token);
      }
    }
    filterChain.doFilter(request, response);

  }
}
