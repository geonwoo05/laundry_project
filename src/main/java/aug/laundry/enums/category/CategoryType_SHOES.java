package aug.laundry.enums.category;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CategoryType_SHOES {
    SNEAKERS("운동화", 6000L),
    SHOES("구두", 7000L),
    LOAFERS("로퍼", 7000L),
    SPORTS_SHOES("스포츠화", 9000L),
    WALKER("워커", 11000L),
    BOOTS("부츠", 15000L),
    UGG_BOOTS("어그부츠", 20000L);

    private String title;
    private Long price;


    private CategoryType_SHOES(String title, Long price) {
        this.title = title;
        MemberShip memberShip = new MemberShip(true);
        this.price = memberShip.apply(price);
    }

    public static Map<String, Long> getJson() {
        return Arrays.stream(CategoryType_SHOES.values()).collect(Collectors.toMap(field -> field.getTitle(), field -> field.getPrice()));
    }

}

