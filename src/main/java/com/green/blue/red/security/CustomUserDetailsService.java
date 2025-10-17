package com.green.blue.red.security;

import com.green.blue.red.domain.Member;
import com.green.blue.red.dto.MemberDTO;
import com.green.blue.red.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("--------loadUserByUsername----------");

        Member member = memberRepository.getWithRoles(username);
        if(member == null) {
            throw new UsernameNotFoundException("Not Found");
        }
        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList().stream()
                        .map(memberRole -> memberRole.name()).toList()
        );
        log.info("memberDTO : {}",memberDTO);

        return memberDTO;
    }
}
