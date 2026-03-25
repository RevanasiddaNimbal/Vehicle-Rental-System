package authentication.service;

import authentication.factory.AuthStrategyFactory;
import authentication.model.AuthUser;
import authentication.model.UserRole;
import authentication.strategy.AuthLoginStretegy;
import authentication.strategy.AuthRegisterStretegy;
import config.StrategyConfig;

public class AuthService {
    private final AuthStrategyFactory factory;
    private final StrategyConfig strategyConfig;


    public AuthService(AuthStrategyFactory factory, StrategyConfig strategyConfig) {
        this.factory = factory;
        this.strategyConfig = strategyConfig;
    }

    public void register(UserRole role) {
        AuthRegisterStretegy strategy = factory.getRegisterStrategy(role);
        AuthUser user = strategy.register();
        if (user == null) {
            return;
        }
        strategyConfig.getWalletStrategy().create(user.getId());
    }

    public AuthUser login(UserRole role) {

        AuthLoginStretegy strategy = factory.getLoginStrategy(role);

        return strategy.login();
    }

    public void forgotPassword(UserRole role) {
        AuthLoginStretegy strategy = factory.getLoginStrategy(role);
        strategy.forgotPassword();
    }
}

