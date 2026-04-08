package authentication.service;

import authentication.factory.AuthStrategyFactory;
import authentication.model.AuthUser;
import authentication.strategy.AuthLoginStretegy;
import authentication.strategy.AuthRegisterStrategy;
import authentication.strategy.PasswordRecoveryStrategy;
import user.model.UserRole;

public class AuthService {
    private final AuthStrategyFactory factory;


    public AuthService(AuthStrategyFactory factory) {
        this.factory = factory;
    }

    public AuthUser register(UserRole role) {
        AuthRegisterStrategy strategy = factory.getRegisterStrategy(role);
        return strategy.register();
    }

    public AuthUser login(UserRole role) {
        AuthLoginStretegy strategy = factory.getLoginStrategy(role);
        return strategy.login();
    }

    public void forgotPassword(UserRole role) {
        PasswordRecoveryStrategy strategy = factory.getPasswordRecoveryStrategy(role);
        strategy.forgotPassword();
    }
}

