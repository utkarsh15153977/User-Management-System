package com.user.usermanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
//@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(CustomUserDetailsService userDetailsService, JwtUtil jwtUtil){
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        log.info("Header of the request URL ", header);
        String path = request.getRequestURI();
        log.info("Path of Request URL", path);
        if(header==null && !header.startsWith("Bearer ")){
            log.info("Header of the request URL ", header);
        }
        else{
            String token = header.substring(7);
            try{
                String username = jwtUtil.extractUsername(token);
                if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    //Validation before token generation.
                    if(jwtUtil.validateToken(token, userDetails)){
                        UsernamePasswordAuthenticationToken auth =  new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        log.info("User {} Successfully Created", username);
                    }
                }
            }
            catch (Exception e){
                log.info("JWT Authentication Failed {}", e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
