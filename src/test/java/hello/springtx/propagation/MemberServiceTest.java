package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LogRepository logRepository;

    @Test
    void outerTxOff_success() {
        //given
        String username = "outerTxOff_success";

        //when
        memberService.joinV1(username);

        //then
        assertTrue(memberRepository.find(username)
                .isPresent());

        assertTrue(logRepository.find(username)
                .isPresent());
    }

    @Test
    void outerTxOff_fail() {
        //given
        String username = "로그예외 outerTxOff_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);
        //memberService.joinV1(username);

        //then
        assertTrue(memberRepository.find(username)
                .isPresent());

        assertTrue(logRepository.find(username)
                .isEmpty());
    }

    @Test
    void singleTx() {
        //given
        String username = "singleTx";

        //when
        memberService.joinV1(username);

        //then
        assertTrue(memberRepository.find(username)
                .isPresent());

        assertTrue(logRepository.find(username)
                .isPresent());
    }

    @Test
    void outerTxOn_success() {
        //given
        String username = "outerTxOn_success";

        //when
        memberService.joinV1(username);

        //then
        assertTrue(memberRepository.find(username)
                .isPresent());

        assertTrue(logRepository.find(username)
                .isPresent());
    }

    @Test
    void outerTxOn_fail() {
        //given
        String username = "로그예외 outerTxOn_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);
        //memberService.joinV1(username);

        //then
        assertTrue(memberRepository.find(username)
                .isEmpty());

        assertTrue(logRepository.find(username)
                .isEmpty());
    }

    @Test
    void recoverException_fail() {
        //given
        String username = "로그예외 recoverException_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV2(username))
                .isInstanceOf(UnexpectedRollbackException.class);

        //then
        assertTrue(memberRepository.find(username)
                .isEmpty());

        assertTrue(logRepository.find(username)
                .isEmpty());
    }

    @Test
    void recoverException_success() {
        //given
        String username = "로그예외 recoverException_success";

        //when
        memberService.joinV2(username);

        //then
        assertTrue(memberRepository.find(username)
                .isPresent());

        assertTrue(logRepository.find(username)
                .isEmpty());
    }
}