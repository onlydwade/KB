package com.bytefinger.toutuo.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @author pat
 * @date 2022/10/11 12:12
 **/
@Configuration
public class CorsConfig {

    @Value("${cors.open:YES}")
    private String openCors;

    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        if ("YES".equals(openCors)) {
            config.addAllowedMethod("*");
            config.addAllowedOrigin("*");
            config.addAllowedHeader("*");
        }
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }

}
