package cancellation.model;

import java.time.LocalDateTime;

public class CancellationRecord {
    private String id;
    private int rentalId;
    private String customerId;
    private String vehicleId;
    private String ownerId;
    private PolicyType policy;
    private double refundAmount;
    private LocalDateTime canceledAt;

    public CancellationRecord(String id, int rentalId, String customerId, String vehicleId, String ownerId, PolicyType policy, double refundAmount, LocalDateTime canceledAt) {
        this.id = id;
        this.rentalId = rentalId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.ownerId = ownerId;
        this.policy = policy;
        this.refundAmount = refundAmount;
        this.canceledAt = canceledAt;
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

    public String getCustomerId() {
        return customerId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public PolicyType getPolicyApplied() {
        return policy;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }
}
