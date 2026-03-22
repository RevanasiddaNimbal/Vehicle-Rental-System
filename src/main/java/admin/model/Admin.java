package admin.model;

import authentication.model.AuthUser;

public class Admin implements AuthUser {
    private final String id;
    private String username;
    private String email;
    private String password;
    private boolean isSuperAdmin;

    public Admin(String id, String username, String email, String password, boolean isSuperAdmin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isSuperAdmin = isSuperAdmin;
    }

    public String getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public boolean getIsSuperAdmin() {
        return isSuperAdmin;
    }


}
