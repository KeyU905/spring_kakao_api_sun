package edu.bit.ex.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import edu.bit.ex.vo.KakaoAuth;
import edu.bit.ex.vo.KakaoProfile;


@Service
public class KakaoService {
    
    private final static String K_CLIENT_ID = "fb77abf1338b4423681cd8a1f4ee4b05";
    private final static String K_REDIRECT_URI = "http://localhost:8282/ex/auth/kakao/callback";
    
    
    //GET /oauth/authorize?client_id={REST_API_KEY}&redirect_uri={REDIRECT_URI}&response_type=code
    //Host: kauth.kakao.com
    
    public String getAuthorizationUrl() {
       String kakaoUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" + K_CLIENT_ID + "&redirect_uri="
             + K_REDIRECT_URI + "&response_type=code";
       
       return kakaoUrl;
    }
    
    //"https://kauth.kakao.com/oauth/token"
    //POST /oauth/token HTTP/1.1
    //Host: kauth.kakao.com
    //Content-type: application/x-www-form-urlencoded;charset=utf-8

    /*
       curl -v -X POST "https://kauth.kakao.com/oauth/token" \
     -d "grant_type=authorization_code" \
     -d "client_id={REST_API_KEY}" \
     -d "redirect_uri={REDIRECT_URI}" \
     -d "code={AUTHORIZATION_CODE}"
     */
    
    private final static String K_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    
    public KakaoAuth getKakaoTokenInfo(String code) {
        //http 요청을 간단하게 해줄 수 있는 클래스
        //Retrofit2 -> 안드로이드 앱. RestTemplate 와 유사.
        
        RestTemplate restTemplate = new RestTemplate();
        // 위의  /*/의 정보를 담을 객체
        
        // 헤더 구성 클래스(Set Header)
        HttpHeaders headers = new HttpHeaders();
        // import 할 때 springframework 거 하기
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        
        //파라미터 넘기기(Set Parameter)
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        // 맵으로 키, 밸류 형태로 넘겨준다.
        params.add("grant_type", "authorization_code");
        params.add("client_id", K_CLIENT_ID);
        params.add("redirect_uri", K_REDIRECT_URI);
        params.add("code", code);
        
        // Set http entity
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params,headers);
        // 헤더와 파라미터 합체! HttpEntity 사용해서.
       
        // 실제 요청하기
        // Http 요청하기 - POST 방식으로 - 그리고 response 변수의 응답을 받음.
        ResponseEntity<String> response = restTemplate.postForEntity(K_TOKEN_URI, kakaoTokenRequest, String.class);
        // String.class 는 String으로 보내겠다는 관습적 표현.
        // postForEntuty. post 방식으로 (a,b,c) a의 uri로 b의 엔티티를 보낸다.
        // json 뿐 아니라 
        //"https://kauth.kakao.com/oauth/token"
        //POST /oauth/token HTTP/1.1   까지.
        
        
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
        
        // Gson, Json Simple, ObjectMapper 세 가지 정도의 클래스가 있음
        Gson gson = new Gson();
        if(response.getStatusCode() == HttpStatus.OK)
        {
            return gson.fromJson(response.getBody(), KakaoAuth.class);
        }
        return null;
    }
    
    
    
    
    
    /*
       curl -v -X GET "https://kapi.kakao.com/v2/user/me" \
      -H "Authorization: Bearer {ACCESS_TOKEN}"
      
      https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
  */
    private final static String K_PROFILE_URI = "https://kapi.kakao.com/v2/user/me";
    
    public KakaoProfile getKakaoProfile(String accessToken) {
        
        RestTemplate restTemplate = new RestTemplate();
        
        // 헤더 구성 클래스(Set Header)
        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);
        // "Bearer " 단어 뒤의 공백 한 칸까지 구현해야. 
        
        
        // Set http entity
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null,headers);
        // 헤더와 파라미터 합체! HttpEntity 사용해서.
        
        // 실제 요청하기
        // Http 요청하기 - POST 방식으로 - 그리고 response 변수의 응답을 받음.
        ResponseEntity<String> response = restTemplate.postForEntity(K_PROFILE_URI, request, String.class);
        
        System.out.println(response.getBody());
        
        // Gson, Json Simple, ObjectMapper 세 가지 정도의 클래스가 있음
         Gson gson = new Gson();
        if(response.getStatusCode() == HttpStatus.OK)
        {
            return gson.fromJson(response.getBody(), KakaoProfile.class);
        }
        return null; 
        
    }
    
}
