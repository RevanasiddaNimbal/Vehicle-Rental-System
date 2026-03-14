package admin.repository;

import admin.model.Admin;

public interface AdminRepo {
    public Admin findByEmail(String email);

    public Admin findById(String id);

    public boolean update(Admin admin);
}

