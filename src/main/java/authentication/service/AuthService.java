package authentication.service;

import authentication.factory.AuthStrategyFactory;
import authentication.model.AuthUser;
import authentication.model.UserRole;
import authentication.strategy.AuthLoginStretegy;
import authentication.strategy.AuthRegisterStretegy;
import wallet.stretegy.PostRegisterationStrategy;

public class AuthService {
    private AuthStrategyFactory factory;
    private PostRegisterationStrategy walletStrategy;


    public AuthService(AuthStrategyFactory factory, PostRegisterationStrategy walletStrategy) {
        this.factory = factory;
        this.walletStrategy = walletStrategy;
    }

    public void register(UserRole role) {
        AuthRegisterStretegy strategy = factory.getRegisterStrategy(role);
        AuthUser user = strategy.register();
        if (user == null) {
            return;
        }
        walletStrategy.create(user.getId());
    }

    public AuthUser login(UserRole role) {

        AuthLoginStretegy strategy = factory.getLoginStrategy(role);

        return strategy.login();
    }

    public void resetPassword(UserRole role) {
        AuthLoginStretegy strategy = factory.getLoginStrategy(role);
        strategy.resetPassword();
    }
}

