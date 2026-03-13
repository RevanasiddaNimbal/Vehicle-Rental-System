package authentication.service;

import authentication.factory.AuthStrategyFactory;
import authentication.model.AuthUser;
import authentication.model.UserRole;
import authentication.strategy.AuthLoginStretegy;
import authentication.strategy.AuthRegisterStretegy;

public class AuthService {
    private AuthStrategyFactory factory;

    public AuthService(AuthStrategyFactory factory) {
        this.factory = factory;
    }

    public AuthUser register(UserRole role) {

        AuthRegisterStretegy strategy = factory.getRegisterStrategy(role);

        return strategy.register();
    }

    public AuthUser login(UserRole role) {

        AuthLoginStretegy strategy = factory.getLoginStrategy(role);

        return strategy.login();
    }
}

