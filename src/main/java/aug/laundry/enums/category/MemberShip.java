package aug.laundry.enums.category;

import lombok.Setter;

@Setter
public class MemberShip {
    private boolean check;

    public MemberShip(boolean check) {
        this.check = check;
    }

    public Long apply(Long price) {
        if (this.check) {
            return CategoryPriceCalculator.PASS.calculate(price);
        } else {
            return CategoryPriceCalculator.COMMON.calculate(price);
        }
    }
}