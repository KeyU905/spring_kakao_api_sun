package edu.bit.ex.vo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.bit.ex.service.KakaoService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KakaoAuth {

    public String access_token;
    public String token_type;
    public String refresh_token;
    public Integer expires_in;
    public String scope;
    public Integer refresh_token_expires_in;
    
    
}
