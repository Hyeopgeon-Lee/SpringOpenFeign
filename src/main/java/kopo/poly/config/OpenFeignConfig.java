package kopo.poly.config;

import feign.Contract;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeignConfig {

    @Value("${naver.papago.clientId}")
    private String clientId;

    @Value("${naver.papago.clientSecret}")
    private String clientSecret;

    // API 접속을 위해 접속 방법은 기본 값으로 설정함(반드시 설정되어야 함)
    @Bean
    public Contract feignContract() {
        return new Contract.Default();
    }

    /**
     * 네이버API 호출에 사용되는 X-Naver-Client-Id, X-Naver-Client-Secret 설정
     * OpenFeign 통해 호출되는 모든 API 헤더에 적용됨
     */
    @Bean
    public RequestInterceptor requestInterceptor() {

        return requestTemplate -> {
            requestTemplate.header("X-Naver-Client-Id", clientId);
            requestTemplate.header("X-Naver-Client-Secret", clientSecret);
        };
    }

    @Bean
    Logger.Level feignLoggerLevel() {

        /*
        OpenFeign 통해 전송 및 전달받는 모든 과정에 대해 로그 찍기 설정

        NONE: 로깅하지 않음(기본값)
        BASIC: 요청 메소드와 URI와 응답 상태와 실행시간 로깅함
        HEADERS: 요청과 응답 헤더와 함께 기본 정보들을 남김
        FULL: 요청과 응답에 대한 헤더와 바디, 메타 데이터를 남김
        */
        return Logger.Level.FULL;
    }

}
