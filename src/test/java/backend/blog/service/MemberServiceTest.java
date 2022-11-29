package backend.blog.service;

import backend.blog.repository.MemberRepository;
import backend.blog.domain.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void joinTest() throws Exception {
        //given
        Member member = new Member();
        member.setName("김회원");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("박회원");

        Member member2 = new Member();
        member2.setName("박회원");

        //when
        memberService.join(member1);
        memberService.join(member2); // 예외 처리 발생

//        @Test(expected = IllegalStateException.class) 처리로 변경
//        try {
//            memberService.join(member2); // 예외 처리 발생
//        } catch (IllegalStateException e) {
//            return;
//        }

        //then
        fail("정상적으로 예외가 발생하지 못했습니다.");
    }
}