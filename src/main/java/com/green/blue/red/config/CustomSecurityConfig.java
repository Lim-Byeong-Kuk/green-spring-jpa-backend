package com.green.blue.red.config;

import com.green.blue.red.security.filter.JWTCheckFilter;
import com.green.blue.red.security.handler.APILoginFailHandler;
import com.green.blue.red.security.handler.APILoginSuccessHandler;
import com.green.blue.red.security.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD","GET","POST","PUT","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("========security config================");

        // CORS(Cross-Origin Resource Sharing) 설정을 활성화
        // 프론트엔드(ex:React,Vue) 에서 다른 도메인으로 API 요청할 수 있게 허용
        // 위에 정의된 corsConfigurationSource() 에서 세부 설정을 가져옴
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        // 세션을 Stateless (무상태) 로 만든다. 즉, 서버가 로그인 세션을 저장하지 않음.
        // JWT 기반 인증에서 필수 설정, 사용자의 인증상태는 세션 대신 JWT토큰으로 하겠다는 의미
        http.sessionManagement(sessionConfig ->
                sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // CSRF 보호 비활성화
        // CSRF 는 폼 로그인 시 필요한데, JWT+REST API 방식에서는 필요 없음
        // 세션 기반이 아니므로 CSRF 토큰 검증이 불필요
        http.csrf(config -> config.disable());


        // 폼 로그인 설정
        // 로그인 요청 URL : /api/member/login   -> /login/member/login 으로 POST 요청을 보낼 때 인증절차를 수행
        // 스프링 시큐리티는 원래 디폴트로 /login  -> url 로 요청을 보내면 구현해 놓은 로그인 페이지를 보여줌
        // 그리고 거기서 로그인 하면 post 방식으로 /login 으로 요청이 오는 것

        // 인증 성공 시 APILoginSuccessHandler 동작
        // 인증 실패 시 APILoginFailHandler 동작


        // 인증 성공과 실패는 어디서 판별? -> 결과적으로 CustomUserDatailsService 에서 판별한다.

        // formLogin() 설정하면 Spring Security 는 UsernamePasswordAuthenticationFilter 를 자동으로 필터체인에 등록
        // UsernamePasswordAuthenticationFilter 는 AuthenticationManager 에게 인증하라고 요청
        // AuthenticationManager 는 Authentication Provider 를 조회하여 인증 요구
        // AuthenticationProvider 가 UserDetailsService 를 통해 실제 데이터를 조회 하여 UserDetails 를 결과로 준다.

        // 우리는 CustomUserDetailsService 를 구현해 놓았다. (  UserDetailsSerive 를 implements 하여  )
        // 우리는 CustomUserDetailsService 안에 loadByUsername() 메서드를 어디서도 호출한 적이 없음
        // 그러나 Spring Security 의 Filter 가 동작하면서 내부적으로 호출되게 되어있다.

        http.formLogin(config -> {
            config.loginPage("/api/member/login");
            config.successHandler(new APILoginSuccessHandler());
            config.failureHandler(new APILoginFailHandler());
        });

        // UsernamePasswordAuthenticationFilter 가 동작하기 전에 JWTCheckFilter 를 먼저 동작시키겠다.
        // 클라이언트가 보낸 요청 헤더를 확인하고 JWT 가 유효한지 검사
        http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class);


        // 인증이 됐지만 권한이 부족한 사용하자 특정 리소스에 접근하려고 할 때 실행되는 핸들러
        http.exceptionHandling(config -> {
            config.accessDeniedHandler(new CustomAccessDeniedHandler());
        });

        return http.build();
    }
}
