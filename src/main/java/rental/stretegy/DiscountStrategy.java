package rental.stretegy;

import rental.model.Rental;

public class DiscountStrategy implements PricingStrategy {
    private static final double DIS_3_DAY = 0.05;
    private static final double DIS_7_DAY = 0.08;

    @Override
    public double calculate(Rental rental) {
        long days = rental.getDays();
        double baseTotal = days * rental.getBasePrice();

        double discount = 0;

        if (days >= 3 && days <= 7) {
            discount = baseTotal * DIS_3_DAY;
        } else if (days > 7) {
            discount = baseTotal * DIS_7_DAY;
        }

        rental.setDiscount(discount);
        return -discount;

    }
}
