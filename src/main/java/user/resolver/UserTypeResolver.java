package user.resolver;

import user.model.UserDetails;

public interface UserTypeResolver {
    UserDetails resolve(String userId);

}
