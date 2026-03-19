package authentication.strategy;

import authentication.model.AuthUser;
import authentication.service.OtpService;
import customer.model.Customer;
import customer.service.CustomerService;
import util.IdGeneratorUtil;
import util.InputUtil;
import util.OtpUtil;
import util.PasswordUtil;

import java.util.Scanner;

public class CustomerAuthStretegy implements AuthLoginStretegy, AuthRegisterStretegy {

    private final CustomerService service;
    private final OtpService otpService;
    private final Scanner input;

    public CustomerAuthStretegy(Scanner input, CustomerService service, OtpService otpService) {
        this.input = input;
        this.service = service;
        this.otpService = otpService;
    }

    @Override
    public void register() {

        String id = IdGeneratorUtil.generateCustomerId();
        String name = InputUtil.readString(input, "Enter Full Name");
        String email = InputUtil.readValidEmail(input, "Enter Email Address");
        String phone = InputUtil.readValidPhone(input, "Enter Phone Number");
        String address = InputUtil.readString(input, "Enter Address");
        String license = InputUtil.readString(input, "Enter Driving License Number");
        String password = InputUtil.readValidPassword(input, "Enter Password");

        if (service.getCustomerByEmail(email) != null) {
            System.out.println("Customer Already Exists");
            return;
        }
        Customer customer = new Customer(id, name, email, phone, address, license, PasswordUtil.getHashPassword(password), false);

        if (OtpUtil.isVerifiedOtp(input, otpService, email)) {
            customer.activate();

            if (service.addCustomer(customer)) {
                System.out.println("Customer Registered Successfully");
            } else {
                System.out.println("Failed to Register Customer");

            }
        } else {
            System.out.println("Registration Failed.");

        }
    }

    @Override
    public AuthUser login() {

        String email = InputUtil.readValidEmail(input, "Enter Email Address");
        String password = InputUtil.readString(input, "Enter Password");

        Customer customer = service.getCustomerByEmail(email);

        if (customer != null &&
                PasswordUtil.verify(password, customer.getPassword())) {

            if (customer.isActive()) {
                System.out.println("Login Successful.");
                return customer;
            } else {
                System.out.println("This account is no longer active. Please contact support.");
                return null;
            }

        } else if (customer == null) {
            System.out.println("Customer not found. Please register.");
            return null;
        } else {
            System.out.println("Invalid email and password.");
            return null;
        }
    }

    @Override
    public void resetPassword() {
        String email = InputUtil.readValidEmail(input, "Enter Email Address");
        Customer customer = service.getCustomerByEmail(email);
        if (customer == null) {
            System.out.println("Customer not found. Please register.");
            return;
        }
        String password = InputUtil.readValidPassword(input, "Enter Password");
        if (OtpUtil.isVerifiedOtp(input, otpService, email)) {
            customer.setPassword(PasswordUtil.getHashPassword(password));
            if (service.updateCustomer(customer)) {
                System.out.println("Password resent successfully.Please login.");
            } else {
                System.out.println("Failed to Resent Password.Please try again.");
            }
        } else {
            System.out.println("Failed to resent Password. Please try again.");
        }
    }

}
