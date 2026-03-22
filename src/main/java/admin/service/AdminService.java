package admin.service;

import admin.model.Admin;
import admin.repository.AdminRepo;
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
            Admin admin = new Admin("ADM-001", "Assistant", "rrx2038@gmail.com", "123456", false);
            repository.save(admin);
            System.out.println("Normal Admin created successfully");
        }
        Admin manager = repository.findById("ADM-002");
        if (manager == null) {
            Admin admin = new Admin("ADM-002", "Manager", "revanasiddanimbal82@gmail.com", "123456", true);
            repository.save(admin);
            System.out.println("Supper Admin created successfully");
        }
    }

    public boolean ResetPassword(Scanner input, String adminId) {
        Admin admin = repository.findById(adminId);
        if (admin == null) {
            return false;
        }
        String oldPassword = InputUtil.readValidPassword(input, "Enter old password");
        String newPassword = InputUtil.readValidPassword(input, "Enter new password");
        if (!PasswordUtil.verify(oldPassword, admin.getPassword())) {
            System.out.println("Old password doesn't match");
            return false;
        }
        admin.setPassword(newPassword);
        return repository.update(admin);
    }

    public boolean updateAdmin(Admin admin) {
        if (repository.findById(admin.getId()) == null) {
            return false;
        }
        return repository.update(admin);

    }

    public Admin getAdminById(String id) {
        return repository.findById(id);
    }

    public Admin getAdminByEmail(String email) {
        return repository.findByEmail(email);
    }
}
