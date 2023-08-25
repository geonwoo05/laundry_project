package aug.laundry.enums.category;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Category {
    COMMON("일반", CategoryType_COMMON.getJson()),
    CLOTHES("의류", CategoryType_CLOTHES.getJson()),
    BEDDING("침구류", CategoryType_BEDDING.getJson()),
    SHOES("신발", CategoryType_SHOES.getJson()),
    EMPTY("없음", null);


    private String title;
    private Map<String, Long> subCategory;

    Category(String title, Map<String, Long> subCategory) {
        this.title = title;
        this.subCategory = getJson(title);
    }

    public static String findByTitle(String title) {
        return Arrays.stream(values())
                .filter(category -> category.hasTitle(title))
                .findAny().orElse(EMPTY).getTitle();
    }

    private boolean hasTitle(String title) {
        return subCategory.entrySet().stream().anyMatch(stringLongEntry -> stringLongEntry.getKey().equals(title));
    }

    public Map<String, Long> getJson(String title) {
        if (title.equals("일반")) {
            return CategoryType_COMMON.getJson();
        } else if (title.equals("의류")) {
            return CategoryType_CLOTHES.getJson();
        } else {
            return title.equals("침구류") ? CategoryType_BEDDING.getJson() : CategoryType_SHOES.getJson();
        }
    }

    public static Map<String, Long> getClazz(String title){
        return Arrays.stream(values()).filter(category -> category.hasTitle(title)).findAny().orElse(EMPTY).subCategory;
    }

}

