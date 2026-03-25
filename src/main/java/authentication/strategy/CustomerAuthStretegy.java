package authentication.strategy;

import authentication.model.AuthUser;
import customer.model.Customer;
import customer.service.CustomerService;
import exception.DuplicateResourceException;
import exception.InactiveUserException;
import otp.service.OtpService;
import util.*;

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
    public AuthUser register() {
        String email = InputUtil.readValidEmail(input, "Enter Email Address");
        try {
            if (service.getCustomerByEmail(email) != null) {
                System.out.println("Registration Failed: A customer with this email already exists.");
                return null;
            }
        } catch (InactiveUserException e) {
            System.out.println("Registration Failed: " + e.getMessage());
            return null;
        }

        String name = InputUtil.readString(input, "Enter Full Name");
        String phone = InputUtil.readValidPhone(input, "Enter Phone Number");
        String address = InputUtil.readString(input, "Enter Address");
        String license = InputUtil.readString(input, "Enter Driving License Number");
        String password = InputUtil.readValidPassword(input, "Enter Password");

        if (!OtpUtil.isVerifiedOtp(input, otpService, email)) {
            System.out.println("Registration Failed: OTP Verification was unsuccessful.");
            return null;
        }


        String id = IdGeneratorUtil.generate(IdPrefix.CUS);

        Customer customer = new Customer(id, name, email, phone, address, license, PasswordUtil.getHashPassword(password));

        try {
            if (service.addCustomer(customer)) {
                System.out.println("Customer Registered Successfully. Welcome, " + customer.getName() + "!");
                return customer;
            } else {
                System.out.println("Failed to Register Customer");
                return null;
            }
        } catch (DuplicateResourceException e) {
            System.out.println("Registration Failed: " + e.getMessage());
            return null;
        }
    }

    @Override
    public AuthUser login() {
        String email = InputUtil.readValidEmail(input, "Enter Email Address");
        String password = InputUtil.readString(input, "Enter Password");

        try {
            Customer customer = service.getCustomerByEmail(email);

            if (customer == null) {
                System.out.println("Login Failed: Customer not found. Please register.");
                return null;
            }

            if (PasswordUtil.verify(password, customer.getPassword())) {
                System.out.println("Login Successful. Welcome back, " + customer.getName() + "!");
                return customer;
            } else {
                System.out.println("Login Failed: Invalid email or password.");
                return null;
            }
        } catch (InactiveUserException e) {
            System.out.println("Login Failed: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void forgotPassword() {
        String email = InputUtil.readValidEmail(input, "Enter Email Address");

        try {
            Customer customer = service.getCustomerByEmail(email);
            if (customer == null) {
                System.out.println("Customer not found. Please register.");
                return;
            }

            String password = InputUtil.readValidPassword(input, "Enter new Password");

            if (OtpUtil.isVerifiedOtp(input, otpService, email)) {
                customer.setPassword(PasswordUtil.getHashPassword(password));

                if (service.updateCustomer(customer)) {
                    System.out.println("Password Reset Successfully. Please login.");
                } else {
                    System.out.println("Failed to Reset Password. Please try again.");
                }
            } else {
                System.out.println("Failed to verify OTP. Please try again.");
            }
        } catch (InactiveUserException e) {
            System.out.println("Failed to reset password: " + e.getMessage());
        }
    }
}