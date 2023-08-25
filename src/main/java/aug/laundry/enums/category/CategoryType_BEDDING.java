package aug.laundry.enums.category;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CategoryType_BEDDING {
    REGULAR_BLANKET("일반이불", 12000L),
    MICROFIBER_BLANKET("극세사이불", 16000L),
    DOWNFER_BLANKET("다운퍼이불 (오리, 거위털)", 22000L),
    WOOL_BLANKET("양모이불", 23000L),
    SILK_QUIT_OF_SILK("실크이불", 25000L),
    BLANKET_PAD("이불패드", 10000L),
    BLANKET_COVER("이불커버", 10000L),
    SINGLE_BLANKET("홑이불", 10000L),
    REGULAR_TOPPER("일반토퍼", 18000L),
    GOOSE_TOPPER("구스토퍼", 25000L),
    PILLOW_COVER("베개커버", 3500L),
    PILLOW_COTTON("베개(솜)", 10000L),
    PILLOW_DOWNFER("베개(다운퍼)", 12000L);

    private String title;
    private Long price;

    CategoryType_BEDDING(String title, Long price) {
        this.title = title;
        MemberShip memberShip = new MemberShip(true);
        this.price = memberShip.apply(price);
    }

    public static Map<String, Long> getJson(){
        return Arrays.stream(CategoryType_BEDDING.values()).collect(Collectors.toMap(field -> field.getTitle(), field -> field.getPrice()));
    }
}
