package aug.laundry.dao;

import aug.laundry.dto.MemberDto;
import aug.laundry.service.MemberService_kgw;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class TestTest {

    @Autowired
    private TestDao testDao;

    @Autowired
    private MemberService_kgw memberService;

    @Autowired
    private MemberMapper memberMapper;

    @Test
    void test(){
        int result = testDao.find();
        Assertions.assertThat(result).isEqualTo(2);


    }

    @Test
    void testSelectOne(){
        Long memberId = 7L;
        MemberDto memberDto = memberService.selectOne(memberId);
        System.out.println(memberDto);



    }
}