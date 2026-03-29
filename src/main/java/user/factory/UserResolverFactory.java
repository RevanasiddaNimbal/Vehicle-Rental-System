package user.factory;

import user.model.UserDetails;
import user.resolver.UserTypeResolver;

import java.util.Map;

public class UserResolverFactory {
    private final Map<String, UserTypeResolver> resolverRegistry;

    public UserResolverFactory(Map<String, UserTypeResolver> resolverRegistry) {
        this.resolverRegistry = resolverRegistry;
    }

    public UserDetails resolveUserById(String userId) {
        if (userId == null || userId.length() < 3) {
            throw new IllegalArgumentException("Invalid User ID format: " + userId);
        }

        String prefix = userId.substring(0, 3).toUpperCase();
        UserTypeResolver resolver = resolverRegistry.get(prefix);
        if (resolver == null) {
            throw new IllegalArgumentException("No resolver found for user prefix: " + prefix);
        }

        return resolver.resolve(userId);
    }
}