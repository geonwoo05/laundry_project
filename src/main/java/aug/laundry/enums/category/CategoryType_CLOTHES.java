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
public enum CategoryType_CLOTHES {
    Y_SHIRT("와이셔츠", 2100L),
    SCHOOL_UNIFORM_SHIRT("교복셔츠", 2100L),
    SCHOOL_UNIFORM_JACKET("교복자켓", 5000L),
    REGULAR_SHIRT("일반셔츠", 2100L),
    BLOUSE("블라우스", 4200L),
    T_SHIRT("티셔츠", 4200L),
    SWEAT_SHIRT("맨투맨", 4200L),
    HOODIE("후드티", 4800L),
    KNITWEAR("니트", 5500L),
    SWEATER("스웨터", 5500L),
    CARDIGAN("가디건", 5500L),
    PANTS("바지", 4800L),
    SKIRT("스커트", 4800L),
    ONEPIECE("원피스", 6800L),
    JUMPSUIT("점프수트", 6800L),
    ARTIFICIAL_SKIN("인조가죽하의", 11000L),
    VEST("조끼", 3000L),
    PADDED_VEST("패딩조끼", 8000L),
    SKI_BOARD_PANTS("스키,보드 바지", 24800L),
    SKI_BOARD_JUMP_SUIT("스키, 보드 점스수트", 46800L),
    SKI_BOARD_JACKET("스키, 보드 자켓", 37000L),
    PADDED_PANTS("패딩바지", 11000L),
    SUIT_JACKET("정장자켓", 5000L),
    JACKET("자켓", 8000L),
    JUMPER("점퍼", 8000L),
    COAT("코트", 14000L),
    TRENCH_COAT("트렌치 코드", 14000L),
    LIGHTWEIGHT_PADDING("경량패딩", 9000L),
    PADDING("일반패딩", 16800L),
    DOWN_PADDING("다운패딩", 16800L),
    ARTIFICIAL_LEATHER_JACKET("인조가죽자켓", 15000L),
    TIE("넥타이", 2500L),
    MUFFLER("목도리", 4000L),
    SCARF("스카프", 4000L),
    GLOVES("장갑", 4000L),
    KNIT_CAP("니트모자", 4000L),
    CAP_HAT("캡모자", 6000L);

    private String title;
    private Long price;

    CategoryType_CLOTHES(String title, Long price) {
        this.title = title;
        MemberShip memberShip = new MemberShip(true);
        this.price = memberShip.apply(price);
    }

    public static Map<String, Long> getJson() {
        return Arrays.stream(CategoryType_CLOTHES.values()).collect(Collectors.toMap(field -> field.getTitle(), field -> field.getPrice()));
    }

}
