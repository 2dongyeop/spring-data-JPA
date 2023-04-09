package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    //도메인 클래스 컨버터 사용 전
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    //도메인 클래스 컨버터 사용 후
    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    //페이징과 정렬
    // members?page=0&size=3&sort=id,desc&sort=username,desc 과 같이 사용 가능
    @GetMapping("/members")
    public Page<MemberDto> list(
            @PageableDefault(
                    size = 12,
                    sort = "username",
                    direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> map = page.map(member -> new MemberDto(member));
        return map;
    }


    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("member" + i, i));
        }
    }
}
