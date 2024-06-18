package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity // 난 엔티티 다
@Table(name ="member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity {
    // 기본키 컬럼명 = member_id AI-> 데이터 저장시 1씩 증가
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //알아서 설정
    private String name;
    //중복허용 X
    @Column(unique = true)
    private String email;
    //알아서 설정
    private String password;
    //알아서 설정
    private String address;
    //알아서 설정
    @Column(unique = true)
    private String phone;
    //Enum -> 컴 : 숫자 우리 : 문자
    //데이터베이스 문자그대로 -> USER, ADMIN
    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto,
                                       PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        member.setPhone(memberFormDto.getPhone());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);

        member.setRole(Role.ADMIN);
        return member;

    }
}
