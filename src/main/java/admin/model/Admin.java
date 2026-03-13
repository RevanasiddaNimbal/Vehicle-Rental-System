package admin.model;

import authentication.model.AuthUser;

public class Admin implements AuthUser {
    private final String id;
    private final String username;
    private final String email;
    private final String password;

    public Admin(String id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }


}
