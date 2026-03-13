package admin.repository;

import admin.model.Admin;

public interface AdminRepo {
    public Admin findByEmail(String email);
}

