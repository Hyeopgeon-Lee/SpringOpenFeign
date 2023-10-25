package kopo.poly.service;

import feign.Param;
import feign.RequestLine;
import kopo.poly.dto.PapagoDTO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "PapagoAPI", url = "https://openapi.naver.com")
public interface INaverAPIService {

    @RequestLine("POST /v1/papago/detectLangs")
    PapagoDTO detectLangs(
            @Param("query") String query);

    @RequestLine("POST /v1/papago/n2mt")
    PapagoDTO translate(
            @Param("source") String source,
            @Param("target") String target,
            @Param("text") String text);
}
