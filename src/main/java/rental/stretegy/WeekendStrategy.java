package rental.stretegy;

import rental.model.Rental;

import java.time.LocalDate;

public class WeekendStrategy implements PricingStrategy {

    private static final double FRI_MUL = 0.05;
    private static final double SAT_MUL = 0.10;
    private static final double SUN_MUL = 0.12;

    @Override
    public double calculate(Rental rental) {
        double charge = 0;
        LocalDate start = rental.getStartDate();
        int days = rental.getDays();

        for (int i = 0; i < days; i++) {

            LocalDate date = start.plusDays(i);

            switch (date.getDayOfWeek()) {
                case FRIDAY -> charge += rental.getBasePrice() * FRI_MUL;
                case SATURDAY -> charge += rental.getBasePrice() * SAT_MUL;
                case SUNDAY -> charge += rental.getBasePrice() * SUN_MUL;
            }
        }

        rental.setWeekendCharge(charge);

        return charge;
    }
}
