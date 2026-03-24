package authentication.strategy;

import authentication.model.AuthUser;

public interface AuthLoginStretegy {
    AuthUser login();

    void forgotPassword();

}
