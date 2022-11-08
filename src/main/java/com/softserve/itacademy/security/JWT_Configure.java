package com.softserve.itacademy.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
        import org.springframework.security.web.DefaultSecurityFilterChain;
        import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWT_Configure extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private JwtProvider jwtTokenProvider;

    public JWT_Configure(JwtProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public  void configure(HttpSecurity httpSecurity)throws Exception{
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
