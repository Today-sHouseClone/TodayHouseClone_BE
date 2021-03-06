package com.hanghae.Today.sHouse.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
// import java.net.http.HttpHeaders;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 헤더에서 JWT 를 받아옵니다.
        String bearer = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        if (bearer == null || !bearer.startsWith("Bearer ")) {
            // 다음 Filter 를 타야 API 까지 가고, 돌아왔을 때 종료해준다.
            chain.doFilter(request, response);
            return;
        }
        String token = bearer.substring("Bearer ".length());
        //String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        //System.out.println("받은 토큰 :@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + token);
        // 유효한 토큰인지 확인합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {

//            log.info("request ={}", ((HttpServletRequest) request).getRequestURI());
//            log.info("token = {}", token);
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}