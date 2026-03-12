package authentication.service;

import authentication.factory.AuthStrategyFactory;
import authentication.model.AuthUser;
import authentication.model.UserRole;
import authentication.strategy.AuthStrategy;

public class AuthService {
    private AuthStrategyFactory factory;

    public AuthService(AuthStrategyFactory factory) {
        this.factory = factory;
    }

    public AuthUser register(UserRole role) {

        AuthStrategy strategy = factory.getStrategy(role);

        return strategy.register();
    }

    public AuthUser login(UserRole role) {

        AuthStrategy strategy = factory.getStrategy(role);

        return strategy.login();
    }
}

