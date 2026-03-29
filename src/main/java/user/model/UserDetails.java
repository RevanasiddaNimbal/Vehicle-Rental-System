package user.model;

public class UserDetails {
    private final String name;
    private final String email;
    private final String phone;
    private final UserRole roleType;

    public UserDetails(String name, String email, String phone, UserRole roleType) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.roleType = roleType;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public UserRole getRoleType() {
        return roleType;
    }
}
