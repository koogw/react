package com.react.react.service;

import com.react.react.data.PopulationData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PopulationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "71516965496b6f6f3636515a765047";
    private final List<String> locations = new ArrayList<>(List.of(
            "강남 MICE 관광특구", "동대문 관광특구", "명동 관광특구", "이태원 관광특구", "잠실 관광특구", "종로·청계 관광특구",
            "홍대 관광특구", "경복궁", "광화문·덕수궁", "보신각", "서울 암사동 유적", "창덕궁·종묘", "가산디지털단지역",
            "강남역", "건대입구역", "고덕역", "고속터미널역", "교대역", "구로디지털단지역", "구로역", "군자역", "남구로역",
            "대림역", "동대문역", "뚝섬역", "미아사거리역", "발산역", "북한산우이역", "사당역", "삼각지역", "서울대입구역",
            "서울식물원·마곡나루역", "서울역", "선릉역", "성신여대입구역", "수유역", "신논현역·논현역", "신도림역", "신림역",
            "신촌·이대역", "양재역", "역삼역", "연신내역", "오목교역·목동운동장", "왕십리역", "용산역", "이태원역", "장지역",
            "장한평역", "천호역", "총신대입구(이수)역", "충정로역", "합정역", "혜화역", "홍대입구역(2호선)", "회기역",
            "4·19 카페거리", "가락시장", "가로수길", "광장(전통)시장", "김포공항", "낙산공원·이화마을", "노량진",
            "덕수궁길·정동길", "방배역 먹자골목", "북촌한옥마을", "서촌", "성수카페거리", "수유리 먹자골목", "쌍문동 맛집거리",
            "압구정로데오거리", "여의도", "연남동", "영등포 타임스퀘어", "외대앞", "용리단길", "이태원 앤틱가구거리", "인사동",
            "창동 신경제 중심지", "청담동 명품거리", "청량리 제기동 일대 전통시장", "해방촌·경리단길", "DDP(동대문디자인플라자)",
            "DMC(디지털미디어시티)", "강서한강공원", "고척돔", "광나루한강공원", "광화문광장", "국립중앙박물관·용산가족공원",
            "난지한강공원", "남산공원", "노들섬", "뚝섬한강공원", "망원한강공원", "반포한강공원", "북서울꿈의숲", "불광천",
            "서리풀공원·몽마르뜨공원", "서울광장", "서울대공원", "서울숲공원", "아차산", "양화한강공원", "어린이대공원",
            "여의도한강공원", "월드컵공원", "응봉산", "이촌한강공원", "잠실종합운동장", "잠실한강공원", "잠원한강공원",
            "청계산", "청와대", "북창동 먹자골목", "남대문시장", "익선동"
    ));


    public List<PopulationData> fetchAllPopulationData() {
        return locations.parallelStream()
                .map(this::fetchDataForLocation)
                .collect(Collectors.toList());
    }
    private PopulationData fetchDataForLocation(String location) {
        String url = String.format("http://openapi.seoul.go.kr:8088/%s/xml/citydata_ppltn/1/5/%s", apiKey, location);
        String xmlResponse = restTemplate.getForObject(url, String.class);

        System.out.println("XML Response for location " + location + ": " + xmlResponse); // XML 응답 출력

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PopulationData.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (PopulationData) unmarshaller.unmarshal(new StringReader(xmlResponse));
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}


