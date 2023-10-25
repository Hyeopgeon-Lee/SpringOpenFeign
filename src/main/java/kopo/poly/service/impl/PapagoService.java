package kopo.poly.service.impl;

import kopo.poly.dto.PapagoDTO;
import kopo.poly.service.INaverAPIService;
import kopo.poly.service.IPapagoService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PapagoService implements IPapagoService {

    // OpenFeign 정의된 API 인터페이스 가져오기
    private final INaverAPIService naverAPIService;

    @Override
    public PapagoDTO detectLangs(PapagoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".detectLangs Start!");

        String text = CmmUtil.nvl(pDTO.getText()); // 영작할 문장

        // PapagoAPI 호출하기
        // 결과  예 : {"langCode":"ko"}
        PapagoDTO rDTO = naverAPIService.detectLangs(text);

        // 언어 감지를 위한 원문 저장하기
        rDTO.setText(text);

        log.info(this.getClass().getName() + ".detectLangs End!");

        return rDTO;
    }

    @Override
    public PapagoDTO translate(PapagoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".translate Start!");

        // 언어 종류 찾기
        PapagoDTO rDTO = this.detectLangs(pDTO);

        // 찾은 언어 종류
        String langCode = CmmUtil.nvl(rDTO.getLangCode());

        log.info("langCode : " + langCode);

        rDTO = null; // 사용 용도가 끝나서 메모리에서 지우기

        String source = ""; // 원문 언어(한국어 : ko / 영어 : en)
        String target = ""; // 번역할 언어

        if (langCode.equals("ko")) {
            source = "ko";
            target = "en";

        } else if (langCode.equals("en")) {
            source = "en";
            target = "ko";

        } else {
            // 한국어와 영어가 아니면, 에러 발생시키기
            new Exception("한국어와 영어만 번역됩닌다.");
        }

        String text = CmmUtil.nvl(pDTO.getText()); // 번역할 문장

        rDTO = naverAPIService.translate(source, target, text);

        log.info("rDTO : " + rDTO.getMessage().get("result"));

        // 네이버 결과 데이터 구조는 Map구조에 Map 구조에 Map 구조로 3중 Map구조되어 있음
        Map<String, String> result = (Map<String, String>) rDTO.getMessage().get("result");

        String srcLangType = CmmUtil.nvl(result.get("srcLangType"));
        String tarLangType = CmmUtil.nvl(result.get("tarLangType"));
        String translatedText = CmmUtil.nvl(result.get("translatedText"));

        log.info("srcLangType : " + srcLangType);
        log.info("tarLangType : " + tarLangType);
        log.info("translatedText : " + translatedText);

        // API 호출 결과를 기반으로 HTML에서 사용하기 쉽게 새롭게 데이터 구조 정의하기
        rDTO = new PapagoDTO();
        rDTO.setText(text);
        rDTO.setTranslatedText(translatedText);
        rDTO.setScrLangType(srcLangType);
        rDTO.setTarLangType(tarLangType);

        result = null;

        log.info(this.getClass().getName() + ".translate End!");

        return rDTO;
    }

}

