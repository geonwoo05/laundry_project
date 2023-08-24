package aug.laundry.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TestTest {

    @Autowired
    private TestDao testDao;

    @Test
    void test(){
        int result = testDao.find();
        Assertions.assertThat(result).isEqualTo(2);
    }
}