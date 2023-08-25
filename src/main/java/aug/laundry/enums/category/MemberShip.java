package aug.laundry.enums.category;

import lombok.Setter;

@Setter
public class MemberShip {
    private boolean check;

    public MemberShip(boolean check) {
        this.check = check;
    }

    public Long apply(Long price) {
        CategoryPriceCalculator categoryPriceCalculator;
        if (this.check) {
            categoryPriceCalculator = CategoryPriceCalculator.PASS;
            return categoryPriceCalculator.calculate(price);
        } else {
            categoryPriceCalculator = CategoryPriceCalculator.COMMON;
            return categoryPriceCalculator.calculate(price);
        }
    }
}