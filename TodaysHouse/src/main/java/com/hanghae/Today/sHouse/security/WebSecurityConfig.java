package com.hanghae.Today.sHouse.security;

import com.hanghae.Today.sHouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //    private final CorsFilter corsFilter;
private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web){
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }
    // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        jwtAuthenticationFilter.setFilterProcessesUrl("/user/login");

        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 시큐리티 폼로그인기능 비활성화
                .formLogin().disable()
                // 로그인폼 화면으로 리다이렉트 비활성화
                .httpBasic().disable()
                // UsernamePasswordAuthenticationFilter 단계에서 json로그인과 jwt토큰을 만들어 response 반환
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // AuthenticationManager
                // BasicAuthenticationFilter 단계에서 jwt토큰 검증
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                .authorizeRequests()
                // PreFlight 요청 모두 허가
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()

                // 게시글 인증
                //.antMatchers(HttpMethod.DELETE, "/api/memos/**").authenticated()
                //.antMatchers(HttpMethod.POST, "/api/memos/**").authenticated()
                //.antMatchers(HttpMethod.PUT, "/api/memos/**").authenticated()

                // 댓글 인증    얘네를 하면 로그인을 하지 않고 했을 때 예외처리가 아닌 403 error가 뜬다.
                //.antMatchers(HttpMethod.DELETE, "/api/comment/**").authenticated()
                //.antMatchers(HttpMethod.POST, "/api/comment/**").authenticated()
                //.antMatchers(HttpMethod.PUT, "/api/comment/**").authenticated()

                // 그 외 요청 모두 허가
                .anyRequest().permitAll()
                .and().cors();
    }
}