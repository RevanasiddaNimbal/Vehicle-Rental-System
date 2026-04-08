package authentication.strategy;

import authentication.model.AuthUser;

public interface AuthRegisterStrategy {
    AuthUser register();
}
