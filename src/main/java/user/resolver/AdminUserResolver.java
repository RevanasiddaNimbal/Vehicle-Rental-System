package user.resolver;

import admin.model.Admin;
import admin.service.AdminService;
import exception.ResourceNotFoundException;
import user.model.UserDetails;
import user.model.UserRole;

public class AdminUserResolver implements UserTypeResolver {
    private final AdminService adminService;

    public AdminUserResolver(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public UserDetails resolve(String userId) {
        Admin admin = adminService.getAdminById(userId);
        if (admin == null) {
            throw new ResourceNotFoundException("Admin ID " + userId + " not found.");
        }

        return new UserDetails(admin.getUsername(), admin.getEmail(), "N/A", UserRole.ADMIN);
    }
}