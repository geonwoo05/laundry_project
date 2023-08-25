package aug.laundry.enums.category;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CategoryType_COMMON {
    BASIC("생활빨래 10L", 4000L),
    ADDITIONAL("생활빨래 20L 초과시 10L 당", 3800L);

    private String title;
    private Long price;

    private CategoryType_COMMON(String title, Long price) {
        this.title = title;
        MemberShip memberShip = new MemberShip(true);
        this.price = memberShip.apply(price);
    }

    public static Map<String, Long> getJson() {
        return Arrays.stream(CategoryType_COMMON.values()).collect(Collectors.toMap(field -> field.getTitle(), field -> field.getPrice()));

    }

}
