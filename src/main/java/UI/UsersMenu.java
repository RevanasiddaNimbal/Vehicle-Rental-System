package UI;

import authentication.model.UserRole;

public interface UsersMenu {
    void show(UserRole role, String userId);
}
