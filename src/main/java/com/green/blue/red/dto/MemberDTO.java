package com.green.blue.red.dto;

import com.green.blue.red.domain.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Getter
@Setter
public class MemberDTO extends User {

    private String email;
    private String pw;
    private String nickname;
    private boolean social;
    private List<String> roleNames = new ArrayList<>();
    //인증(authentication), 인가
    public MemberDTO(String email, String pw, String nickname, boolean social, List<String> roleNames) {
        super(email, pw, roleNames.stream().map(i->new SimpleGrantedAuthority("ROLE_"+i)).toList());
        this.email=email;
        this.pw=pw;
        this.social=social;
        this.roleNames=roleNames;
    }

    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", email);
        dataMap.put("pw",pw);
        dataMap.put("nickname", nickname);
        dataMap.put("social", social);
        dataMap.put("roleNames", roleNames);
        return dataMap;
    }
}
