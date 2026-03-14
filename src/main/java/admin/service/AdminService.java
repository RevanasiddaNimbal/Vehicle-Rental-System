package admin.service;

import admin.model.Admin;
import admin.repository.AdminRepo;

public class AdminService {
    private final AdminRepo repository;

    public AdminService(AdminRepo repository) {
        this.repository = repository;
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
