package admin.repository;

import admin.model.Admin;

public interface AdminRepo {
    public boolean save(Admin admin);

    public Admin findByEmail(String email);

    public Admin findById(String id);

    public boolean update(Admin admin);
}

