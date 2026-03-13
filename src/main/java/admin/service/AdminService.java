package admin.service;

import admin.model.Admin;
import admin.repository.AdminRepo;

public class AdminService {
    private final AdminRepo repository;

    public AdminService(AdminRepo repository) {
        this.repository = repository;
    }

    public Admin getAdminByEmail(String email) {
        return repository.findByEmail(email);
    }
}
