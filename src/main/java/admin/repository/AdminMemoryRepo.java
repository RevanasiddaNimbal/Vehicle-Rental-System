package admin.repository;

import admin.model.Admin;

import java.util.HashMap;
import java.util.Map;

public class AdminMemoryRepo implements AdminRepo {
    private final Map<String, Admin> Storage = new HashMap<>();

    @Override
    public Admin findById(String id) {
        return Storage.get(id);
    }

    @Override
    public boolean save(Admin admin) {
        if (!Storage.containsKey(admin.getId())) return false;
        Storage.put(admin.getId(), admin);
        return true;
    }

    @Override
    public Admin findByEmail(String email) {
        for (Admin admin : Storage.values()) {
            if (admin.getEmail().equals(email)) {
                return admin;
            }
        }
        return null;
    }

    @Override
    public boolean update(Admin admin) {
        if (Storage.get(admin.getId()) == null) {
            return false;
        }
        Storage.put(admin.getId(), admin);
        return true;
    }
}
