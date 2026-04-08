package authentication.factory;

import authentication.strategy.AuthLoginStretegy;
import authentication.strategy.AuthRegisterStrategy;
import authentication.strategy.PasswordRecoveryStrategy;
import user.model.UserRole;

import java.util.Map;

public class AuthStrategyFactory {
    private final Map<UserRole, AuthLoginStretegy> loginStrategies;
    private final Map<UserRole, AuthRegisterStrategy> registerStrategies;
    private final Map<UserRole, PasswordRecoveryStrategy> passwordRecoveryStrategies;

    public AuthStrategyFactory(Map<UserRole, AuthLoginStretegy> loginStrategies,
                               Map<UserRole, AuthRegisterStrategy> registerStrategies,
                               Map<UserRole, PasswordRecoveryStrategy> passwordRecoveryStrategies) {
        this.loginStrategies = loginStrategies;
        this.registerStrategies = registerStrategies;
        this.passwordRecoveryStrategies = passwordRecoveryStrategies;
    }

    public AuthLoginStretegy getLoginStrategy(UserRole role) {
        AuthLoginStretegy strategy = loginStrategies.get(role);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
        return strategy;
    }

    public AuthRegisterStrategy getRegisterStrategy(UserRole role) {
        AuthRegisterStrategy strategy = registerStrategies.get(role);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
        return strategy;
    }

    public PasswordRecoveryStrategy getPasswordRecoveryStrategy(UserRole role) {
        PasswordRecoveryStrategy strategy = passwordRecoveryStrategies.get(role);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
        return strategy;
    }
}