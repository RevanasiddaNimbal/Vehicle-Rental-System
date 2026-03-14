package customer.model;

import authentication.model.AuthUser;

public class Customer implements AuthUser {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String drivingLicenseNumber;
    private String password;
    private boolean active;

    public Customer(String id, String name, String email, String phone, String address, String drivingLicenseNumber, String password, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.drivingLicenseNumber = drivingLicenseNumber;
        this.password = password;
        this.active = active;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

}
