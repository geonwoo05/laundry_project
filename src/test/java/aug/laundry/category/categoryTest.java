package aug.laundry.category;

import aug.laundry.dao.EnumDao;
import aug.laundry.dao.LaundryMapper;
import aug.laundry.dao.LaundryRepository;
import aug.laundry.dao.ObjectMapperFactory;
import aug.laundry.dto.DateForm;
import aug.laundry.enums.category.Category;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class categoryTest {

    @Autowired
    private final EnumDao enumDao = new EnumDao(new ObjectMapperFactory());

    @Autowired
    private LaundryRepository laundryRepository;

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
    public void mapperTest() {
    }

    @Test
    public void dateTimeTest() {
        DateForm dateForm = new DateForm();
        System.out.println("dateForm = " + dateForm);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        LocalDateTime with = LocalDateTime.now().with(LocalTime.of(23, 0));
        System.out.println("with = " + with);
    }
}
