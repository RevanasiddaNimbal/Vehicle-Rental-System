package rental.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Rental {
    private int id;
    private String customerId;
    private String vehicleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private int days;
    private double basePrice;
    private double totalPrice;
    private double weekendCharge;
    private double discount;
    private RentalStatus status;

    public Rental(int id, String customerId, String vehicleId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int days, double basePrice, double totalPrice, double weekendCharge, double discount, RentalStatus status) {
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
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getDays() {
        return days;
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

    public double getBasePrice() {
        return basePrice;
    }

    public void setWeekendCharge(double weekendCharge) {
        this.weekendCharge = weekendCharge;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getWeekendCharge() {
        return weekendCharge;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }
}
