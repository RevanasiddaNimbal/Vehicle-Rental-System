package authentication.strategy;

import authentication.model.AuthUser;

public interface AuthStrategy extends AuthLoginStretegy {
    AuthUser register();
}
