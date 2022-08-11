package com.nhnacademy.marketgg.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.marketgg.auth.aop.RdbSessionAspect;
import com.nhnacademy.marketgg.auth.session.rdb.AttributeRepository;
import com.nhnacademy.marketgg.auth.session.rdb.SessionIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("rdb")
@Configuration
@RequiredArgsConstructor
public class RdbSessionConfig {

    private final SessionIdRepository sessionIdRepository;
    private final AttributeRepository attributeRepository;
    private final ObjectMapper objectMapper;

    @Bean
    public RdbSessionAspect rdbSessionAspect() {
        return new RdbSessionAspect(sessionIdRepository, attributeRepository, objectMapper);
    }

}
