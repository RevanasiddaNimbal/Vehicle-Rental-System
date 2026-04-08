package admin.service;

import admin.model.Admin;
import admin.repository.AdminRepo;
import exception.DuplicateResourceException;
import exception.ResourceNotFoundException;
import exception.UnauthorizedAccessException;
import util.InputUtil;
import util.PasswordUtil;

import java.util.Scanner;

public class AdminService {
    private final AdminRepo repository;

    public AdminService(AdminRepo repository) {
        this.repository = repository;
    }

    public void createDefaultAdmins() {
        Admin assistant = repository.findById("ADM-001");
        if (assistant == null) {
            Admin admin = new Admin("ADM-001", "Assistant", "rrx2038@gmail.com", PasswordUtil.getHashPassword("123456"), false);
            repository.save(admin);
            System.out.println("Normal Admin created successfully");
        }
        Admin manager = repository.findById("ADM-002");
        if (manager == null) {
            Admin admin = new Admin("ADM-002", "Manager", "revanasiddanimbal82@gmail.com", PasswordUtil.getHashPassword("123456"), true);
            repository.save(admin);
            System.out.println("Supper Admin created successfully");
        }
    }

    public boolean resetPassword(Scanner input, String adminId) {
        Admin admin = repository.findById(adminId);
        if (admin == null) {
            return false;
        }
        String oldPassword = InputUtil.readValidPassword(input, "Enter old password");
        String newPassword = InputUtil.readValidPassword(input, "Enter new password");
        if (PasswordUtil.verify(oldPassword, PasswordUtil.getHashPassword(newPassword))) {
            throw new IllegalArgumentException("New password must be different from old password");
        }

        if (!PasswordUtil.verify(oldPassword, admin.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }
        admin.setPassword(newPassword);
        return repository.update(admin);
    }

    public boolean updateAdmin(Admin admin) {
        if (repository.findById(admin.getId()) == null) {
            throw new ResourceNotFoundException("Admin ID not found.");
        }

        Admin adminWithSameEmail = repository.findByEmail(admin.getEmail());
        if (adminWithSameEmail != null && !adminWithSameEmail.getId().equals(admin.getId())) {
            throw new DuplicateResourceException("Update failed: The email '" + admin.getEmail() + "' is already in use by another Admin.");
        }

        return repository.update(admin);
    }

    public Admin getAdminById(String id) {
        Admin admin = repository.findById(id);
        if (admin == null) {
            throw new ResourceNotFoundException("Admin not found with ID: " + id);
        }
        return admin;
    }

    public Admin getAdminByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void verifySuperAdmin(String adminId) {
        Admin admin = getAdminById(adminId);
        if (!admin.isSuperAdmin()) {
            throw new UnauthorizedAccessException("Action Denied: Only a Super Admin can perform this operation.");
        }
    }
}