package com.zeneo.chatappspring.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("X-Authorization");
        Cookie[] cookies = request.getCookies();
        Cookie authorizationCookie = null;
        String jwtToken;
        if (header != null && header.startsWith("Bearer ")) {
            jwtToken = header.substring(7);

        } else {
            if (cookies != null) {
                for (Cookie cookie: cookies) {
                    if (cookie.getName().equals("X-Authorization")) {
                        authorizationCookie = cookie;
                    }
                }
            }
            if (authorizationCookie != null){
                jwtToken = authorizationCookie.getValue();
            } else {
                chain.doFilter(request, response);
                return;
            }
        }
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = verifyJWT(jwtToken);
        } catch (TokenExpiredException tokenExpiredException) {
            Cookie cookie = new Cookie("X-Authorization", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }


        assert decodedJWT != null;
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(decodedJWT.getClaim("uid").asString(), null, new ArrayList<>());

        SecurityContextHolder.getContext().setAuthentication(token);


        chain.doFilter(request, response);
    }

    public static DecodedJWT verifyJWT(String token) {
        return JWT
                .require(Algorithm.HMAC256("dklfajgaiodhaodjfahsjauwiqwjeadnaefewjhikdhuiakh"))
                .build().verify(token);
    }

}
