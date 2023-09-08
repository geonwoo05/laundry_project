package aug.laundry.enums.orderStatus;

import aug.laundry.enums.category.Category;
import aug.laundry.enums.zipcode.ZipCode_SEOUL;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public enum routineOrder {

    중구(Arrays.asList("중앙동", "동광동", "대청동","보수동","부평동","광복동","남포동","영주동")),
    서구( Arrays.asList("동대신동","서대신동","부민동","아미동","초장동","충무동","남부민동","암남동")),
    동구(Arrays.asList("초량동","수정동","수정동","좌천동","범일동")),
    영도구(Arrays.asList("남항동","영선동","신선동","봉래동","청학동","동삼동")),
    부산진구(Arrays.asList("부전동","연지동","초읍동","양정동","전포동","부암동","당감동","가야동","개금동","범천동")),
    동래구(Arrays.asList("수민동","복산동","명륜동","온천동","사직동","안락동","명장동")),
    남구(Arrays.asList("대연동","용호동","용당동","감만동","우암동","문현동")),
    북구(Arrays.asList("구포동","금곡동","화명동","덕천동","만덕동")),
    해운대구(Arrays.asList("우제동","중제동","좌제동","송정동","반여동","반송동","재송동")),
    사하구(Arrays.asList("괴정동","당리동","하단동","신평동","장림동","다대동","구평동","감천동")),
    금정구(Arrays.asList("서동","금사동","부곡동","장전동","선두구동","청룡노포동","남산동","구서동","금성동")),
    강서구(Arrays.asList("대저동","강동동","명지동","가락동","녹산동","가덕도동")),
    연제구(Arrays.asList("거제동","연산동")),
    수영구(Arrays.asList("남천동","수영동","망미동","광안동","민락동")),
    사상구(Arrays.asList("삼락동","모라동","덕포동","괘법동","감전동","주례동","학장동","엄궁동")),
    기장군(Arrays.asList("기장읍","장안읍","정관읍","일광읍","철마면"));

    private List<String> dongName;

    routineOrder(List dongName) {
        this.dongName = dongName;
    }

    public static List<String> regionList(String region){
        routineOrder order = routineOrder.valueOf(region);
        List<String> dongName = order.getDongName();
        return dongName;
    }
}
