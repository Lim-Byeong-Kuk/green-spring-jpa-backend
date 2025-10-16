package com.green.blue.red.dto;

import com.green.blue.red.domain.MemberRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
public class MemberDTO {

    private String email;
    private String pw;
    private String nickname;
    private boolean social;

    private List<MemberRole> memberRoleList = new ArrayList<>();
}
