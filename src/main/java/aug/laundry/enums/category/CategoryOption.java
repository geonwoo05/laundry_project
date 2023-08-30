package aug.laundry.enums.category;

import lombok.Getter;

@Getter
public enum CategoryOption {

    QUICK_LAUNDRY("빠른세탁"),
    COMMON_LAUNDRY("생활빨래"),
    DRYCLEANING("드라이클리닝"),
    REPAIR("수선");

    private String title;

    CategoryOption(String title) {
        this.title = title;
    }

}
