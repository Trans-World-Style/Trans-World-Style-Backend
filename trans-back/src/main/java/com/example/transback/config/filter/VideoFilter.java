package com.example.transback.config.filter;

import com.example.transback.util.JwtUtil;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class VideoFilter extends AbstractGatewayFilterFactory<VideoFilter.Config> {
    private static final Logger logger = LogManager.getLogger(VideoFilter.class);
    public VideoFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String jwt = exchange.getRequest().getHeaders().getFirst("Authorization");
            jwt = jwt.replace("Bearer ", ""); // "Bearer " 접두사 제거
            System.out.println(jwt);

            if (jwt != null && JwtUtil.validateJWT(jwt)) {
                // JWT가 유효한 경우 계속 필터 체인 진행
                return chain.filter(exchange);
            } else {
                // 유효하지 않은 경우 401 Unauthorized 응답 반환
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        });
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}