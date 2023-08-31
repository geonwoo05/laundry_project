package aug.laundry.category;

import aug.laundry.dao.EnumDao;
import aug.laundry.dao.ObjectMapperFactory;
import aug.laundry.enums.category.Category;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class categoryTest {

    @Autowired
    private final EnumDao enumDao = new EnumDao(new ObjectMapperFactory());

    @Test
    public void categoryTest() throws JsonProcessingException {

        // 부츠에 부모 ENUM 가져오기 ( title이 필요하면 .title 붙히면 됩니다)
        System.out.println(Category.BOOTS.getParentCategory());

        // 의류 카테고리에 하위카테고리 가져오기
        System.out.println(Category.CLOTHES.getChildCategories());

        // 전체 하위카테고리 목록 가져오기
        System.out.println(Category.getAll());

        // 전체카테고리 JSON으로 받아오기 (매개변수 : Map<String, Long) )
        System.out.println(enumDao.getJson(Category.getAll()));

        // 해당 카테고리의 하위카테고리 JSON으로 받아오기 (매개변수 : Category)
        System.out.println(enumDao.getJson(Category.BEDDING));
    }

    @Test
    void MapperTest() {

    }

    @Test
    @DisplayName("Enum 카테고리에 카테고리 title과 같은값이 있어야한다")
    void valueOfTest(){
        //given
        Category category = Arrays.stream(Category.values())
                .filter(x -> x.getTitle().equals("와이셔츠"))
                .findAny().orElse(null);
        //when
        String title = category.getTitle();
        //then
        Assertions.assertThat(title).isEqualTo("와이셔츠");
    }

    @Test
    @DisplayName("Enum 카테고리에 카테고리 title과 같은값이 없으니 Null이다")
    void valueOfTest2(){
        //given
        Category category = Arrays.stream(Category.values())
                .filter(x -> x.getTitle().equals("카테고리없음"))
                .findAny().orElse(null);
        //when

        //then
        Assertions.assertThat(category).isNull();
    }
}
