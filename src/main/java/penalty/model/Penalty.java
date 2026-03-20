package penalty.model;

import java.time.LocalDate;

public class Penalty {
    private String id;
    private int rentalId;
    private String vehicleId;
    private String customerId;
    private double amount;
    private PenaltyType type;
    private PenaltyReason reason;
    private LocalDate issuedDate;

    public Penalty(String id, int rentalId, String vehicleId, String customerId, double amount, PenaltyType type, PenaltyReason reason, LocalDate issuedDate) {
        this.id = id;
        this.rentalId = rentalId;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.amount = amount;
        this.type = type;
        this.reason = reason;
        this.issuedDate = issuedDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getRentalId() {
        return rentalId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setPenaltyAmount(double amount) {
        this.amount = amount;
    }

    public PenaltyType getType() {
        return type;
    }

    public PenaltyReason getReason() {
        return reason;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

}
