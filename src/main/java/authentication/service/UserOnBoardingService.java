package authentication.service;

import authentication.model.AuthUser;
import exception.UserRegisterationFailedException;
import user.model.UserRole;
import wallet.strategy.PostRegisterationStrategy;

public class UserOnBoardingService {
    private AuthService authService;
    private PostRegisterationStrategy strategy;

    public UserOnBoardingService(AuthService authService, PostRegisterationStrategy strategy) {
        this.authService = authService;
        this.strategy = strategy;
    }

    public void registerNewUser(UserRole role) {
        AuthUser user = authService.register(role);
        if (user == null) {
            throw new UserRegisterationFailedException("Failed to register user.");
        }
        strategy.create(user.getId());
    }
}
