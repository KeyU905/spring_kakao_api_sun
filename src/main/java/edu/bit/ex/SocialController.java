package edu.bit.ex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.bit.ex.service.KakaoService;
import edu.bit.ex.vo.KakaoAuth;
import edu.bit.ex.vo.KakaoProfile;


@Controller
public class SocialController {
	
    @Autowired
    private KakaoService kakaoService;
    
    
	@GetMapping("/social")
	public String social(Model model) {
	    System.out.println("social()..");
	    model.addAttribute("kakaoUrl",kakaoService.getAuthorizationUrl());
		
		return "social/login";
	}
	
	@GetMapping("auth/kakao/callback")
	public String social(String code, Model model) {
	    // 첫 번째 단계 인증코드 받기
	    System.out.println("social().." + code);
	    
	    // 두 번째 단계 인증코드 받아서 넘기기
	    KakaoAuth kakaoAuth = kakaoService.getKakaoTokenInfo(code);
	    	    
	    // 세 번째 프로필 받아오기
	    KakaoProfile profile = kakaoService.getKakaoProfile(kakaoAuth.getAccess_token());
	    
	    model.addAttribute("user", profile);
	    
	    
	    return "social/social_home";
	    
	    
	}
	
}
