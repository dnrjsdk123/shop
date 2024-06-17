package com.shop.service;



import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
@RequiredArgsConstructor // lombok final, @NonNull 변수에 붙음녀 자동 주입(Autowired)을 해줍니다.
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository; //자동주입 된 상태
    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return  memberRepository.save(member); //데이터 베이스에 저장하라는명령
    }
    private void validateDuplicateMember(Member member){
        Member findMemberEmail = memberRepository.findByEmail(member.getEmail());
        Member findMemberPhone = memberRepository.findByPhone(member.getPhone());
        if (findMemberEmail != null){
            throw new IllegalStateException("이메일이 중복되었습니다.");
        }
        if (findMemberPhone != null){
            throw new IllegalStateException("전화번호가 중복되었습니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if (member == null){
            throw new UsernameNotFoundException(email);
        }
        return User.builder().username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
