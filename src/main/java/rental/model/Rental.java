package rental.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Rental {
    private int id;
    private final String customerId;
    private final String vehicleId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private int days;
    private final double basePrice;
    private double totalPrice;
    private double weekendCharge;
    private double discount;
    private double securityDeposit;
    private RentalStatus status;

    public Rental(int id, String customerId, String vehicleId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int days, double basePrice, double totalPrice, double weekendCharge, double discount, double securityDeposit, RentalStatus status) {
        if (customerId == null || vehicleId == null || startDate == null || endDate == null || startTime == null || endTime == null || status == null) {
            throw new IllegalArgumentException("Rental properties cannot be null.");
        }
        this.id = id;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
        this.basePrice = basePrice;
        this.totalPrice = totalPrice;
        this.weekendCharge = weekendCharge;
        this.discount = discount;
        this.securityDeposit = securityDeposit;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        if (days < 0) throw new IllegalArgumentException("Days cannot be negative.");
        this.days = days;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getWeekendCharge() {
        return weekendCharge;
    }

    public void setWeekendCharge(double weekendCharge) {
        this.weekendCharge = weekendCharge;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        if (status == null) throw new IllegalArgumentException("Status cannot be null.");
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Rental)) return false;
        Rental rental = (Rental) obj;
        return id == rental.id;
    }
}